package org.mts.internship.config;

import org.mts.internship.dto.ErrorResponse;
import org.mts.internship.exception.DepartmentNotFoundException;
import org.mts.internship.exception.DestinationException;
import org.mts.internship.exception.WorkerExistsByPhoneNumberException;
import org.mts.internship.exception.WorkerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DepartManagerExceptionHandler {

    @ExceptionHandler({DepartmentNotFoundException .class, WorkerNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex){
        return new ResponseEntity(new ErrorResponse()
                .setCode("NOT_FOUND")
                .setStatus(404)
                .setMessage(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DestinationException.class)
    public ResponseEntity<ErrorResponse> handleDestinationException(DestinationException ex){
        return new ResponseEntity(new ErrorResponse()
                .setCode("UNPROCESSABLE_ENTITY")
                .setStatus(422)
                .setMessage(ex.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(WorkerExistsByPhoneNumberException.class)
    public ResponseEntity<ErrorResponse> handleExistByPhoneNumberException(WorkerExistsByPhoneNumberException ex){
        return new ResponseEntity(new ErrorResponse()
                .setCode("CONFLICT")
                .setStatus(409)
                .setMessage(ex.getMessage()), HttpStatus.CONFLICT);
    }
}
