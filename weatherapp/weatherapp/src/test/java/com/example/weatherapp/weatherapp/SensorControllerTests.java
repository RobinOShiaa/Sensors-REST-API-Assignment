package com.example.weatherapp.weatherapp;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class SensorControllerTests {

    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter =  objectMapper.writer();
    @Mock
    private SensorRepo sensorRepo;
    @InjectMocks
    private SensorController sensorController;

    Sensor RECORD_1 = new Sensor(1L,"Ireland","Dublin");
    Sensor RECORD_2 = new Sensor(2L,"France","Paris");
    Sensor RECORD_3 = new Sensor(3L,"Spain","Madrid");

    Sensor RECORD_4 = new Sensor(4L,"Italy","Rome");


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sensorController).build();
    }

    @Test
    public void getAllSensors() throws Exception {
        List<Sensor> sensors = new ArrayList<>(Arrays.asList(RECORD_1,RECORD_2,RECORD_3));
        Mockito.when(sensorRepo.findAll()).thenReturn(sensors);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[2].country", Matchers.is("Spain")))
                .andExpect(jsonPath("$[1].country", Matchers.is("France")));



    }

    @Test
    public void getSensorById_Success() throws Exception {
        Mockito.when(sensorRepo.findById(RECORD_1.getId())).thenReturn(Optional.ofNullable(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/sensors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.jsonPath("$", notNullValue()))
                        .andExpect(jsonPath("$.country", Matchers.is("Ireland")));

    }
    @Test
    public void getSensorById_Fail() throws Exception {
        Mockito.when(sensorRepo.findById(RECORD_1.getId())).thenReturn(Optional.ofNullable(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/sensors/111111111dsadag1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }

    @Test
    public void CreateSensor_Success() throws Exception{
        Sensor sensor = Sensor.builder()
                .id(4L)
                .country("Italy")
                .city("Rome")
                .build();
        Mockito.when(sensorRepo.save(sensor)).thenReturn(sensor);
        String content = objectWriter.writeValueAsString(sensor);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().is(201))
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.country", Matchers.is("Italy")));
    }

    @Test
    public void deleteSensorById_Success () throws Exception {
        Mockito.when(sensorRepo.findById(RECORD_2.getId())).thenReturn(Optional.of(RECORD_2));
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/sensors/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }





}
