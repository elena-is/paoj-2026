package com.pao.laboratory03.exceptions;
// * 5. exception/StudentNotFoundException.java — EXCEPȚIE CUSTOM
//  *    - extends RuntimeException
//  *    - Constructor cu String message → super(message)

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }
}