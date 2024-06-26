package org.epam.gymservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HalloController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        String html =
                "<html>" +
                        "<body>" +
                        "<p>Frontend part has not been finished,</p>" +
                        "<p>please use swagger UI on this <a href=\"https://ilya.zhizhin.xyz/swagger-ui/index.html\">link</a></p>" +
                        "</body></html>";
        return ResponseEntity.ok(html);
    }
}

