package com.example.fuelreportservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(value = "fuel-report")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FuelReport {
    @Id
    private String id;
    @Field(value = "creation_date")
    private Date creationDate;
    @Field(value = "car")
    private Long carId;
    private Double mileage;
    private String type;
    private String notes;
}
