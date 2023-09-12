package com.example.fuelreportservice.controller;

import com.example.fuelreportservice.dto.FuelReportResponse;
import com.example.fuelreportservice.dto.NewFuelReportRequest;
import com.example.fuelreportservice.service.FuelReportService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fuel")
@RequiredArgsConstructor
public class FuelReportController {
    private final FuelReportService fuelReportService;

    @GetMapping("list")
    public ResponseEntity<?> mileageReports(@RequestParam Long carId, @RequestParam(defaultValue = "1") int page) {
        try {
            JSONObject reports = fuelReportService.getFuelReports(carId, page);
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("avg")
    public ResponseEntity<?> reportsAvg(@RequestParam Long carId) {
        try {
            JSONObject avg = fuelReportService.getFuelAvg(carId);
            return ResponseEntity.ok(avg);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody NewFuelReportRequest request) {
        try {
            FuelReportResponse report = fuelReportService.addReport(request);
            if (report != null)
                return ResponseEntity.ok(report);
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
