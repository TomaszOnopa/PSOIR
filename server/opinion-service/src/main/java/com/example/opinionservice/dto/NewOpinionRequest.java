package com.example.opinionservice.dto;

public record NewOpinionRequest(Long carId, Double rating, String comment) {
}
