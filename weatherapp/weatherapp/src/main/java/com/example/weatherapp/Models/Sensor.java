package com.example.weatherapp.Models;

import lombok.*;
import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.processing.Generated;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Data
// Automatically Establishes Setters and Getters alongside Constructor of the Class Metric
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sensor")
@Builder
public class Sensor {
    @Id
    @Column(name = "id")
    private long id;  // PK of Sensor Table
    @Column(name="country")
    private String country; // Sensors Country location and city
    @Column(name="city")
    private String city;


}
