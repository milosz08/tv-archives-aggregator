package pl.miloszgilga.archiver.scrapper.gui;

public class InoperableException extends RuntimeException {
  public InoperableException(Throwable cause) {
    super(cause);
  }

  public InoperableException(String message) {
    super(message);
  }
}
