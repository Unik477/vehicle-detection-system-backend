package com.bbau.vehicledetection.backend.controller;




import com.bbau.vehicledetection.backend.entity.BlockedVehicle.BlockedVehicle;
import com.bbau.vehicledetection.backend.service.BlockedVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/blocked-vehicles")
@CrossOrigin("*")
public class BlockedVehicleController {

    private final BlockedVehicleService blockedVehicleService;

    @Autowired
    public BlockedVehicleController(BlockedVehicleService blockedVehicleService) {
        this.blockedVehicleService = blockedVehicleService;
    }

    //  Check if a vehicle is currently blocked
    @GetMapping("/check/{vehicleNumber}")
    public ResponseEntity<Boolean> isVehicleBlocked(@PathVariable String vehicleNumber) {
        boolean isBlocked = blockedVehicleService.isVehicleBlocked(vehicleNumber);
        return ResponseEntity.ok(isBlocked);
    }

    //  Get details of a blocked vehicle
    @GetMapping("/{vehicleNumber}")
    public ResponseEntity<?> getBlockedVehicle(@PathVariable String vehicleNumber) {
        Optional<BlockedVehicle> vehicle = blockedVehicleService.getBlockedVehicle(vehicleNumber);
        return vehicle.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Block a vehicle
@PostMapping("/block")
public ResponseEntity<?> blockVehicle(@RequestBody BlockRequest request) {
    try {
        BlockedVehicle vehicle = blockedVehicleService.blockVehicle(
                request.getVehicleNumber(),
                request.getBlockedBy(),
                request.getBlockedReason()
        );
        return ResponseEntity.ok(vehicle);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    //  Allow a previously blocked vehicle by vehicle number
@PostMapping("/allow/{vehicleNumber}")
public ResponseEntity<BlockedVehicle> allowVehicle(
        @PathVariable String vehicleNumber,
        @RequestBody AllowRequest request) {

    BlockedVehicle vehicle = blockedVehicleService.allowVehicleByNumber(
            vehicleNumber,
            request.getAllowedBy(),
            request.getAllowedReason()
    );
    return ResponseEntity.ok(vehicle);
}

    // DTOs for request payloads -- good practice by Nikhil Papa
    public static class BlockRequest {
        private String vehicleNumber;
        private String blockedBy;
        private String blockedReason;

        public String getVehicleNumber() { return vehicleNumber; }
        public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

        public String getBlockedBy() { return blockedBy; }
        public void setBlockedBy(String blockedBy) { this.blockedBy = blockedBy; }

        public String getBlockedReason() { return blockedReason; }
        public void setBlockedReason(String blockedReason) { this.blockedReason = blockedReason; }
    }

    public static class AllowRequest {
        private String allowedBy;
        private String allowedReason;

        public String getAllowedBy() { return allowedBy; }
        public void setAllowedBy(String allowedBy) { this.allowedBy = allowedBy; }

        public String getAllowedReason() { return allowedReason; }
        public void setAllowedReason(String allowedReason) { this.allowedReason = allowedReason; }
    }
}

