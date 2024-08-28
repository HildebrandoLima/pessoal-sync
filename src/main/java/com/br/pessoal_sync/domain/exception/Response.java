package com.br.pessoal_sync.domain.exception;

public class Response {
    private String message;
	private Object data;
	private String details;
	private int status;

	public Response(String message, Object data,  String details, int status) {
		this.message = message;
		this.data = data;
		this.details = details;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
