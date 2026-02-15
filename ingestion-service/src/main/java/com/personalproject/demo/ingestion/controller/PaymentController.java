package com.personalproject.demo.ingestion.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @GetMapping("/status")
    public String checkStatus(){
        return"Ingestion Service is Online";
    }
}
