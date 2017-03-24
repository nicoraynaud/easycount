package org.tekinico.easycount.service.exceptions;

/**
 * Created by Nico on 24/03/2017.
 */
public class ImportException extends RuntimeException {

    public ImportException() {
        super();
    }

    public ImportException(String message) {
        super(message);
    }

    public ImportException(String message, Throwable cause) {
        super(message, cause);
    }
}
