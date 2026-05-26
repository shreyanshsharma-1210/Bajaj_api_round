package com.example.demo.controller;

import com.example.demo.dto.BFHLRequest;
import com.example.demo.dto.BFHLResponse;
import com.example.demo.service.BFHLService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BFHLController.class)
class BFHLControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BFHLService bfhlService;

    @Test
    @DisplayName("POST /bfhl returns 200 with valid response")
    void testPostBfhlSuccess() throws Exception {
        BFHLResponse mockResponse = BFHLResponse.builder()
                .isSuccess(true)
                .userId("shreyansh_sharma_12102005")
                .email("sharmashreyansh340@gmail.com")
                .rollNumber("21BCE1210")
                .oddNumbers(List.of("1"))
                .evenNumbers(Arrays.asList("334", "4"))
                .alphabets(Arrays.asList("A", "R"))
                .specialCharacters(List.of("$"))
                .sum("339")
                .concatString("Ra")
                .build();

        when(bfhlService.process(any(BFHLRequest.class))).thenReturn(mockResponse);

        BFHLRequest request = new BFHLRequest(Arrays.asList("a", "1", "334", "4", "R", "$"));


        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.user_id").value("shreyansh_sharma_12102005"))
                .andExpect(jsonPath("$.email").value("sharmashreyansh340@gmail.com"))
                .andExpect(jsonPath("$.roll_number").value("21BCE1210"))
                .andExpect(jsonPath("$.odd_numbers[0]").value("1"))
                .andExpect(jsonPath("$.even_numbers").isArray())
                .andExpect(jsonPath("$.alphabets").isArray())
                .andExpect(jsonPath("$.special_characters[0]").value("$"))
                .andExpect(jsonPath("$.sum").value("339"))
                .andExpect(jsonPath("$.concat_string").value("Ra"));
    }

    @Test
    @DisplayName("POST /bfhl with empty data returns 200")
    void testPostBfhlEmptyData() throws Exception {
        BFHLResponse mockResponse = BFHLResponse.builder()
                .isSuccess(true)
                .userId("shreyansh_sharma_12102005")
                .email("sharmashreyansh340@gmail.com")
                .rollNumber("21BCE1210")
                .oddNumbers(Collections.emptyList())
                .evenNumbers(Collections.emptyList())
                .alphabets(Collections.emptyList())
                .specialCharacters(Collections.emptyList())
                .sum("0")
                .concatString("")
                .build();

        when(bfhlService.process(any(BFHLRequest.class))).thenReturn(mockResponse);

        BFHLRequest request = new BFHLRequest(Collections.emptyList());

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success").value(true))
                .andExpect(jsonPath("$.sum").value("0"))
                .andExpect(jsonPath("$.odd_numbers").isArray())
                .andExpect(jsonPath("$.even_numbers").isArray());
    }

    @Test
    @DisplayName("POST /bfhl returns 500 when service throws exception")
    void testPostBfhlServiceException() throws Exception {
        when(bfhlService.process(any(BFHLRequest.class))).thenThrow(new RuntimeException("Unexpected error"));

        BFHLRequest request = new BFHLRequest(List.of("a"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.is_success").value(false));
    }
}
