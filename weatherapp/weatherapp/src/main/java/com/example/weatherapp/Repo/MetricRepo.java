package com.example.weatherapp.Repo;

import com.example.weatherapp.Models.Metric;
import com.example.weatherapp.Models.Sensor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

public interface MetricRepo extends JpaRepository<Metric,Long> {
    // SQl statement to reduce data of relevant table for specific request
    @Query(value = "SELECT sensorid,metricname,metricvalue,metricid,recorded FROM (SELECT * FROM (SELECT * FROM (SELECT * FROM metric JOIN sensor ON metric.sensorid = sensor.id WHERE sensorid= :id) as T WHERE metricname IN (:mName)) as S WHERE recorded BETWEEN DATE_SUB(NOW(), INTERVAL :range DAY) AND NOW()) as B ORDER BY recorded DESC LIMIT :limit", nativeQuery = true)
    List<Metric> findAllByMetricName(String [] mName,Long id,String range,int limit); // Finds Metric Objects based off the dynamic information prescribed by the SQL QUERY


    List<Metric> findAllBySensorCountryIgnoreCase(String name);
    // Finds all Metric Objects mapped to SQL database by Country name and is CaseInsensitive

    // Query to subset data and display average value of metric value
    @Query(value = "SELECT metricid,metricname,AVG(metricvalue) as metricvalue,recorded,sensorid,city,country FROM (SELECT * FROM (SELECT * FROM (SELECT * FROM metric JOIN sensor ON metric.sensorid = sensor.id WHERE sensorid= :id) as T WHERE metricname = :mName) as S WHERE recorded BETWEEN DATE_SUB(NOW(), INTERVAL :range DAY) AND NOW()) as B",nativeQuery = true)
    List<Metric> findBymetricvalue(Long id,String mName,String range);
    @Transactional
    void deleteBySensorId(Long sensorId);

    // Delete Metrics based off the sensorId
}
