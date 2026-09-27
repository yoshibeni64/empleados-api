package com.academia.empleados.service;

import com.academia.empleados.dto.EmpleadoRequest;
import com.academia.empleados.dto.EmpleadoResponse;
import com.academia.empleados.entity.Empleado;
import com.academia.empleados.exception.EmailDuplicadoException;
import com.academia.empleados.exception.EmpleadoNoEncontradoException;
import com.academia.empleados.repository.EmpleadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmpleadoService {

    private final EmpleadoRepository repository;

    // Inyección por constructor: Spring pasa el repositorio al crear el servicio
    public EmpleadoService(EmpleadoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listar() {
        return repository.findAll().stream()
                .map(EmpleadoResponse::desde)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpleadoResponse buscarPorId(Long id) {
        return EmpleadoResponse.desde(obtener(id));
    }

    public EmpleadoResponse crear(EmpleadoRequest datos) {
        if (repository.existsByEmail(datos.email())) {
            throw new EmailDuplicadoException(datos.email());
        }
        Empleado empleado = new Empleado(datos.nombre(), datos.apellidos(), datos.email(), datos.puesto(),
                datos.departamento(), datos.salario(), datos.fechaIngreso());
        empleado.setActivo(datos.activo() == null || datos.activo());
        return EmpleadoResponse.desde(repository.save(empleado));
    }

    public EmpleadoResponse actualizar(Long id, EmpleadoRequest datos) {
        Empleado empleado = obtener(id);
        if (repository.existsByEmailAndIdNot(datos.email(), id)) {
            throw new EmailDuplicadoException(datos.email());
        }
        empleado.setNombre(datos.nombre());
        empleado.setApellidos(datos.apellidos());
        empleado.setEmail(datos.email());
        empleado.setPuesto(datos.puesto());
        empleado.setDepartamento(datos.departamento());
        empleado.setSalario(datos.salario());
        empleado.setFechaIngreso(datos.fechaIngreso());
        empleado.setActivo(datos.activo() == null || datos.activo());
        return EmpleadoResponse.desde(repository.save(empleado));
    }

    public void eliminar(Long id) {
        repository.delete(obtener(id));
    }

    // Busca el empleado o lanza la excepción que el manejador convierte en 404
    private Empleado obtener(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EmpleadoNoEncontradoException(id));
    }
}