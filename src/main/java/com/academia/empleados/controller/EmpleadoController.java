package com.academia.empleados.controller;

import com.academia.empleados.dto.EmpleadoRequest;
import com.academia.empleados.dto.EmpleadoResponse;
import com.academia.empleados.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleados", description = "Alta, consulta, modificación y baja de empleados")
public class EmpleadoController {

    private final EmpleadoService service;

    public EmpleadoController(EmpleadoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los empleados")
    public List<EmpleadoResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un empleado por su id")
    @ApiResponse(responseCode = "200", description = "Empleado encontrado")
    @ApiResponse(responseCode = "404", description = "No existe un empleado con ese id",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public EmpleadoResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    @Operation(summary = "Crear un empleado")
    @ApiResponse(responseCode = "201", description = "Empleado creado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "El email ya existe",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public ResponseEntity<EmpleadoResponse> crear(@Valid @RequestBody EmpleadoRequest datos) {
        EmpleadoResponse creado = service.crear(datos);
        URI ubicacion = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(creado.id()).toUri();
        return ResponseEntity.created(ubicacion).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar un empleado")
    @ApiResponse(responseCode = "200", description = "Empleado modificado")
    @ApiResponse(responseCode = "400", description = "Datos inválidos",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "No existe un empleado con ese id",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "El email ya lo tiene otro empleado",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public EmpleadoResponse actualizar(@PathVariable Long id, @Valid @RequestBody EmpleadoRequest datos) {
        return service.actualizar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar un empleado")
    @ApiResponse(responseCode = "204", description = "Empleado eliminado")
    @ApiResponse(responseCode = "404", description = "No existe un empleado con ese id",
            content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}