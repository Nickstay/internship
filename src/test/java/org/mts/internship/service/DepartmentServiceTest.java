package org.mts.internship.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DepartmentServiceTest {

    private WorkerRepository workerRepository = Mockito.mock(WorkerRepository.class);
    private DepartmentRepository departmentRepository = Mockito.mock(DepartmentRepository.class);
    private WorkerMapper workerMapper = Mockito.mock(WorkerMapper.class);
    private DepartmentMapper departmentMapper = Mockito.mock(DepartmentMapper.class);

    private final DepartmentService service = new DepartmentService(
            workerRepository, departmentRepository, workerMapper, departmentMapper);

    @Test
    void transferShouldThrowWorkerNotFoundException() {
        when(workerRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(WorkerNotFoundException.class,
                () -> service.transfer(new TransferTicket().setWorkerId(1)));

        verify(workerRepository).findById(any());
        verify(workerRepository, never()).save(any());
        verifyNoInteractions(departmentRepository);
    }

    @Test
    void transferShouldThrowDepartmentNotFoundException() {
        String name = "Name";

        when(workerRepository.findById(any())).thenReturn(Optional.of(new Worker()));
        when(departmentRepository.findByName(any())).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class,
                () -> service.transfer(new TransferTicket().setDestinationDepartmentName(name)));

        verify(workerRepository).findById(any());
        verify(departmentRepository).findByName(any());
        verify(workerRepository, never()).save(any());
        verifyNoInteractions(workerMapper);
    }

    @Test
    void transferShouldThrowDestinationException() {
        Department department = new Department().setId(3).setName("IT");
        Worker worker = new Worker().setDepartment(department).setId(1);
        TransferTicket ticket = new TransferTicket().setWorkerId(1).setDestinationDepartmentName("IT");

        when(workerRepository.findById(any())).thenReturn(Optional.ofNullable(worker));
        when(departmentRepository.findByName(any())).thenReturn(Optional.ofNullable(department));

        assertThrows(DestinationException.class,
                () -> service.transfer(ticket));

        verify(workerRepository).findById(any());
        verify(departmentRepository).findByName(any());
        verify(workerRepository, never()).save(any());
        verifyNoInteractions(workerMapper);
    }

    @Test
    void transferShouldSucceed() {
        long id = 1;
        String destinationName = "IT";
        Department destinationDepartment = new Department().setId(3).setName(destinationName);
        Worker testWorker = new Worker()
                .setId(id)
                .setDepartment(new Department().setId(1).setName("Quality"));
        TransferTicket ticket = new TransferTicket()
                .setWorkerId(id).setDestinationDepartmentName(destinationName);
        WorkerDto dto = new WorkerDto()
                .setId(id)
                .setDepartmentName(destinationName);

        when(workerRepository.findById(id)).thenReturn(Optional.ofNullable(testWorker));
        when(departmentRepository.findByName(destinationName)).thenReturn(
                Optional.ofNullable(destinationDepartment));
        when(workerRepository.save(testWorker)).thenReturn(testWorker);
        when(workerMapper.mapToDto(testWorker)).thenReturn(dto);

        service.transfer(ticket);

        ArgumentCaptor<Worker> captor = ArgumentCaptor.forClass(Worker.class);
        verify(workerRepository).save(captor.capture());
        assertEquals(captor.getValue().getDepartment().getName(), destinationName);

        verify(workerRepository).findById(id);
        verify(departmentRepository).findByName(destinationName);
        verify(workerRepository).save(any());
        verify(workerMapper).mapToDto(any());


    }

    @Test
    void getByDepartmentShouldThrowDepartmentNotFound() {
        when(departmentRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThrows(DepartmentNotFoundException.class, () -> service.getByDepartment(anyString()));
    }

    @Test
    void getByDepartmentShouldSucceed() {
        String name = "IT";
        Department department = new Department().setName(name);
        department.setWorkers(new ArrayList<Worker>());

        when(departmentRepository.findByName(name)).thenReturn(Optional.of(department));

        service.getByDepartment(name);

        verify(departmentRepository).findByName(name);
    }

    @Test
    void getAllDepartmentsShouldSucceed() {
        List<Department> departmentList = new ArrayList<>();
        departmentList.add(new Department().setId(1).setName("IT"));
        DepartmentDto dto = new DepartmentDto().setId(1).setName("IT");

        when(departmentRepository.findAll()).thenReturn(departmentList);
        when(departmentMapper.mapToDto(any())).thenReturn(dto);

        List<DepartmentDto> response = service.getAll();

        assertEquals(response.get(0).getId(), departmentList.get(0).getId());

        verify(departmentRepository).findAll();
        verify(departmentMapper).mapToDto(any());
    }

    @Test
    void getAllWithNoDepartmentShouldSucceed() {
        List<Department> departmentList = new ArrayList<>();

        when(departmentRepository.findAll()).thenReturn(departmentList);

        List<DepartmentDto> response = service.getAll();

        assertEquals(departmentList, response);

        verify(departmentRepository).findAll();
        verifyNoInteractions(departmentMapper);

    }
}