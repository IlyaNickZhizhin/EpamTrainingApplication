package org.epam.gymservice.config;

import org.epam.gymservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "report-service")
public interface ReportFeignClient {

    @PostMapping("/v1/api/workload/change")
    TrainerWorkloadResponse getWorkload(@RequestHeader("Authorization") String bearerToken, @RequestBody TrainerWorkloadRequest request);
}
