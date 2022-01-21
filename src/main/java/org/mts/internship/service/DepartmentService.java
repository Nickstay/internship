package org.mts.internship.service;

import lombok.RequiredArgsConstructor;
import org.mts.internship.dto.TransferTicket;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.entity.Department;
import org.mts.internship.entity.Worker;
import org.mts.internship.exception.DepartmentNotFoundException;
import org.mts.internship.exception.DestinationException;
import org.mts.internship.exception.WorkerNotFoundException;
import org.mts.internship.mapper.WorkerMapper;
import org.mts.internship.repository.DepartmentRepository;
import org.mts.internship.repository.WorkerRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final WorkerRepository workerRepository;
    private final DepartmentRepository departmentRepository;

    public WorkerDto transfer(TransferTicket ticket) {
        Worker worker = workerRepository.findById(ticket.getWorkerId())
                .orElseThrow(() -> new WorkerNotFoundException(ticket.getWorkerId()));
        Department department = departmentRepository.findByName(ticket.getDestinationDepartmentName())
                .orElseThrow(() -> new DepartmentNotFoundException(ticket.getDestinationDepartmentName()));
        if (worker.getDepartment().getId() == department.getId()){
            throw new DestinationException(department.getId());
        }
        worker.setDepartment(department);
        workerRepository.save(worker);
        return WorkerMapper.mapToDto(worker);
    }
}
