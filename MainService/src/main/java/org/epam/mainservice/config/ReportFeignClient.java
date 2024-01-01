package org.epam.mainservice.config;

import org.epam.mainservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.mainservice.dto.reportDto.TrainerWorkloadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "report-service")
public interface ReportFeignClient {

    @PostMapping("/v1/api/workload/change")
    TrainerWorkloadResponse getWorkload(@RequestBody TrainerWorkloadRequest request);
}
