package com.dodlu.core.app.model.request.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.URL;

@Accessors(chain = true)
@NoArgsConstructor
@Data
public class ImageDownloadRequest {
  @URL(message = "Invalid image url")
  private String imageUrl;
}
