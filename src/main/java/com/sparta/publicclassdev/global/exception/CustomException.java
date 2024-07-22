package com.sparta.publicclassdev.global.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "CustomException:: ")
public class CustomException extends RuntimeException{
	private ErrorCode errorCode;

	public CustomException(ErrorCode errorCode){
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		log.info("ExceptionMethod: {}", getExceptionMethod());
		log.info("ErrorCode: {}", errorCode.getMessage());
	}
	public String getExceptionMethod(){
		String className = Thread.currentThread().getStackTrace()[3].getClassName();
		String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
		return className + "." +methodName;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}
}