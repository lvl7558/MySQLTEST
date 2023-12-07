package com.example.mysqltest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/temp")
public class EntityController {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityController.class);

    @Autowired
    private EntityService entityService;

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
public @ResponseBody CompletableFuture<List<BenchmarkEntity>> addNewData(
        @RequestBody JsonRequest jsonRequest
) {
    try {
        // Assuming you have a method to handle the list of temperature data
        return entityService.saveTemps(jsonRequest.getTemperatureData());
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}



    private static Function<Throwable, ResponseEntity<? extends List<BenchmarkEntity>>> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<BenchmarkEntity>> getAllTemps() {
        try {
            CompletableFuture<List<BenchmarkEntity>> cars1 = entityService.getAllTemps();
            CompletableFuture<List<BenchmarkEntity>> cars2 = entityService.getAllTemps();
            CompletableFuture<List<BenchmarkEntity>> cars3 = entityService.getAllTemps();

            CompletableFuture<Void> allOf = CompletableFuture.allOf(cars1, cars2, cars3);
            allOf.join(); // Wait for all futures to complete
            //remove the joining
            List<BenchmarkEntity> result = allOf.thenApply(v ->
                    Stream.of(cars1.join(), cars2.join(), cars3.join())
                            .flatMap(List::stream)
                            .collect(Collectors.toList())
            ).join();

            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (CompletionException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}

