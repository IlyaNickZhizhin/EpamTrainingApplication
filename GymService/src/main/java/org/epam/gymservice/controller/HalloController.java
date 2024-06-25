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
                        "<p>Frontend part has not been finished</p>," +
                        "<p>please use swagger UI on this <a href=\"http://64.225.100.214:2052/swagger-ui/index.html\">link</a></p>" +
                        "<p>1) enter /v3/api-docs in the Swagger UI page</p>" +
                        "<p>2) push \"Explore\" button</p>" +
                        "<p>3) explore it</p>" +
                        "</body></html>";
        return ResponseEntity.ok(html);
    }
}

