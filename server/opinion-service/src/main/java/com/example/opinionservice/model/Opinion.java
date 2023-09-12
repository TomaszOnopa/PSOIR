package com.example.opinionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(value = "opinion")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Opinion {
    @Id
    private String id;
    @Field(value = "car")
    private Long carId;
    @Field(value = "creation_date")
    private Date creationDate;
    private Double rating;
    private String comment;
}
