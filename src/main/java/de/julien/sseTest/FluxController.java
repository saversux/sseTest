package de.julien.sseTest;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;

@RestController
@Log
public class FluxController {
    final private HashMap<String, Sinks.Many<TestEntity>> sinks = new HashMap<>();

    @GetMapping("/human/{name}")
    public Flux<ServerSentEvent<TestEntity>> streamEvents(@PathVariable String name) throws WebClientException {
        return getSink(name).asFlux().map(e -> ServerSentEvent.builder(e).build());
    }

    @ResponseStatus(
            value = HttpStatus.BAD_REQUEST,
            reason = "stream already taken")
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateHandler() {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("stream already taken");
    }

    @GetMapping(value = "/ok", produces = MediaType.TEXT_PLAIN_VALUE)
    public String ok() {
        sinks.forEach((key, value) -> {
            Sinks.EmitResult result = value.tryEmitNext(new TestEntity(key, 18, true));
            log.info(key + " sub count: " + value.currentSubscriberCount());

            if (result.isFailure()) {
                log.severe("emit failed, removing sink");
            }
        });

        return "ok";
    }

    private Sinks.Many<TestEntity> getSink(String name) {
        if (sinks.containsKey(name)) {
            sinks.get(name).tryEmitComplete();
        }
        sinks.put(name, Sinks.many().unicast().onBackpressureBuffer());
        return sinks.get(name);
    }

}
