package org.mts.internship.mapper;

import org.mts.internship.dto.WorkerDto;
import org.mts.internship.entity.Worker;

public class WorkerMapper {
    public static WorkerDto mapToDto(Worker worker) {
        return new WorkerDto()
                .setId(worker.getId())
                .setFirstName(worker.getFirstName())
                .setLastName(worker.getLastName())
                .setDepartmentName(worker.getDepartment().getName());
    }

}
