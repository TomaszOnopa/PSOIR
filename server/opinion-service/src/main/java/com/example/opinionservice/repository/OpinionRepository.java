package com.example.opinionservice.repository;

import com.example.opinionservice.model.Opinion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OpinionRepository extends MongoRepository<Opinion, String> {
    Page<Opinion> findAllByCarIdOrderByCreationDateDesc(Long id, Pageable pageable);
    List<Opinion> findAllByCarId(Long id);
}
