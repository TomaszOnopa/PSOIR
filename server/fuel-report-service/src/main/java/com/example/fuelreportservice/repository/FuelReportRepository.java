package com.example.fuelreportservice.repository;

import com.example.fuelreportservice.model.FuelReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FuelReportRepository extends MongoRepository<FuelReport, String> {
    Page<FuelReport> findAllByCarIdOrderByCreationDateDesc(Long car, Pageable pageable);
    List<FuelReport> findAllByCarId(Long car);
}
