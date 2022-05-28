package com.example.weatherapp.Models;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data

// Automatically Establishes Setters and Getters alongside Constructor of the Class Metric
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metric_generator") // Automatically increment new id after Generating new metric
    private Long metricid; // PK of Metric Table
    @Column(name="metricname")
    private String metricname; // Metric name ie Temperture
    @Column(name="metricvalue") // Value of Metric
    private double metricvalue;
    @Column(name="recorded")
    private Date recorded; // Date of issued Client Side Http request
    @ManyToOne(fetch = FetchType.EAGER, optional = false) // Create a Many Metrics to One Sensor relationship
    @JoinColumn(name = "sensorid", nullable = false)  // Join Tables On FK sensorid with PK id in Sensor Table
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Sensor sensor;

}
