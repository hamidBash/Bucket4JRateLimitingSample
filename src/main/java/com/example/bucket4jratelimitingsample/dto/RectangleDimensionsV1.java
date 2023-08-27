package com.example.bucket4jratelimitingsample.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class RectangleDimensionsV1 {
    private String name;
    private Double length;
    private Double width;
}
