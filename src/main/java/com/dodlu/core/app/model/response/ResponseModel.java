package com.dodlu.core.app.model.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseModel<T> {
  private T data;
  private String message;
  private List<String> errors;
  private int status;

  public ResponseModel(T data) {
    this.data = data;
    this.status = HttpStatus.OK.value();
    this.message = "Request successful";
  }
}
