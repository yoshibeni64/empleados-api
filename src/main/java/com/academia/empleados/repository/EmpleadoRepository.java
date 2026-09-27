package com.academia.empleados.repository;

import com.academia.empleados.entity.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    // Consultas DERIVADAS: Spring Data arma el SQL a partir del nombre del método
    // ... WHERE LOWER(departamento) = LOWER(?) ORDER BY apellidos ASC
    List<Empleado> findByDepartamentoIgnoreCaseOrderByApellidosAsc(String departamento);

    // ... WHERE salario BETWEEN ? AND ? ORDER BY salario DESC
    List<Empleado> findBySalarioBetweenOrderBySalarioDesc(BigDecimal minimo, BigDecimal maximo);

    // Consulta JPQL escrita a mano: cada filtro es OPCIONAL (si llega null, no filtra)
    @Query("""
            SELECT e FROM Empleado e
            WHERE (:departamento IS NULL OR LOWER(e.departamento) = LOWER(:departamento))
              AND (:texto IS NULL OR LOWER(CONCAT(e.nombre, ' ', e.apellidos)) LIKE LOWER(CONCAT('%', :texto, '%')))
              AND (:activo IS NULL OR e.activo = :activo)
              AND (:salarioMinimo IS NULL OR e.salario >= :salarioMinimo)
              AND (:salarioMaximo IS NULL OR e.salario <= :salarioMaximo)
            """)
    Page<Empleado> buscar(@Param("departamento") String departamento,
                          @Param("texto") String texto,
                          @Param("activo") Boolean activo,
                          @Param("salarioMinimo") BigDecimal salarioMinimo,
                          @Param("salarioMaximo") BigDecimal salarioMaximo,
                          Pageable pageable);
}