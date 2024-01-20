package com.dodlu.core.domain.storage;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.MockitoAnnotations.openMocks;

import com.amazonaws.services.s3.AmazonS3;
import com.dodlu.core.app.exceptions.CoreException;
import com.dodlu.core.app.exceptions.CoreExceptionService;
import com.dodlu.core.domain.file.service.concretes.AwsStorageService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class StorageServiceTest {
  @MockBean private AmazonS3 amazonS3;
  @MockBean private AwsStorageService storageService;

  @BeforeEach
  void setUp() {
    openMocks(this);
    CoreExceptionService exception = new CoreExceptionService();
    storageService = new AwsStorageService(exception, amazonS3);
  }

  @Test
  public void shouldNotUploadImageFile_whenIncorrectFormat() {
    String fileName = "test.txt";
    String contentType = "text/plain";
    String content = "Hello, world!";
    byte[] contentBytes = content.getBytes();
    MockMultipartFile mockFile =
        new MockMultipartFile(fileName, fileName, contentType, contentBytes);

    CompletableFuture<String> upload = storageService.uploadImage(mockFile);
    assertThatExceptionOfType(CompletionException.class)
        .isThrownBy(upload::join)
        .withCauseInstanceOf(CoreException.class)
        .satisfies(
            cause ->
                assertThat(cause.getCause().getMessage())
                    .isEqualTo("Image format .txt not supported"));
  }

  @Test
  public void shouldNotUploadDocFile_whenIncorrectFormat() {
    String fileName = "test.pptx";
    String contentType = "text/plain";
    String content = "Hello, world!";
    byte[] contentBytes = content.getBytes();
    MockMultipartFile mockFile =
        new MockMultipartFile(fileName, fileName, contentType, contentBytes);

    CompletableFuture<String> upload = storageService.uploadDocument(mockFile);
    assertThatExceptionOfType(CompletionException.class)
        .isThrownBy(upload::join)
        .withCauseInstanceOf(CoreException.class)
        .satisfies(
            cause ->
                assertThat(cause.getCause().getMessage())
                    .isEqualTo("Document format .pptx not supported"));
  }
}
