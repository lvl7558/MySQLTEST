package com.example.mysqltest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * The rest controller is already acting like a multithreaded enviorment, so each request is already
 * following a thread per request model
 *
 *
 */
@RestController
@RequestMapping("/api/temp")
public class EntityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityController.class);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private EntityService entityService;

    @Autowired
    private BenchmarkRepository benchmarkRepository;

//    @RequestMapping (method = RequestMethod.POST, consumes={MediaType.MULTIPART_FORM_DATA_VALUE},
//            produces={MediaType.APPLICATION_JSON_VALUE})
//    public @ResponseBody CompletableFuture<List<BenchmarkEntity>> addNewData (@RequestParam int year, @RequestParam double temp){
//        try {
//            return entityService.saveTemps(year,temp);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
@RequestMapping(
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public @ResponseBody List<BenchmarkEntity> addNewData(
        @RequestBody JsonRequest jsonRequest
) {
    try {
        // Assuming you have a method to handle the list of temperature data
        LOGGER.info("HTTP POST ");
        LocalDateTime timestamp = LocalDateTime.now();
        logTimestamp(timestamp);
        return entityService.saveTemps(jsonRequest.getTemperatureData());
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}



    private static Function<Throwable, ResponseEntity<? extends List<BenchmarkEntity>>> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

//    @RequestMapping(method = RequestMethod.GET)
//    public @ResponseBody ResponseEntity<List<BenchmarkEntity>> getAllTemps() {
    @GetMapping
    public List<BenchmarkEntity> getAllTemps(){
        //LOGGER.info("HTTP GET Thread info {} ", Thread.currentThread());
        return benchmarkRepository.findAll();
//        try {



            /*LOGGER.info("HTTP GET");
            final long start = System.currentTimeMillis();
            LOGGER.info("Start Time: {}", (start));
            List<BenchmarkEntity> result = (List<BenchmarkEntity>) entityService.getAllTemps();
            final long end = System.currentTimeMillis();
            LOGGER.info("Elapsed Time: {}", (end - start));
            LOGGER.info("End Time: {}", (end));*/

            /*if (!Files.exists(Path.of("HTTP_time2.csv"))) {
                Files.createFile(Path.of("HTTP_time2.csv"));
            }

            // Add data to CSV file
            try (BufferedWriter writer = Files.newBufferedWriter(Path.of("HTTP_time2.csv"), StandardOpenOption.APPEND)) {
                String line = ""+(end - start);
                writer.write(line);
                writer.newLine();
            }*/

//            List<BenchmarkEntity> result = (List<BenchmarkEntity>) entityService.getAllTemps();
//            return ResponseEntity.status(HttpStatus.OK).body(result);
//        } catch (CompletionException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }


    private void logTimestamp(LocalDateTime timestamp) {
        // Log timestamp using SLF4J
        LOGGER.info("Timestamp recorded: {}", timestamp.format(DATE_TIME_FORMATTER));
    }

    @PutMapping("/{id}")
    public BenchmarkEntity updateBook(@PathVariable Long id, @RequestBody BenchmarkEntity updatedEntity) {
        return  entityService.updateBook(id,updatedEntity);

    }

    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        entityService.deleteBook(id);
    }
}

