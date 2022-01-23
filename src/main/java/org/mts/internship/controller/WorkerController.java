package org.mts.internship.controller;

import lombok.RequiredArgsConstructor;
import org.mts.internship.dto.CreateWorkerRequest;
import org.mts.internship.dto.UpdateWorkerRequest;
import org.mts.internship.dto.WorkerDto;
import org.mts.internship.service.WorkerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/worker")
public class WorkerController {

    private final WorkerService service;

    @GetMapping
    public ResponseEntity<Set<WorkerDto>> getAllWorkers(){
        return new ResponseEntity(service.getAllWorkers(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<WorkerDto> getWorker(@PathVariable long id){
        return new ResponseEntity(service.getById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WorkerDto> createWorker(@RequestBody CreateWorkerRequest request){
        return new ResponseEntity(service.createWorker(request), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<WorkerDto> updateWorker(@PathVariable long id, @RequestBody UpdateWorkerRequest request){
        return new ResponseEntity(service.updateWorker(id, request), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteWorker(@PathVariable long id){
        service.deleteWorker(id);
        return new ResponseEntity(HttpStatus.OK);
    }


}
