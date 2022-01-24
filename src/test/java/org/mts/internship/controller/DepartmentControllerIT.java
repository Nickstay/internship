package org.mts.internship.controller;

import org.junit.jupiter.api.Test;
import org.mts.internship.dto.DepartmentDto;
import org.mts.internship.dto.ErrorResponse;
import org.mts.internship.dto.TransferTicket;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.entity.Department;
import org.mts.internship.entity.Worker;
import org.mts.internship.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DepartmentControllerIT extends AbstractIT {

    @Autowired
    protected DepartmentMapper departmentMapper;

    private String url = "/department";

    @Test
    void testTransferWorkerSuccessful() throws Exception {
        TransferTicket ticket = new TransferTicket();
        ticket.setWorkerId(1);
        ticket.setDestinationDepartmentName("IT");

//        Perform as expected
        String response =
                mvc.perform(MockMvcRequestBuilders.post(url.concat("/transfer")).contentType(MediaType.APPLICATION_JSON)
                        .content(super.mapToJson(ticket)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();
        WorkerDto workerDtoFromResponse = mapToWorkerDto(response);

//        Get worker from database
        WorkerDto workerDtoFromDB = workerMapper.mapToDto(em.find(Worker.class, (long) 1));

        assertAll(
                () -> assertEquals(workerDtoFromResponse.getId(), ticket.getWorkerId()),
                () -> assertEquals(workerDtoFromResponse.getDepartmentName(), ticket.getDestinationDepartmentName())
        );
        assertEquals(workerDtoFromDB, workerDtoFromResponse);
    }

    @Test
    void testTransferWorkerToHisDepartment() throws Exception {
        TransferTicket ticket = new TransferTicket();
        ticket.setWorkerId(1);
        ticket.setDestinationDepartmentName("Security");

        String response =
                mvc.perform(MockMvcRequestBuilders.post(url.concat("/transfer")).contentType(MediaType.APPLICATION_JSON)
                        .content(super.mapToJson(ticket)))
                        .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getMessage(), "Worker is already in department: Security"),
                () -> assertEquals(errorResponse.getStatus(), 422),
                () -> assertEquals(errorResponse.getCode(), "UNPROCESSABLE_ENTITY")
        );
    }

    @Test
    void testTransferInvalidWorkerId() throws Exception {
        TransferTicket ticket = new TransferTicket();
        ticket.setWorkerId(1000);
        ticket.setDestinationDepartmentName("HR");

        String response = mvc.perform(MockMvcRequestBuilders.post(url.concat("/transfer")).contentType(MediaType.APPLICATION_JSON)
                .content(super.mapToJson(ticket)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getMessage(), "Worker not found by id: 1000"),
                () -> assertEquals(errorResponse.getStatus(), 404),
                () -> assertEquals(errorResponse.getCode(), "NOT_FOUND")
        );
    }

    @Test
    void testTransferInvalidDepartmentName() throws Exception {
        TransferTicket ticket = new TransferTicket();
        ticket.setWorkerId(1);
        ticket.setDestinationDepartmentName("BULBA");

        String response = mvc.perform(MockMvcRequestBuilders.post(url.concat("/transfer")).contentType(MediaType.APPLICATION_JSON)
                .content(super.mapToJson(ticket)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getMessage(), "Department not found by name: BULBA"),
                () -> assertEquals(errorResponse.getStatus(), 404),
                () -> assertEquals(errorResponse.getCode(), "NOT_FOUND")
        );
    }

    @Test
    void testGetByExistingDepartment() throws Exception {
//        Quality Department exist there is 2 workers
        String response =
                mvc.perform(MockMvcRequestBuilders.get(url.concat("/Quality")))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();
        List<WorkerDto> workerDtoListFromResponse = mapToWorkerDtoList(response);

        WorkerDto workerDtoFromDB1 = workerMapper.mapToDto(em.find(Worker.class, (long) 2));
        WorkerDto workerDtoFromDB2 = workerMapper.mapToDto(em.find(Worker.class, (long) 3));

        assertAll(
                () -> assertEquals(workerDtoListFromResponse.get(0), workerDtoFromDB1),
                () -> assertEquals(workerDtoListFromResponse.get(1), workerDtoFromDB2),
                () -> assertEquals(workerDtoListFromResponse.size(), 2)
        );
    }

    @Test
    void testGetByNonExistingDepartment() throws Exception {
//        There is no such department
        String response = mvc.perform(MockMvcRequestBuilders.get(url.concat("/BULBA")))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getMessage(), "Department not found by name: BULBA"),
                () -> assertEquals(errorResponse.getStatus(), 404),
                () -> assertEquals(errorResponse.getCode(), "NOT_FOUND")
        );
    }

    @Test
    void testGetAllDepartments() throws Exception {
//        Check for internal errors
        String response = mvc.perform(MockMvcRequestBuilders.get(url))
                .andReturn().getResponse().getContentAsString();
        List<DepartmentDto> departmentDtoListFromResponse = Arrays.asList(objectMapper.readValue(response, DepartmentDto[].class));

//        Get list of departments from database
        List<Department> departmentListFromDB = em.createQuery("Select d from Department d").getResultList();
        List<DepartmentDto> departmentDtoListFromDB =
                departmentListFromDB.stream()
                        .map(departmentMapper::mapToDto)
                        .collect(Collectors.toList());

        assertEquals(departmentDtoListFromDB, departmentDtoListFromResponse);
    }
}