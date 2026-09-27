package com.academia.empleados.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class ManejadorErrores {

    // 404: se pidió un id que no existe
    @ExceptionHandler(EmpleadoNoEncontradoException.class)
    public ProblemDetail noEncontrado(EmpleadoNoEncontradoException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problema.setTitle("Empleado no encontrado");
        return problema;
    }

    // 409: el email ya lo tiene otro empleado
    @ExceptionHandler(EmailDuplicadoException.class)
    public ProblemDetail emailDuplicado(EmailDuplicadoException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problema.setTitle("Email duplicado");
        return problema;
    }

    // 400: los datos no pasan las validaciones de EmpleadoRequest (@NotBlank, @Email, ...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail datosInvalidos(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new TreeMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errores.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "Hay " + errores.size() + " campo(s) con errores");
        problema.setTitle("Datos inválidos");
        problema.setProperty("errores", errores);
        return problema;
    }

    // 400: el JSON no se puede leer (una coma de más, una fecha como 15/03/2024, ...)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail jsonIlegible(HttpMessageNotReadableException ex) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                "El cuerpo no es un JSON válido. Revisa comas y comillas, y que las fechas vayan como 2024-03-15");
        problema.setTitle("JSON ilegible");
        return problema;
    }
}