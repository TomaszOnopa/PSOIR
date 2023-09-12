package com.example.opinionservice.dto;

import java.sql.Date;

public record OpinionResponse(String id, Long carId, Date creationDate, Double rating, String comment) {
}
