package org.epam.reportservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.epam.reportservice.dto.ReportTrainerWorkloadRequest;
import org.epam.reportservice.dto.ReportTrainerWorkloadResponse;
import org.epam.reportservice.service.WorkloadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/workload")
@RequiredArgsConstructor
@Tag(name = "Workload controller", description = "for adding new training to workload list")
@Slf4j
@CrossOrigin
public class WorkloadController {

    private final WorkloadService workloadService;
    @PutMapping("/change")
    @Operation(summary = "Change trainer workload",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Add new workload to storage",
                    content = @Content(schema = @Schema(implementation = ReportTrainerWorkloadRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workload added changed successfully",
                            content = @Content(schema = @Schema(implementation = ReportTrainerWorkloadResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data for getting workload")
            })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or "+
            "hasAuthority('ROLE_TRAINER') or " +
            "hasAuthority('ROLE_TRAINEE')")
    public ResponseEntity<ReportTrainerWorkloadResponse> add(@RequestBody ReportTrainerWorkloadRequest request) {
        log.info("Adding workload of trainer{} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return new ResponseEntity<>(workloadService.addWorkload(request), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new ReportTrainerWorkloadResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/change")
    @Operation(summary = "Change trainer workload",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Add new workload to storage",
                    content = @Content(schema = @Schema(implementation = ReportTrainerWorkloadRequest.class))),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Workload added changed successfully",
                            content = @Content(schema = @Schema(implementation = ReportTrainerWorkloadResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid data for getting workload")
            })
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or "+
            "hasAuthority('ROLE_TRAINER') or " +
            "hasAuthority('ROLE_TRAINEE')")
    public ResponseEntity<ReportTrainerWorkloadResponse> delete(@RequestBody ReportTrainerWorkloadRequest request) {
        log.info("Deleting workload of trainer{} {}***", request.getFirstName(), request.getLastName().charAt(0));
        try {
            return new ResponseEntity<>(workloadService.deleteWorkload(request), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(new ReportTrainerWorkloadResponse(), HttpStatus.BAD_REQUEST);
        }
    }
}
