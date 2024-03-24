package com.mballen.demoparkapi.exception;

public class PasswordInvalidException extends RuntimeException{

   public PasswordInvalidException(String message){
       super(message);
   }

}
