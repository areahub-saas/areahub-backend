package br.com.areahub.app.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(){
        super("Bad Request");
    }
    public BusinessException(String message){
        super(message);}
}