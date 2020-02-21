package com.project.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/lang/{lng}")
    public ResponseEntity chooseLanguage(@PathVariable("lng") String lng) throws JsonProcessingException {

        //логика (отправляем locale на service), получаем ответ, отправляем на страницу

        return ResponseEntity.status(HttpStatus.OK).body(new ObjectMapper().writeValueAsString(null));
    }
}
