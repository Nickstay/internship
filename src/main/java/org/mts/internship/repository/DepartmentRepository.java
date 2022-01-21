package org.mts.internship.repository;

import org.mts.internship.entity.Department;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends CrudRepository<Department, Long> {

    Optional<Department> findByName(String departmentName);
}
