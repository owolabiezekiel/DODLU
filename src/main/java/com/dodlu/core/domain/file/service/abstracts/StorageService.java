package com.dodlu.core.domain.file.service.abstracts;

import com.dodlu.core.app.model.request.file.ImageDownloadRequest;
import java.util.concurrent.CompletableFuture;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  CompletableFuture<String> uploadDocument(MultipartFile file);

  CompletableFuture<String> uploadImage(MultipartFile file);

  CompletableFuture<byte[]> downloadFile(ImageDownloadRequest imageDownloadRequest);

  CompletableFuture<Void> deleteFile(ImageDownloadRequest imageDownloadRequest);
}
