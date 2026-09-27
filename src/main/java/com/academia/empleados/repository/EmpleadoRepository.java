package com.academia.empleados.repository;

import com.academia.empleados.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    // ¿Existe algún empleado con este email?  → SELECT ... WHERE email = ?
    boolean existsByEmail(String email);

    // ¿Existe OTRO empleado (id distinto) con este email?  → WHERE email = ? AND id <> ?
    boolean existsByEmailAndIdNot(String email, Long id);
}