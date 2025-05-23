package com.bbau.vehicledetection.backend.controller;

import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleEntry;
import com.bbau.vehicledetection.backend.service.VehicleEntryService;
import com.bbau.vehicledetection.backend.service.WebSocketSender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin("*") // Allows requests from frontend
public class VehicleEntryController {

    @Autowired
    private WebSocketSender webSocketSender;

    @Autowired
    private VehicleEntryService vehicleEntryService;

    // ✅ Handle vehicle entry or exit
    // @PostMapping("/entry")
    // public ResponseEntity<?> registerVehicleEntry(@RequestBody VehicleEntry
    // vehicleEntry) {
    // try {
    // vehicleEntryService.handleVehicleEntry(vehicleEntry); // Pass the entire
    // object
    // return ResponseEntity.ok("Vehicle entry processed successfully.");
    // } catch (Exception e) {
    // return ResponseEntity.badRequest().body(e.getMessage());
    // }
    // }

    @PostMapping("/entry")
    public ResponseEntity<String> registerVehicleEntry(@RequestBody VehicleEntry vehicleEntry) {
        vehicleEntryService.handleVehicleEntry(vehicleEntry);

        webSocketSender.send("/topic/vehicle-entry", vehicleEntry);

        return ResponseEntity.ok("Vehicle entry processed successfully.");
    }

    // ✅ Get list of vehicles currently inside
    @GetMapping("/inside")
    public List<VehicleEntry> getVehiclesInside() {
        return vehicleEntryService.getAllActiveVehicles();
    }

    // ✅ Mark vehicle exit
    // @PutMapping("/exit/{vehicleNumber}/{exitGate}")
    // public VehicleEntry markVehicleExit(@PathVariable String vehicleNumber,
    // @PathVariable int exitGate) {
    // return vehicleEntryService.updateExit(vehicleNumber, exitGate);
    // }
    @PutMapping("/exit/{vehicleNumber}/{exitGate}")
    public ResponseEntity<VehicleEntry> markVehicleExit(@PathVariable String vehicleNumber,
            @PathVariable int exitGate) {
        VehicleEntry updatedEntry = vehicleEntryService.updateExit(vehicleNumber, exitGate);

        webSocketSender.send("/topic/vehicle-exit", updatedEntry);

        return ResponseEntity.ok(updatedEntry);
    }

    // ✅ Get all vehicle entries
    @GetMapping("/all")
    public List<VehicleEntry> getAllVehicleEntries() {
        return vehicleEntryService.getAllEntries();
    }

    // ✅ Get all rows for a specific vehicle number
    @GetMapping("/{vehicleNumber}")
    public List<VehicleEntry> getEntriesByVehicleNumber(@PathVariable String vehicleNumber) {
        return vehicleEntryService.getEntriesByVehicleNumber(vehicleNumber);
    }

    // ✅ Get all rows for a specific date
    @GetMapping("/date/{date}")
    public List<VehicleEntry> getEntriesByDate(@PathVariable String date) {
        LocalDate parsedDate = LocalDate.parse(date); // Parse the date string to LocalDate
        return vehicleEntryService.getEntriesByDate(parsedDate);
    }

    // ✅ Get all rows for a specific vehicle type
    @GetMapping("/type/{vehicleType}")
    public List<VehicleEntry> getEntriesByVehicleType(@PathVariable String vehicleType) {
        return vehicleEntryService.getEntriesByVehicleType(vehicleType);
    }
}

// package com.bbau.vehicledetection.backend.controller;

// import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleEntry;
// import com.bbau.vehicledetection.backend.service.VehicleEntryService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// import java.time.LocalDate;
// import java.util.List;

// @RestController
// @RequestMapping("/api/vehicles")
// @CrossOrigin("*") // Allows requests from frontend
// public class VehicleEntryController {

// @Autowired
// private VehicleEntryService vehicleEntryService;

// // Handle vehicle entry or exit
// @PostMapping("/entry")
// public VehicleEntry registerVehicleEntry(@RequestBody VehicleEntry
// vehicleEntry) {
// System.out.println("Received vehicle entry: " + vehicleEntry);
// return vehicleEntryService.handleVehicleEntryOrExit(vehicleEntry);
// }

// // Get list of vehicles currently inside
// @GetMapping("/inside")
// public List<VehicleEntry> getVehiclesInside() {
// return vehicleEntryService.getAllActiveVehicles();
// }

// // Mark vehicle exit
// @PutMapping("/exit/{vehicleNumber}/{exitGate}")
// public VehicleEntry markVehicleExit(@PathVariable String vehicleNumber,
// @PathVariable int exitGate) {
// return vehicleEntryService.updateExit(vehicleNumber, exitGate);
// }

// // Get all vehicle entries
// @GetMapping("/all")
// public List<VehicleEntry> getAllVehicleEntries() {
// return vehicleEntryService.getAllEntries();
// }

// // Get all rows for a specific vehicle number
// @GetMapping("/{vehicleNumber}")
// public List<VehicleEntry> getEntriesByVehicleNumber(@PathVariable String
// vehicleNumber) {
// return vehicleEntryService.getEntriesByVehicleNumber(vehicleNumber);
// }

// // Get all rows for a specific date
// @GetMapping("/date/{date}")
// public List<VehicleEntry> getEntriesByDate(@PathVariable String date) {
// LocalDate parsedDate = LocalDate.parse(date); // Parse the date string to
// LocalDate
// return vehicleEntryService.getEntriesByDate(parsedDate);
// }

// // Get all rows for a specific vehicle type
// @GetMapping("/type/{vehicleType}")
// public List<VehicleEntry> getEntriesByVehicleType(@PathVariable String
// vehicleType) {
// return vehicleEntryService.getEntriesByVehicleType(vehicleType);
// }

// }
