package com.acs.wave.examples.jetty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private Long id;
    private String name;
    private Integer age;
}