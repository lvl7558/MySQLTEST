package com.example.mysqltest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;


@Service
public class EntityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityService.class);

    @Autowired
    private BenchmarkRepository benchmarkRepository;

//    @Async
//    public CompletableFuture<List<BenchmarkEntity>> saveTemps(final InputStream inputStream) throws Exception {
//        final long start = System.currentTimeMillis();
//
//        List<BenchmarkEntity> list = parseCSVFile(inputStream);
//
//        LOGGER.info("Saving a list of cars of size {} records", list.size());
//
//        list = benchmarkRepository.saveAll(list);
//
//        LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start));
//        return CompletableFuture.completedFuture(list);
//    }
@Async
public CompletableFuture<List<BenchmarkEntity>> saveTemps(int year
        , double temp) throws Exception {
    final long start = System.currentTimeMillis();



    LOGGER.info("Saving a list of cars of size {} records");
    BenchmarkEntity n = new BenchmarkEntity();
    n.setYear(year);
    n.setTemp(temp);
    benchmarkRepository.save(n);
    List<BenchmarkEntity> l = new ArrayList<>();
    l.add(n);
    LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start));
    return CompletableFuture.completedFuture(l);
}

//    private List<BenchmarkEntity> parseCSVFile(final InputStream inputStream) throws Exception {
//        final List<BenchmarkEntity> temps=new ArrayList<>();
//        try {
//            try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
//                String line;
//                while ((line=br.readLine()) != null) {
//                    final String[] data=line.split(",");
//                    final BenchmarkEntity temp=new BenchmarkEntity();
//                    temp.setYear(Integer.parseInt(data[0]));
//                    temp.setTemp(Double.parseDouble(data[1]));
//                    temps.add(temp);
//                }
//                return temps;
//            }
//        } catch(final IOException e) {
//            LOGGER.error("Failed to parse CSV file {}", e);
//            throw new Exception("Failed to parse CSV file {}", e);
//        }
//    }

    @Async
    public CompletableFuture<List<BenchmarkEntity>> getAllTemps() {

        LOGGER.info("Request to get a list of temps");

        final List<BenchmarkEntity> cars = benchmarkRepository.findAll();
        return CompletableFuture.completedFuture(cars);
    }

}