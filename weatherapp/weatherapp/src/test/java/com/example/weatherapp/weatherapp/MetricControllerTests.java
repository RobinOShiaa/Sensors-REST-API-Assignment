package com.example.weatherapp.weatherapp;

import com.example.weatherapp.Controllers.MetricController;
import com.example.weatherapp.Controllers.SensorController;
import com.example.weatherapp.Models.Metric;
import com.example.weatherapp.Models.Sensor;
import com.example.weatherapp.Repo.MetricRepo;
import com.example.weatherapp.Repo.SensorRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.geo.Metrics;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import com.example.weatherapp.Controllers.SensorController;
import com.example.weatherapp.Models.Sensor;
import com.example.weatherapp.Repo.SensorRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class MetricControllerTests {
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();
    @Mock
    private SensorRepo sensorRepo;
    @Mock
    private MetricRepo metricRepo;

    @InjectMocks
    private MetricController metricController;
    Date date = new Date();
    Sensor RECORD_4 = new Sensor(6L, "Dublin", "Ireland");
    Metric RECORD_1 = new Metric(1L, "Temperture", 14.5, date, RECORD_4);
    Metric RECORD_2 = new Metric(2L, "Humidity", 25, date, RECORD_4);
    Metric RECORD_3 = new Metric(3L, "Temperture", 8.5, date, RECORD_4);


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(metricController).build();
    }

    @Test
    public void getAllMetrics() throws Exception {
        List<Metric> metrics = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        Mockito.when(metricRepo.findAll()).thenReturn(metrics);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/metrics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].metricname", Matchers.is("Temperture")))
                .andExpect(jsonPath("$[1].metricname", Matchers.is("Humidity")));

    }

    @Test
    public void deleteMetricById_Success () throws Exception {
        Mockito.when(metricRepo.findById(RECORD_1.getMetricid())).thenReturn(Optional.of(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api//metric/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }


}



