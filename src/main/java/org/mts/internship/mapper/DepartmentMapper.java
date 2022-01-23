package org.mts.internship.mapper;

import org.mts.internship.dto.DepartmentDto;
import org.mts.internship.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {
    public DepartmentDto mapToDto(Department department) {
        return new DepartmentDto()
                .setId(department.getId())
                .setName(department.getName());
    }
}
