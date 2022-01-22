package org.mts.internship.service;

import lombok.RequiredArgsConstructor;
import org.mts.internship.dto.DepartmentDto;
import org.mts.internship.dto.TransferTicket;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.entity.Department;
import org.mts.internship.entity.Worker;
import org.mts.internship.exception.DepartmentNotFoundException;
import org.mts.internship.exception.DestinationException;
import org.mts.internship.exception.WorkerNotFoundException;
import org.mts.internship.mapper.DepartmentMapper;
import org.mts.internship.mapper.WorkerMapper;
import org.mts.internship.repository.DepartmentRepository;
import org.mts.internship.repository.WorkerRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final WorkerRepository workerRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public WorkerDto transfer(TransferTicket ticket) {
        Worker worker = workerRepository.findById(ticket.getWorkerId())
                .orElseThrow(() -> new WorkerNotFoundException(ticket.getWorkerId()));
        Department department = departmentRepository.findByName(ticket.getDestinationDepartmentName())
                .orElseThrow(() -> new DepartmentNotFoundException(ticket.getDestinationDepartmentName()));
        if (worker.getDepartment().getId() == department.getId()){
            throw new DestinationException(department.getName());
        }
        worker.setDepartment(department);
        workerRepository.save(worker);
        return WorkerMapper.mapToDto(worker);
    }

    public Set<WorkerDto> getByDepartment(String departmentName) {
        Department department = departmentRepository.findByName(departmentName)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentName));
        Set<Worker> workers = department.getWorkers();
        return workers.stream()
                .map(WorkerMapper::mapToDto)
                .collect(Collectors.toSet());
    }

    public Set<DepartmentDto> getAll() {
        List<Department> departments= (List<Department>) departmentRepository.findAll();
        return departments.stream()
                .map(DepartmentMapper::mapToDto)
                .collect(Collectors.toSet());
    }
}
