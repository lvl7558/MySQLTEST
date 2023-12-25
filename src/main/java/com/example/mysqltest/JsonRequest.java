package com.example.mysqltest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class JsonRequest {
    @JsonProperty("temperatureData")
    private List<TemperatureData> temperatureData;

    @Getter
    public static class TemperatureData{
        private int year;

        private double temp;

        public void setYear(int year) {
            this.year = year;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }


    }

    // Constructors, getters, and setters




}