package ru.antara.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import ru.antara.mock.data.Product;
import ru.antara.mock.response.ResponsePostData;
import ru.antara.mock.response.ResponseProducts;
import ru.antara.mock.data.Status;
import ru.antara.mock.response.ResponseSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController()
@RequestMapping("api/mock")
public class MockController {

    private final Logger logger = LoggerFactory.getLogger(MockController.class);

    private long timeSleep = 1000L;
    private long timeOut = 10000L;

    public final ResponseProducts data = new ResponseProducts(Status.SUCCESS,
            new ArrayList<>(Arrays.asList(new Product(1, "Хлеб", 123), new Product(2, "Колбаса", 12)))
    );

    @GetMapping(value = "/settings", produces = "application/json")
    public ResponseSettings changeSettings(@RequestParam(name = "timeSleep", required = false) Long timeSleep,
                                           @RequestParam(name = "timeOut", required = false) Long timeOut) {
        this.timeSleep = Objects.isNull(timeSleep) ? this.timeSleep : timeSleep;
        this.timeOut = Objects.isNull(timeOut) ? this.timeOut : timeOut;
        return new ResponseSettings(this.timeSleep, this.timeOut);
    }

    @Async
    @GetMapping(value = "/data", produces = "application/json")
    public CompletableFuture<ResponseProducts> getData() {

        return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return data;
                }).
                completeOnTimeout(new ResponseProducts(Status.ERROR, new ArrayList<>()), timeOut, TimeUnit.MILLISECONDS).
                thenApply(responseProducts -> {
                    if (responseProducts.status().equals(Status.ERROR)) {
                        logger.error("TIMEOUT GET");
                    } else {
                        logger.info("GET: " + data);
                    }
                    return responseProducts;
                });
    }

    @Async
    @PostMapping(value = "/submit", consumes = "application/json", produces = "application/json")
    public CompletableFuture<ResponsePostData> submitData(@RequestBody Product product) {
        return CompletableFuture.supplyAsync(() -> {
                    try {
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return new ResponsePostData(Status.SUCCESS, "Всё ок!");
                }).
                completeOnTimeout(new ResponsePostData(Status.ERROR, "Слишком долго"), timeOut, TimeUnit.MILLISECONDS).
                thenApply(responsePostData -> {
                    if (responsePostData.status().equals(Status.ERROR)) {
                        logger.error("TIMEOUT ADDED: " + product);
                    } else {
                        data.items().add(product);
                        logger.info("ADD: " + product);
                    }
                    return responsePostData;
                });
    }
}