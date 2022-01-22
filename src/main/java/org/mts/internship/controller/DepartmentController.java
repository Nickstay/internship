package org.mts.internship.controller;

import lombok.RequiredArgsConstructor;
import org.mts.internship.dto.DepartmentDto;
import org.mts.internship.dto.TransferTicket;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping("transfer")
    public ResponseEntity<WorkerDto> transferWorker(@RequestBody TransferTicket ticket){
        return new ResponseEntity(service.transfer(ticket), HttpStatus.OK);
    }

    @GetMapping("{department}")
    public ResponseEntity<Set<WorkerDto>> getByDepartment(@PathVariable String department){
        return new ResponseEntity(service.getByDepartment(department), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<Set<DepartmentDto>> getAllDepartments(){
        return new ResponseEntity(service.getAll(), HttpStatus.FOUND);
    }

}
