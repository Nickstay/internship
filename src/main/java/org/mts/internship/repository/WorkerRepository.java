package org.mts.internship.repository;

import org.mts.internship.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {

    Optional<Worker> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
