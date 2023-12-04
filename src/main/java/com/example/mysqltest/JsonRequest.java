package com.example.mysqltest;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class JsonRequest {
    @JsonProperty("temperatureData")
    private List<TemperatureData> temperatureData;

    public static class TemperatureData{
        private int year;

        private double temp;
        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }


    }

    public List<TemperatureData> getTemperatureData() {
        return temperatureData;
    }

    // Constructors, getters, and setters




}