package com.example.fuelreportservice.dto;

import java.sql.Date;

public record FuelReportResponse(String id, Date creationDate, Long carId, Double mileage, String type, String notes) {
}
