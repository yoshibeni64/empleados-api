package com.academia.empleados.exception;

public class EmailDuplicadoException extends RuntimeException {

    public EmailDuplicadoException(String email) {
        super("Ya existe un empleado con el email " + email);
    }
}