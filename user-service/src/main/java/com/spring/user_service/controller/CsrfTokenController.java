package com.spring.user_service.controller;

import com.spring.api.CsrfTokenControllerApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfTokenController implements CsrfTokenControllerApi {

    @GetMapping("/csrf")
    @Override
    public ResponseEntity<com.spring.dto.CsrfToken> csrfToken(com.spring.dto.CsrfToken csrfToken) {
        return ResponseEntity.ok(csrfToken);
    }
}
