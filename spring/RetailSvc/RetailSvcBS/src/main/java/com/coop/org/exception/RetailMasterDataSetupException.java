package com.coop.org.exception;

public class RetailMasterDataSetupException extends RetailException {

	public RetailMasterDataSetupException(){}
	public RetailMasterDataSetupException(String message) {
		this.message = message;
	}
	public RetailMasterDataSetupException(String message,String errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}