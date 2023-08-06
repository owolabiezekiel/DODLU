package com.dodlu.core.app.exceptions;

import org.springframework.stereotype.Service;

@Service
public class CoreExceptionService extends ExceptionUtil<CoreException> {
  @Override
  public CoreException createCustomException(String s, Throwable throwable, int i) {
    return new CoreException(i, s, throwable);
  }

  @Override
  public Class<CoreException> customExceptionClass() {
    return CoreException.class;
  }
}
