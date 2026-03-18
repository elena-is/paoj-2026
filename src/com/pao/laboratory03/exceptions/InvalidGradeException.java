package com.pao.laboratory03.exceptions;

// * 4. exception/InvalidGradeException.java — EXCEPȚIE CUSTOM
// *    - extends RuntimeException
// *    - Constructor cu String message → super(message)

public class InvalidGradeException extends RuntimeException {

    public InvalidGradeException(String message) {
        super(message);
    }
}