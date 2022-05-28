package com.example.weatherapp.Repo;

import com.example.weatherapp.Models.Metric;
import com.example.weatherapp.Models.Sensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface SensorRepo extends JpaRepository<Sensor,Long> {
    List<Sensor> findBycountry(String name);    // Finds Sensors By country

    Page<Sensor> findAll(Pageable pageable); // Finds all Sensors with Parameter of a Pageable object to limit the results
    List<Sensor> findAllBycountryIgnoreCase(String name);

    @Transactional
    void deleteById(Long id);
}
