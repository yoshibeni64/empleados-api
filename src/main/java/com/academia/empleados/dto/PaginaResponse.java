package com.academia.empleados.dto;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

// Una página de resultados: los elementos y los datos para pedir la siguiente
public record PaginaResponse<T>(
        List<T> contenido,
        int pagina,
        int tamano,
        long totalElementos,
        int totalPaginas,
        boolean primera,
        boolean ultima
) {

    public static <E, T> PaginaResponse<T> desde(Page<E> page, Function<E, T> convertir) {
        return new PaginaResponse<>(
                page.getContent().stream().map(convertir).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast());
    }
}