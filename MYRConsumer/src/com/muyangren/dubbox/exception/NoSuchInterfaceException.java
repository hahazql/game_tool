package com.muyangren.dubbox.exception;

public class NoSuchInterfaceException extends Exception
{
	public NoSuchInterfaceException(){};
	
	public NoSuchInterfaceException(String msg) {super(msg);}
	
	public NoSuchInterfaceException(String message, Throwable cause) { super(message,cause);} 
	
	public NoSuchInterfaceException(Throwable cause) {super(cause);}
}
