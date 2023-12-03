package com.example.mysqltest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RestController
@RequestMapping("/api/temp")
public class EntityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityController.class);

    @Autowired
    private EntityService entityService;

    @RequestMapping (method = RequestMethod.POST, consumes={MediaType.MULTIPART_FORM_DATA_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody CompletableFuture<List<BenchmarkEntity>> addNewData (@RequestParam int year, @RequestParam double temp){
        try {
            return entityService.saveTemps(year,temp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }




    private static Function<Throwable, ResponseEntity<? extends List<BenchmarkEntity>>> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    @RequestMapping (method = RequestMethod.GET, consumes={MediaType.APPLICATION_JSON_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE})
    public @ResponseBody ResponseEntity getAllTemps() {
        try {
            CompletableFuture<List<BenchmarkEntity>> cars1=entityService.getAllTemps();
            CompletableFuture<List<BenchmarkEntity>> cars2=entityService.getAllTemps();
            CompletableFuture<List<BenchmarkEntity>> cars3=entityService.getAllTemps();
            CompletableFuture.allOf(cars1, cars2, cars3).join();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch(final Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

