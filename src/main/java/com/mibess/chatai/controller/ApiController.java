package com.mibess.chatai.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ApiController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String, String>> hostCheck() {

        Map<String, String> response = new HashMap<>();
        response.put("hostcheck",
                "Ok v1 - Deployed at: " + new Date().toString());

        return ResponseEntity.ok().body(response);
    }
}
