package com.br.pessoal_sync.domain.exception;

import java.util.List;

public class Response {
    private String message;
	private List<Object> data;
	private String details;
	private int status;

	public Response(String message, List<Object> data,  String details, int status) {
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

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
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
