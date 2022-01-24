package org.mts.internship.controller;

import org.junit.jupiter.api.Test;
import org.mts.internship.dto.CreateWorkerRequest;
import org.mts.internship.dto.ErrorResponse;
import org.mts.internship.dto.UpdateWorkerRequest;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.entity.Worker;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WorkerControllerIT extends AbstractIT {

    private String url = "/worker";

    @Test
    void testGetAllWorkers() throws Exception {
        String response = mvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<WorkerDto> workerDtoListFromResponse = mapToWorkerDtoList(response);

        List<Worker> workerListFromDB = em.createQuery("Select w from Worker w").getResultList();
        List<WorkerDto> workerDtoListFromDB = workerListFromDB
                .stream()
                .map(workerMapper::mapToDto)
                .collect(Collectors.toList());

        assertEquals(workerDtoListFromDB, workerDtoListFromResponse);
    }

    @Test
    void testGetByExistingId() throws Exception {
        String response = mvc.perform(MockMvcRequestBuilders.get(url.concat("/1")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        WorkerDto workerDtoFromResponse = mapToWorkerDto(response);

        WorkerDto workerDtoFromDB = workerMapper.mapToDto(em.find(Worker.class, (long) 1));

        assertEquals(workerDtoFromDB, workerDtoFromResponse);
    }

    @Test
    void testGetByNonExistingId() throws Exception {
        String response = mvc.perform(MockMvcRequestBuilders.get(url.concat("/1000")))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getCode(), "NOT_FOUND"),
                () -> assertEquals(errorResponse.getStatus(), 404),
                () -> assertEquals(errorResponse.getMessage(), "Worker not found by id: 1000")
        );
    }

    @Test
    void testCreateSuccessful() throws Exception {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setFirstName("Alberto");
        request.setLastName("Franchesco");
        request.setPhoneNumber("87368193620");
        request.setDepartmentName("Marketing");

        String response = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        WorkerDto workerDtoFromResponse = mapToWorkerDto(response);

        long id = workerDtoFromResponse.getId();
        WorkerDto workerDtoFromDB = workerMapper.mapToDto(em.find(Worker.class, id));

        assertAll(
                () -> assertEquals(workerDtoFromResponse.getFirstName(), request.getFirstName()),
                () -> assertEquals(workerDtoFromResponse.getLastName(), request.getLastName()),
                () -> assertEquals(workerDtoFromResponse.getDepartmentName(), request.getDepartmentName())
        );

        assertEquals(workerDtoFromDB, workerDtoFromResponse);
    }

    @Test
    void testCreateByEmptyFirstNameRequest() throws Exception {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setLastName("Franchesco");
        request.setPhoneNumber("87368193620");
        request.setDepartmentName("Marketing");

        String response = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 422),
                () -> assertEquals(errorResponse.getCode(), "UNPROCESSABLE_ENTITY"),
                () -> assertEquals(errorResponse.getMessage(), "Validation failed.")
        );

    }

    @Test
    void testCreateByEmptyLastNameRequest() throws Exception {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setFirstName("Alberto");
        request.setPhoneNumber("87368193620");
        request.setDepartmentName("Marketing");

        String response = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 422),
                () -> assertEquals(errorResponse.getCode(), "UNPROCESSABLE_ENTITY"),
                () -> assertEquals(errorResponse.getMessage(), "Validation failed.")
        );

    }

    @Test
    void testCreateByEmptyPhoneNumberRequest() throws Exception {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setFirstName("Alberto");
        request.setLastName("Franchesco");
        request.setDepartmentName("Marketing");

        String response = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 422),
                () -> assertEquals(errorResponse.getCode(), "UNPROCESSABLE_ENTITY"),
                () -> assertEquals(errorResponse.getMessage(), "Validation failed.")
        );

    }

    @Test
    void testCreateByEmptyDepartmentNameRequest() throws Exception {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setFirstName("Alberto");
        request.setLastName("Franchesco");
        request.setPhoneNumber("87368193620");

        String response = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 422),
                () -> assertEquals(errorResponse.getCode(), "UNPROCESSABLE_ENTITY"),
                () -> assertEquals(errorResponse.getMessage(), "Validation failed.")
        );

    }

    @Test
    void testCreateByNonExistingDepartment() throws Exception {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setFirstName("Alberto");
        request.setLastName("Franchesco");
        request.setPhoneNumber("87368193620");
        request.setDepartmentName("BULBA");     //This Department doesn't exist

        String response = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 404),
                () -> assertEquals(errorResponse.getCode(), "NOT_FOUND"),
                () -> assertEquals(errorResponse.getMessage(), "Department not found by name: BULBA")
        );
    }

    @Test
    void testCreateByExistingPhoneNumber() throws Exception {
        CreateWorkerRequest request = new CreateWorkerRequest();
        request.setFirstName("Alberto");
        request.setLastName("Franchesco");
        request.setPhoneNumber("87003534131");  //This phone number is already in DB
        request.setDepartmentName("Marketing");

        String response = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 409),
                () -> assertEquals(errorResponse.getCode(), "CONFLICT"),
                () -> assertEquals(errorResponse.getMessage(), "Worker already registered by phone number: 87003534131")
        );

    }

    @Test
    void testUpdateWithEmptyRequest() throws Exception {
        UpdateWorkerRequest request = new UpdateWorkerRequest();

        String response = mvc.perform(MockMvcRequestBuilders.put(url.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        WorkerDto workerDtoFromResponse = mapToWorkerDto(response);

        WorkerDto workerDtoFromDBBeforeUpdate = workerMapper.mapToDto(em.find(Worker.class, (long) 1));

//        No changes with empty request
        assertEquals(workerDtoFromDBBeforeUpdate, workerDtoFromResponse);
    }

    @Test
    void testUpdateSuccessful() throws Exception {
        Worker workerFromDBBeforeUpdate = em.find(Worker.class, (long) 1);
        UpdateWorkerRequest request = new UpdateWorkerRequest();
        request.setFirstName("ChangedName");
        request.setLastName("ChangedSurname");
        request.setPhoneNumber("89000233340");

        String response = mvc.perform(MockMvcRequestBuilders.put(url.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        WorkerDto workerDtoFromResponse = mapToWorkerDto(response);

        Worker workerFromDBAfterUpdate = em.find(Worker.class, (long) 1);

        assertEquals(workerMapper.mapToDto(workerFromDBAfterUpdate), workerDtoFromResponse);

//        Department cannot be changed with this request, but all other parameters have been changed
        assertAll(
                () -> assertEquals(workerFromDBBeforeUpdate.getDepartment().getId(),
                        workerFromDBAfterUpdate.getDepartment().getId()),
                () -> assertNotEquals(workerFromDBBeforeUpdate.getFirstName(),
                        workerFromDBAfterUpdate.getFirstName()),
                () -> assertNotEquals(workerFromDBBeforeUpdate.getLastName(),
                        workerFromDBAfterUpdate.getLastName()),
                () -> assertNotEquals(workerFromDBBeforeUpdate.getPhoneNumber(),
                        workerFromDBAfterUpdate.getPhoneNumber())
        );
    }

    @Test
    void testUpdateByExistingPhoneNumber() throws Exception {
        UpdateWorkerRequest request = new UpdateWorkerRequest();
        request.setPhoneNumber("87003534131");

        String response = mvc.perform(MockMvcRequestBuilders.put(url.concat("/1"))
                .contentType(MediaType.APPLICATION_JSON).content(mapToJson(request)))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        Worker workerFromDBAfterUpdate = em.find(Worker.class, (long) 1);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 409),
                () -> assertEquals(errorResponse.getCode(), "CONFLICT"),
                () -> assertEquals(errorResponse.getMessage(), "Worker already registered by phone number: 87003534131")
        );
    }

    @Test
    void deleteSuccessful() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(url.concat("/1")))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByNonExistingId() throws Exception {
        String response = mvc.perform(MockMvcRequestBuilders.delete(url.concat("/1000")))
                .andReturn().getResponse().getContentAsString();
        ErrorResponse errorResponse = mapToErrorResponse(response);

        assertAll(
                () -> assertEquals(errorResponse.getStatus(), 404),
                () -> assertEquals(errorResponse.getCode(), "NOT_FOUND"),
                () -> assertEquals(errorResponse.getMessage(), "Worker not found by id: 1000")
        );
    }
}