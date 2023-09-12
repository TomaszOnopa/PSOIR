package com.example.fuelreportservice.dto;

public record NewFuelReportRequest(Long carId, Double mileage, String type, String notes) {
}
