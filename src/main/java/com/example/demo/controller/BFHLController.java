package com.example.demo.controller;

import com.example.demo.dto.BFHLRequest;
import com.example.demo.dto.BFHLResponse;
import com.example.demo.service.BFHLService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/bfhl")
@RequiredArgsConstructor
public class BFHLController {

    private final BFHLService bfhlService;

    @PostMapping
    public ResponseEntity<BFHLResponse> processData(@RequestBody BFHLRequest request) {
        log.info("POST /bfhl received");
        try {
            BFHLResponse response = bfhlService.process(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error processing /bfhl request", e);
            BFHLResponse errorResponse = BFHLResponse.builder()
                    .isSuccess(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
