package com.example.weatherapp.Controllers;

import com.example.weatherapp.Models.Metric;
import com.example.weatherapp.Models.Sensor;
import com.example.weatherapp.Repo.SensorRepo;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SensorController {
    @Autowired
    SensorRepo sensorRepo;

    //localhost:8080/api/sensors
    @GetMapping("/sensors")
    public ResponseEntity<List<Sensor>> getAllSensors() {
        List<Sensor> sensors = new ArrayList<Sensor>();
        sensorRepo.findAll().forEach(sensors::add);
        if (sensors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sensors, HttpStatus.OK);
    }

    //localhost:8080/api/sensors/country
    @GetMapping("/sensors/country")
    public ResponseEntity<List<Sensor>> getLocationCountry(@RequestParam(value = "name") String countryName) {
        List<Sensor> sensors = sensorRepo.findAllBycountryIgnoreCase(countryName);
        if (sensors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(sensors, HttpStatus.OK);
    }

    //localhost:8080/api/sensors/5
    @GetMapping("/sensors/{id}")
    public ResponseEntity<Sensor> getSensorsById(@PathVariable("id") Long id) {
        Sensor sensor = sensorRepo.findById(id).orElseThrow(() -> new Error("Not found Sensor with id = " + id));
        return new ResponseEntity<>(sensor, HttpStatus.OK);
    }

    //localhost:8080/api/sensors
    @PostMapping("/sensors")
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) {
        try {
            Sensor _sensor = sensorRepo.save(new Sensor(sensor.getId(), sensor.getCountry(), sensor.getCity()));
            return new ResponseEntity<>(_sensor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    //localhost:8080/api/sensors/limit/5
    @GetMapping("/sensors/limit/{length}")
    public ResponseEntity<List<Sensor>> limitSensorsByCount(@PathVariable(value = "length") String length) {
        Pageable page = PageRequest.of(0,Integer.valueOf(length));
        Page<Sensor> sensors = sensorRepo.findAll(page);

        return new ResponseEntity<>(sensors.get().collect(Collectors.toList()), HttpStatus.OK);

    }

    //localhost:8080/api/sensors/5
    @DeleteMapping("/sensors/{id}")
    public ResponseEntity<HttpStatus> deleteSensor(@PathVariable("id") Long id) {
        if(sensorRepo.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        sensorRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //localhost:8080/api/sensors
    @DeleteMapping("/sensors")
    public ResponseEntity<HttpStatus> deleteAllSensors() {
        sensorRepo.deleteAll(); // Delete all sensors

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
