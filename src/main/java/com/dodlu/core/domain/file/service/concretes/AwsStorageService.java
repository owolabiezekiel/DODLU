package com.dodlu.core.domain.file.service.concretes;

import static com.dodlu.core.app.utils.FileUtils.isValidDocumentFormat;
import static com.dodlu.core.app.utils.FileUtils.isValidImageFormat;
import static java.lang.String.format;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.app.model.request.file.ImageDownloadRequest;
import com.dodlu.core.domain.file.service.abstracts.StorageService;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AwsStorageService implements StorageService {
  private final CoreExceptionService exceptionService;
  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.bucket-name}")
  private String awsBucketName;

  @Value("${cloud.aws.region.static}")
  private String region;

  @Override
  public CompletableFuture<String> uploadImage(MultipartFile file) {
    return supplyAsync(
            () -> {
              String filenameExtension =
                  StringUtils.getFilenameExtension(file.getOriginalFilename());
              ensureImageFormatIsSupported(filenameExtension);
              return filenameExtension;
            })
        .thenCompose(extension -> uploadFile(file, extension));
  }

  @Override
  public CompletableFuture<String> uploadDocument(MultipartFile file) {
    return supplyAsync(
            () -> {
              String filenameExtension =
                  StringUtils.getFilenameExtension(file.getOriginalFilename());
              ensureDocumentFormatIsSupported(filenameExtension);
              return filenameExtension;
            })
        .thenCompose(extension -> uploadFile(file, extension));
  }

  private CompletableFuture<String> uploadFile(MultipartFile file, String filenameExtension) {
    return supplyAsync(
        () -> {
          String key = UUID.randomUUID() + "." + filenameExtension;

          ObjectMetadata metaData = new ObjectMetadata();
          metaData.setContentLength(file.getSize());
          metaData.setContentType(file.getContentType());

          try {
            amazonS3.putObject(awsBucketName, key, file.getInputStream(), metaData);
          } catch (IOException e) {
            throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file");
          } catch (MaxUploadSizeExceededException e) {
            throw exceptionService.handleCreateBadRequest("File too large");
          } catch (Exception e) {
            throw exceptionService.handleCreateException(
                format("An unknown error has occurred: %s", e.getMessage()), e.getCause());
          }

          return format("https://%s.s3.%s.amazonaws.com/%s", awsBucketName, region, key);
        });
  }

  @Override
  public CompletableFuture<byte[]> downloadFile(ImageDownloadRequest request) {
    return supplyAsync(
        () -> {
          String[] split = request.getImageUrl().split("(?<!/)/(?!/)");

          try {
            String key = split[1];
            S3Object object = amazonS3.getObject(awsBucketName, key);
            S3ObjectInputStream objectContent = object.getObjectContent();
            return IOUtils.toByteArray(objectContent);
          } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download the file", e.getCause());
          } catch (ArrayIndexOutOfBoundsException e) {
            throw exceptionService.handleCreateBadRequest(
                format("Invalid file url supplied %s", request.getImageUrl()));
          }
        });
  }

  @Override
  public CompletableFuture<Void> deleteFile(ImageDownloadRequest request) {
    return runAsync(
        () -> {
          String[] split = request.getImageUrl().split("(?<!/)/(?!/)");

          try {
            String key = split[1];
            amazonS3.deleteObject(awsBucketName, key);
          } catch (SdkClientException e) {
            throw exceptionService.handleCreateBadRequest(
                "Failed to delete the file", e.getCause());
          } catch (Exception e) {
            throw exceptionService.handleCreateException(
                format("Unknown error occurred while deleting file : %s", e.getMessage()));
          }
        });
  }

  private void ensureImageFormatIsSupported(String filenameExtension) {
    Boolean formatSupported = isValidImageFormat(filenameExtension);
    if (!formatSupported)
      throw exceptionService.handleCreateBadRequest(
          format("Image format .%s not supported", filenameExtension));
  }

  private void ensureDocumentFormatIsSupported(String filenameExtension) {
    Boolean formatSupported = isValidDocumentFormat(filenameExtension);
    if (!formatSupported)
      throw exceptionService.handleCreateBadRequest(
          format("Document format .%s not supported", filenameExtension));
  }
}
