package com.br.pessoal_sync.exception;

public class NotFoundException extends RuntimeException {

	public NotFoundException() {
        super();
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
