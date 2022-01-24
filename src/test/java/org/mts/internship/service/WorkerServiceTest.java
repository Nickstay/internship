package org.mts.internship.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class WorkerServiceTest {

    private WorkerRepository workerRepository = Mockito.mock(WorkerRepository.class);
    private DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
    private WorkerMapper workerMapper = Mockito.mock(WorkerMapper.class);

    private final WorkerService service = new WorkerService(
            workerRepository, departmentRepository, workerMapper);

    @Test
    void getAllWithNoWorkerShouldSucceed() {
        List<Worker> workers = new ArrayList<>();

        when(workerRepository.findAll()).thenReturn(workers);

        List<WorkerDto> response = service.getAllWorkers();

        assertEquals(workers, response);

        verify(workerRepository).findAll();
        verifyNoInteractions(workerMapper);
    }

    @Test
    void getAllWorkersShouldSucceed() {
        List<Worker> workers = new ArrayList<>();
        Worker worker = new Worker().setId(1);
        workers.add(worker);

        WorkerDto dto = new WorkerDto().setId(1);

        when(workerRepository.findAll()).thenReturn(workers);
        when(workerMapper.mapToDto(worker)).thenReturn(dto);

        List<WorkerDto> response = service.getAllWorkers();

        assertEquals(response.get(0).getId(), worker.getId());

        verify(workerRepository).findAll();
        verify(workerMapper).mapToDto(worker);
    }

    @Test
    void getByIdShouldThrowWorkerNotFoundException() {
        long id = 1;

        when(workerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> service.getById(id));

        verify(workerRepository).findById(id);
        verifyNoInteractions(workerMapper);
    }

    @Test
    void getByIdShouldSucceed() {
        long id = 1;
        Worker worker = new Worker().setId(id);
        WorkerDto workerDto = new WorkerDto().setId(id);

        when(workerRepository.findById(id)).thenReturn(Optional.ofNullable(worker));
        when(workerMapper.mapToDto(worker)).thenReturn(workerDto);

        WorkerDto response = service.getById(id);

        assertEquals(workerDto, response);

        verify(workerRepository).findById(id);
        verify(workerMapper).mapToDto(worker);
    }

    @Test
    void createWorkerShouldThrowDepartmentNotFoundException() {
        String name = "Security";
        CreateWorkerRequest request = new CreateWorkerRequest().setDepartmentName(name);

        when(departmentRepository.findByName(name)).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> service.createWorker(request));

        verify(departmentRepository).findByName(name);
        verifyNoInteractions(workerRepository);
        verifyNoInteractions(workerMapper);
    }

    @Test
    void createWorkerShouldThrowWorkerExistsByPhoneNumberException() {
        String name = "Security";
        String number = "666";
        Department department = new Department().setName(name);
        CreateWorkerRequest request = new CreateWorkerRequest()
                .setDepartmentName(name)
                .setPhoneNumber(number);

        when(departmentRepository.findByName(name)).thenReturn(Optional.ofNullable(department));
        when(workerRepository.existsByPhoneNumber(number)).thenReturn(true);

        assertThrows(WorkerExistsByPhoneNumberException.class, () -> service.createWorker(request));

        verify(departmentRepository).findByName(name);
        verify(workerRepository, only()).existsByPhoneNumber(number);
        verifyNoInteractions(workerMapper);
    }

    @Test
    void createWorkerShouldSucceed() {
        long id = 5;
        String name = "Security";
        String number = "666";
        String firstName = "Ada";
        String lastName = "Ginger";
        Department department = new Department().setName(name).setId(1);
        CreateWorkerRequest request = new CreateWorkerRequest()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setDepartmentName(name)
                .setPhoneNumber(number);
        Worker worker = new Worker().setId(id)
                .setFirstName(firstName).setLastName(lastName).setDepartment(department)
                .setPhoneNumber(number);
        WorkerDto workerDto = new WorkerDto().setId(id)
                .setFirstName(firstName).setLastName(lastName).setDepartmentName(name);

        when(departmentRepository.findByName(name)).thenReturn(Optional.ofNullable(department));
        when(workerRepository.existsByPhoneNumber(number)).thenReturn(false);
        when(workerRepository.save(any())).thenReturn(worker);
        when(workerMapper.mapToDto(worker)).thenReturn(workerDto);

        WorkerDto response = service.createWorker(request);

        assertEquals(workerDto, response);

        verify(departmentRepository).findByName(name);
        verify(workerRepository).existsByPhoneNumber(number);
        verify(workerRepository).save(any());
        verify(workerMapper).mapToDto(worker);
    }

    @Test
    void updateWorkerShouldThrowWorkerNotFoundException() {
        long id = 1;
        UpdateWorkerRequest request = new UpdateWorkerRequest();

        when(workerRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class, () -> service.updateWorker(id, request));

        verify(workerRepository, only()).findById(id);
        verifyNoInteractions(workerMapper);
    }

    @Test
    void updateWorkerShouldThrowWorkerExistsByPhoneNumberException() {
        long id = 1;
        String phoneNumber = "82348235224";
        UpdateWorkerRequest request = new UpdateWorkerRequest()
                .setFirstName("Dennis")
                .setLastName("Petrov")
                .setPhoneNumber(phoneNumber);
        Worker worker = new Worker();

        when(workerRepository.findById(id)).thenReturn(Optional.of(worker));
        when(workerRepository.existsByPhoneNumber(phoneNumber)).thenReturn(true);

        assertThrows(WorkerExistsByPhoneNumberException.class, () -> service.updateWorker(id, request));

        verify(workerRepository).findById(id);
        verify(workerRepository).existsByPhoneNumber(phoneNumber);
        verifyNoMoreInteractions(workerRepository);
        verifyNoInteractions(workerMapper);
    }

    @Test
    void updateWorkerShouldSucceed() {
        long id = 1;
        String firstName = "Dennis";
        String lastName = "Petrov";
        String phoneNumber = "82348235224";
        UpdateWorkerRequest request = new UpdateWorkerRequest()
                .setFirstName(firstName)
                .setLastName(lastName)
                .setPhoneNumber(phoneNumber);
        Worker worker = new Worker().setId(id);
        WorkerDto workerDto = new WorkerDto()
                .setId(id)
                .setFirstName(firstName)
                .setLastName(lastName);

        when(workerRepository.findById(id)).thenReturn(Optional.of(worker));
        when(workerRepository.existsByPhoneNumber(phoneNumber)).thenReturn(false);
        when(workerRepository.save(worker)).thenReturn(worker);
        when(workerMapper.mapToDto(worker)).thenReturn(workerDto);

        WorkerDto response = service.updateWorker(id, request);

        assertEquals(response, workerDto);

        verify(workerRepository).findById(id);
        verify(workerRepository).existsByPhoneNumber(phoneNumber);
        verify(workerRepository).save(worker);
        verify(workerMapper).mapToDto(worker);
    }

    @Test
    void deleteWorkerShouldThrowWorkerNotFoundException() {
        long id = 30;

        when(workerRepository.existsById(id)).thenReturn(false);

        assertThrows(WorkerNotFoundException.class, () -> service.deleteWorker(id));

        verify(workerRepository, only()).existsById(id);
    }

    @Test
    void deleteWorkerShouldSucceed() {
        long id = 30;

        when(workerRepository.existsById(id)).thenReturn(true);

        service.deleteWorker(id);

        verify(workerRepository).existsById(id);
        verify(workerRepository).deleteById(id);
    }
}