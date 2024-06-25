package org.epam.gymservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HalloController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String html = "<html><body><p>Frontend part has not been finished, please use swagger UI on this <a href=\"http://ilya.zhizhin.xyz:2052/swagger-ui/index.html\">link</a></p></body></html>";
        return ResponseEntity.ok(html);
    }
}

