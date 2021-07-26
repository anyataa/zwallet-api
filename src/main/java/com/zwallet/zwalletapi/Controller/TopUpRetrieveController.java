package com.zwallet.zwalletapi.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topup")
public class TopUpRetrieveController {

    public ResponseEntity<?> topUpAccount() {
        return ResponseEntity.ok().body("body");
    }

}
