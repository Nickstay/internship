package org.mts.internship.service;

import lombok.RequiredArgsConstructor;
import org.mts.internship.dto.CreateWorkerRequest;
import org.mts.internship.dto.UpdateWorkerRequest;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.entity.Department;
import org.mts.internship.entity.Worker;
import org.mts.internship.exception.DepartmentNotFoundException;
import org.mts.internship.exception.WorkerExistsByPhoneNumberException;
import org.mts.internship.exception.WorkerNotFoundException;
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
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final DepartmentRepository departmentRepository;
    private final WorkerMapper workerMapper;

    public Set<WorkerDto> getAllWorkers() {
        List<Worker> workers = (List<Worker>) workerRepository.findAll();
        return workers.stream()
                .map(workerMapper::mapToDto)
                .collect(Collectors.toSet());
    }

    public WorkerDto getById(long id) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));
        return workerMapper.mapToDto(worker);
    }

    @Transactional
    public WorkerDto createWorker(CreateWorkerRequest request) {
        Department department = departmentRepository.findByName(request.getDepartmentName())
                .orElseThrow(() -> new DepartmentNotFoundException(request.getDepartmentName()));
        if(workerRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new WorkerExistsByPhoneNumberException(request.getPhoneNumber());
        }
        Worker worker = new Worker()
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setPhoneNumber(request.getPhoneNumber())
                .setDepartment(department);

        workerRepository.save(worker);
        return workerMapper.mapToDto(worker);
    }

    @Transactional
    public WorkerDto updateWorker(long id, UpdateWorkerRequest request) {
        Worker worker = workerRepository.findById(id)
                .orElseThrow(() -> new WorkerNotFoundException(id));

        if (request.getFirstName() != null) worker.setFirstName(request.getFirstName());
        if (request.getLastName() != null) worker.setLastName(request.getLastName());
        if (request.getPhoneNumber() != null) worker.setPhoneNumber(request.getPhoneNumber());

        workerRepository.save(worker);
        return workerMapper.mapToDto(worker);
    }

    @Transactional
    public void deleteWorker(long id) {
        if(!workerRepository.existsById(id)){
            throw new WorkerNotFoundException(id);
        }
        workerRepository.deleteById(id);
    }
}
