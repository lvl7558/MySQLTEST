package com.example.mysqltest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class EntityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityService.class);

    @Autowired
    private BenchmarkRepository benchmarkRepository;



    public List<BenchmarkEntity> saveTemps(List<JsonRequest.TemperatureData> temperatureData) throws Exception {
        final long start = System.currentTimeMillis();
        List<BenchmarkEntity> entitiesToSave = temperatureData.stream()
                .map(this::createBenchmarkEntity)
                .collect(Collectors.toList());

        List<BenchmarkEntity> savedEntities = benchmarkRepository.saveAll(entitiesToSave);
        //LOGGER.info("Saving a list of cars of size {} records");
        LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start));
        return savedEntities;
    }

    private BenchmarkEntity createBenchmarkEntity(JsonRequest.TemperatureData temperatureData) {
        BenchmarkEntity entity = new BenchmarkEntity();
        entity.setTemp(temperatureData.getTemp());
        entity.setYear(temperatureData.getYear());
        return entity;
    }

    public List<BenchmarkEntity> getAllTemps() throws IOException {
        LOGGER.info("JDBS GET");
        final long start = System.currentTimeMillis();
        LOGGER.info("Start Time: {}", (start));
        final List<BenchmarkEntity> temps = benchmarkRepository.findAll();
        final long end = System.currentTimeMillis();
        LOGGER.info("Elapsed Time: {}", (end - start));
        LOGGER.info("End Time: {}", (end));

        if (!Files.exists(Path.of("JDBS_time2.csv"))) {
            Files.createFile(Path.of("JDBS_time2.csv"));
        }

        // Add data to CSV file
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of("JDBS_time2.csv"), StandardOpenOption.APPEND)) {
            String line = ""+(end - start);
            writer.write(line);
            writer.newLine();
        }
        return temps;
    }

}