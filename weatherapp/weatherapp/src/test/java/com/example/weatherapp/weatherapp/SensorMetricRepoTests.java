package com.example.weatherapp.weatherapp;

import com.example.weatherapp.Models.Metric;
import com.example.weatherapp.Models.Sensor;
import com.example.weatherapp.Repo.MetricRepo;
import com.example.weatherapp.Repo.SensorRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SensorMetricRepoTests {
	@Autowired
	MetricRepo metricRepo;
	Sensor testsensor = new Sensor(2L,"France","Paris");
	Date date = new Date();
	Metric testMetric = new Metric (1L,"Temperture",14.5,date,testsensor);
	@Autowired
	SensorRepo sensorRepo;
	@Test
	public void testSensorCreate() {
		Sensor sensor = new Sensor();
		long sensorid = 1L;
		sensor.setId(sensorid);
		sensor.setCountry("Ireland");
		sensor.setCity("Dublin");
		sensorRepo.save(sensor);
		assertNotNull(sensorRepo.findById(sensorid).get());

	}
	@Test
	public void testSensorReadAll () {
		sensorRepo.save(testsensor);
		List<Sensor> list = sensorRepo.findAll();
		assertFalse(list.isEmpty());
	}

	@Test
	public  void testSingleSensor() {
		sensorRepo.save(testsensor);
		Sensor sensor = sensorRepo.findById(2L).get();
		assertEquals("France",sensor.getCountry());
	}

	@Test void testSensorDelete() {
		sensorRepo.save(testsensor);
		sensorRepo.deleteById(2L);
		assertFalse(sensorRepo.existsById(2L));
	}


	@Test
	public void testMetricReadAll () {
		metricRepo.save(testMetric);
		List<Metric> list = metricRepo.findAll();
		assertFalse(list.isEmpty());
	}

	@Test void testMetricDelete() {
		sensorRepo.save(testsensor);
		metricRepo.save(testMetric);
		metricRepo.deleteBySensorId(2L);
		assertFalse(metricRepo.existsById(2L));
	}

}
