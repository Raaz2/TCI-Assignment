package com.tci.tciassignment.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String empName;

    private Integer amount;

    private String currency;

    @JsonIgnore
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDate joiningDate;

    @JsonIgnore
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDate exitDate;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Department department;

    @Transient
    @JsonProperty("department")
    private String departmentName;

    @Transient
    @JsonProperty("joiningDate")
    private String dateJoined;

    @Transient
    @JsonProperty("exitDate")
    private String dateExit;
}

