package org.mts.internship.controller;

import lombok.RequiredArgsConstructor;
import org.mts.internship.dto.TransferTicket;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.service.DepartmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping("transfer")
    public ResponseEntity<WorkerDto> transferWorker(@RequestBody TransferTicket ticket){
        return new ResponseEntity(service.transfer(ticket), HttpStatus.OK);
    }

}
