package com.dodlu.core.app.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.dodlu.core.app.model.request.file.ImageDownloadRequest;
import com.dodlu.core.app.model.response.ResponseModel;
import com.dodlu.core.domain.file.service.abstracts.StorageService;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/file")
@CrossOrigin("*")
public class FileController {
  private final StorageService storageService;

  @PostMapping(
      value = "/image-upload",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @Operation(description = "Upload image file to storage bucket")
  @ApiResponse(code = 200, message = "Success", response = String.class)
  public ResponseModel<String> uploadImageFile(@RequestParam("file") MultipartFile file) {
    String imageUrl = storageService.uploadImage(file).join();
    return new ResponseModel<>(imageUrl);
  }

  @PostMapping(
      value = "/document-upload",
      consumes = MULTIPART_FORM_DATA_VALUE,
      produces = APPLICATION_JSON_VALUE)
  @Operation(description = "Upload document file to storage bucket")
  @ApiResponse(code = 200, message = "Success", response = String.class)
  public ResponseModel<String> uploadDocument(@RequestParam("file") MultipartFile file) {
    String documentUrl = storageService.uploadDocument(file).join();
    return new ResponseModel<>(documentUrl);
  }

  @GetMapping(value = "/download", consumes = APPLICATION_JSON_VALUE)
  @Operation(description = "Download file from storage bucket")
  @ApiResponse(code = 200, message = "Success", response = Byte.class, responseContainer = "Array")
  public ResponseModel<byte[]> downloadFile(@RequestBody ImageDownloadRequest imageUrl) {
    byte[] fileContent = storageService.downloadFile(imageUrl).join();
    return new ResponseModel<>(fileContent);
  }
}
