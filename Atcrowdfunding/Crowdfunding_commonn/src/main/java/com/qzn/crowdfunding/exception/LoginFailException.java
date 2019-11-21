package com.qzn.crowdfunding.exception;

/**
 * @author qzn
 * @create 2019/10/23 16:46
 */
public class LoginFailException extends RuntimeException {

    public LoginFailException(String message){
        super(message);
    }
}
