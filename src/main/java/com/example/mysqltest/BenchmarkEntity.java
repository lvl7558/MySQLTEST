// BenchmarkEntity.java
package com.example.mysqltest;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;
import java.io.Serializable;

@Entity
public class BenchmarkEntity implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @Column (name = "year", nullable = false)
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int year;

    @NotNull
    @Column(nullable=false)
    private double temp;

    // getters and setters


    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public double getTemp() {
        return temp;
    }

}
