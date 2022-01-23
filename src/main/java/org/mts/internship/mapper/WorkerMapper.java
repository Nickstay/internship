package org.mts.internship.mapper;

import org.mts.internship.dto.WorkerDto;
import org.mts.internship.entity.Worker;
import org.springframework.stereotype.Component;

@Component
public class WorkerMapper {

    public WorkerDto mapToDto(Worker worker) {
        return new WorkerDto()
                .setId(worker.getId())
                .setFirstName(worker.getFirstName())
                .setLastName(worker.getLastName())
                .setDepartmentName(worker.getDepartment().getName());
    }

}
