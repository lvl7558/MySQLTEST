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
    CompletableFuture<List<BenchmarkEntity>> completableFuture = new CompletableFuture<>();


    Runnable run = new Runnable() {
        @Override
        public void run() {
            try {
                // Assuming you have a method to handle the list of temperature data
                List<BenchmarkEntity> result = entityService.saveTemps(jsonRequest.getTemperatureData()).join();
                completableFuture.complete(result);
            } catch (Exception e) {
                completableFuture.completeExceptionally(e);
            }
        }
    };

    // Assuming you have a mechanism to execute the Runnable asynchronously (e.g., using an Executor)
    //CompletableFuture.runAsync(run);

    //Code for Platform threads
    Thread t = new Thread(run);
    t.start();

    //Thread t = Thread.startVirtualThread(run);

    return completableFuture;

    //old way of doing it
//    try {
//        // Assuming you have a method to handle the list of temperature data
//        return entityService.saveTemps(jsonRequest.getTemperatureData());
//    } catch (Exception e) {
//        throw new RuntimeException(e);
//    }
}



    private static Function<Throwable, ResponseEntity<? extends List<BenchmarkEntity>>> handleGetCarFailure = throwable -> {
        LOGGER.error("Failed to read records: {}", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<BenchmarkEntity>> getAllTemps() {

        CompletableFuture<List<BenchmarkEntity>> tempsFuture = new CompletableFuture<>();

        Runnable run = new Runnable() {
            @Override
            public void run() {
                try {
                    List<BenchmarkEntity> list = entityService.getAllTemps().join();
                    tempsFuture.complete(list);
                } catch (Exception e) {
                    tempsFuture.completeExceptionally(e);
                }
            }
        };
        //Coce for platform threads
        Thread t = new Thread(run);
        t.start();

        //Thread t = Thread.startVirtualThread(run);



        try {
            // Wait for the thread to complete (this is a simple way to achieve synchronization,
            // but in a real application, you might want to use more robust synchronization mechanisms)
            t.join();

            // Get the result from the CompletableFuture
            List<BenchmarkEntity> resultList = tempsFuture.join();

            return ResponseEntity.status(HttpStatus.OK).body(resultList);
        } catch (InterruptedException e) {
            // Handle InterruptedException
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }


        //old way in ansyrncous
//        try {
//            CompletableFuture<List<BenchmarkEntity>> cars1 = entityService.getAllTemps();
//            CompletableFuture<List<BenchmarkEntity>> cars2 = entityService.getAllTemps();
//            CompletableFuture<List<BenchmarkEntity>> cars3 = entityService.getAllTemps();
//
//            CompletableFuture<Void> allOf = CompletableFuture.allOf(cars1, cars2, cars3);
//            allOf.join(); // Wait for all futures to complete
//            //remove the joining
//            List<BenchmarkEntity> result = allOf.thenApply(v ->
//                    Stream.of(cars1.join(), cars2.join(), cars3.join())
//                            .flatMap(List::stream)
//                            .collect(Collectors.toList())
//            ).join();
//
//            return ResponseEntity.status(HttpStatus.OK).body(result);
//        } catch (CompletionException e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
//        }
    }
}

