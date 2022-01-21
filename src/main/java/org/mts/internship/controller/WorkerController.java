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

    @GetMapping("{department}")
    public ResponseEntity<Set<WorkerDto>> getByDepartment(@RequestParam String department){
        return new ResponseEntity(service.getByDepartment(department), HttpStatus.FOUND);
    }

    @GetMapping
    public ResponseEntity<Set<WorkerDto>> getAllWorkers(){
        return new ResponseEntity(service.getAllWorkers(), HttpStatus.FOUND);
    }

    @GetMapping("{id}")
    public ResponseEntity<WorkerDto> getWorker(@RequestParam long id){
        return new ResponseEntity(service.getById(id), HttpStatus.FOUND);
    }

    @PutMapping
    public ResponseEntity<WorkerDto> createWorker(@RequestBody CreateWorkerRequest request){
        return new ResponseEntity(service.createWorker(request), HttpStatus.CREATED);
    }

    @PostMapping()
    public ResponseEntity<WorkerDto> updateWorker(@RequestBody UpdateWorkerRequest request){
        return new ResponseEntity(service.updateWorker(request), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<WorkerDto> deleteWorker(@RequestParam long id){
        return new ResponseEntity(service.deleteWorker(id), HttpStatus.OK);
    }


}
