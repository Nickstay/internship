package org.mts.internship.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mts.internship.dto.ErrorResponse;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.mapper.WorkerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;

//Client requests mock
@AutoConfigureMockMvc
@Sql(value = "/migration/Init_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/migration/Drop_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//Default is WebEnvironment.MOCK. This configuration won't start Tomcat - servlet container
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Testcontainer is described in application.yml
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//Dependency Injection without using @Autowired. Just better practice
public abstract class AbstractIT {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected WorkerMapper workerMapper;
    @Autowired
    protected EntityManager em;

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }

    protected WorkerDto mapToWorkerDto(String source) throws JsonProcessingException {
        return objectMapper.readValue(source, WorkerDto.class);
    }

    protected List<WorkerDto> mapToWorkerDtoList(String source) throws JsonProcessingException {
        return Arrays.asList(objectMapper.readValue(source, WorkerDto[].class));
    }

    protected ErrorResponse mapToErrorResponse(String source) throws JsonProcessingException {
        return objectMapper.readValue(source, ErrorResponse.class);
    }

}
