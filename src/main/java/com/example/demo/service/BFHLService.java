package com.example.demo.service;

import com.example.demo.dto.BFHLRequest;
import com.example.demo.dto.BFHLResponse;

public interface BFHLService {

    BFHLResponse process(BFHLRequest request);
}
