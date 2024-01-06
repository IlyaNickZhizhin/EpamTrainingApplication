package org.epam.gymservice.service.feign;

import lombok.RequiredArgsConstructor;
import org.epam.gymservice.config.ReportFeignClient;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadRequest;
import org.epam.gymservice.dto.reportDto.TrainerWorkloadResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AsyncFeignClientMethods {
    private final ReportFeignClient feignClient;

    @Async
    public CompletableFuture<TrainerWorkloadResponse> addWorkload(String bearerToken, TrainerWorkloadRequest request) {
        return CompletableFuture.completedFuture(feignClient.addWorkload(bearerToken, request));
    }

    @Async
    public CompletableFuture<TrainerWorkloadResponse> deleteWorkload(String bearerToken, TrainerWorkloadRequest request) {
        return CompletableFuture.completedFuture(feignClient.deleteWorkload(bearerToken, request));
    }
}
