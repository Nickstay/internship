package org.mts.internship.mapper;

import org.mts.internship.dto.DepartmentDto;
import org.mts.internship.entity.Department;

public class DepartmentMapper {
    public static DepartmentDto mapToDto(Department department) {
        return new DepartmentDto()
                .setId(department.getId())
                .setName(department.getName());
    }
}
