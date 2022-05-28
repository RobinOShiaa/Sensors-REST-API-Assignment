package com.example.weatherapp.Controllers;

import com.example.weatherapp.Models.Metric;
import com.example.weatherapp.Repo.MetricRepo;
import com.example.weatherapp.Repo.SensorRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api")
public class MetricController {

    @Autowired
    private SensorRepo sensorRepo;
    @Autowired
    private MetricRepo metricRepo;

    //localhost:8080/api/metrics
    @GetMapping("/metrics")
    public ResponseEntity<List<Metric>> getAllMetrics() {
        List<Metric> metrics = new ArrayList<Metric>(); // List of Metric Objects
        metricRepo.findAll().forEach(metrics::add);
        if (metrics.isEmpty()) { // If no Objects
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return No Content Http Status
        }
        return new ResponseEntity<>(metrics, HttpStatus.OK); // Else Return All Metrics with a 201 http status
    }


    //localhost:8080/api/metrics/limit/4
    @GetMapping("/metrics/limit/{length}")
    public ResponseEntity<List<Metric>> limitSensorsByCount(
            // Map Path Variable {length} to String length
            @PathVariable(value = "length") String length) {

        Pageable page = PageRequest.of(0,Integer.valueOf(length)); //Create Pagination of results with limit of {length}
        Page<Metric> metrics = metricRepo.findAll(page); // Find all metrics
        if (metrics.isEmpty()) { // No metrics found
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(metrics.get().collect(Collectors.toList()), HttpStatus.OK); // Convert Page object to A list of Objects

    }
    //localhost:8080/api/sensors/5/metrics/average?type=Temperture&dateRange=7
    //localhost:8080/api/sensors/5/metrics/average?type=Temperture
    @GetMapping("/sensors/{id}/metrics/average")
    public ResponseEntity<List<Metric>> getMetricsForSensorWithFilters(
            @PathVariable(value = "id", required = true) Long id,
            @RequestParam(value = "type",required = true) String mName,
            @RequestParam(value = "dataRange", defaultValue = "1") String range)

    {
        List<Metric> metrics = metricRepo.findBymetricvalue(id,mName,range);
        if (metrics.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }


    //http://localhost:8080/api/sensors/5/metrics/get?limit=2&type=Temperture&dataRange=3
    // Get Sensor 5 + limit 2 results + get Temperture data within last 3 days
    // http://localhost:8080/api/sensors/5/metrics/get?limit=2&type=Temperture
    // Get Sensor 5 Get Sensor 5 limit 2 results get Temperture data today
    //http://localhost:8080/api/sensors/5/metrics/get?limit=2
    // Get Sensor 5 Limit 2 results on any metric today
    //http://localhost:8080/api/sensors/5/metrics/get
    // Get Sensor 5 limit 10 results on any metric today
    @GetMapping("/sensors/{id}/metrics/get")
    public ResponseEntity<List<Metric>> RetrieveFilteredSensor(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "limit", defaultValue = "10") int limit, // Limit by default to 10 results
            @RequestParam(value = "type", defaultValue = "Temperture Humidity Pressure Lightning Snow Rain Cloud Visibility") String str, // by default insert all metric types into the SQL query
            @RequestParam(value = "dataRange", defaultValue = "1") String range) // by default if not provided retrieve metrics for dates between today and today
    {

        if (!sensorRepo.existsById(id)) { // If no Sensors Found
            throw new Error("Not found Sensor with id = " + id);
        }
        String mName[] = str.split(" "); // Convert to list for SQL QUERY WHERE IN clause
        List<Metric> metrics = metricRepo.findAllByMetricName(mName,id,range,limit);
        if (metrics.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }



    //localhost:8080/api/sensors/5/metrics
    @PostMapping("/sensors/{id}/metrics")
    public ResponseEntity<Metric> createMetric(@PathVariable(value = "id") Long id, @RequestBody Metric metricRequest) {
        Metric metric = sensorRepo.findById(id).map(sensor -> { // Iterate through Sensor
            metricRequest.setSensor(sensor); // Map the JSON body of http request to Metric Object setting Sensor relation
            return metricRepo.save(metricRequest); // Save to database
        }).orElseThrow(() -> new Error("Not found Sensor with id = " + id)); // No Sensor Found with id
        if (!sensorRepo.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(metric, HttpStatus.CREATED); // Return metric
    }


    //localhost:8080/api/metrics/country
    @GetMapping("/metrics/country")
    public ResponseEntity<List<Metric>> getLocationCountry(@RequestParam(value = "name") String countryName) {
        List<Metric> metric = metricRepo.findAllBySensorCountryIgnoreCase(countryName);
        if (metric.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(metric, HttpStatus.OK);
    }


    //localhost:8080/api/metric/4
    @DeleteMapping("/metric/{id}")
    public ResponseEntity<HttpStatus> deleteMetric(@PathVariable("id") Long id) {
        metricRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //localhost:8080/api/sensors/5/metrics
    @DeleteMapping("/sensors/{id}/metrics")
    public ResponseEntity<List<Metric>> deleteAllMetricsOfSensor(@PathVariable(value = "id") Long id) {
        if (!sensorRepo.existsById(id)) {
            throw new Error("Not found Sensor with id = " + id);
        }
        metricRepo.deleteBySensorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
