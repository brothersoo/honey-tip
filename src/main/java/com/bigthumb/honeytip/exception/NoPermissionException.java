package com.bigthumb.honeytip.exception;

public class NoPermissionException extends RuntimeException {

  public NoPermissionException() {
    super();
  }

  public NoPermissionException(String message) {
    super(message);
  }
}
