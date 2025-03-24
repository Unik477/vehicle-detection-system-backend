package com.bbau.vehicledetection.backend.service;

import com.bbau.vehicledetection.backend.entity.VehicleEntry;
import com.bbau.vehicledetection.backend.entity.VehicleStatus;
import com.bbau.vehicledetection.backend.repository.VehicleEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;


@Service
public class VehicleEntryService {

    @Autowired
    private VehicleEntryRepository vehicleEntryRepository;

    // ✅ Handle vehicle entry or exit
    public VehicleEntry handleVehicleEntryOrExit(VehicleEntry vehicleEntry) {
        // Get today's date
        LocalDate today = LocalDate.now();

        // Check if the vehicle has an active entry for today
        List<VehicleEntry> entries = vehicleEntryRepository.findByVehicleNumber(vehicleEntry.getVehicleNumber());
        for (VehicleEntry entry : entries) {
            if (entry.getDate().equals(today) && entry.getStatus() == VehicleStatus.IN) {
                // If the vehicle is already inside, mark it as exited
                entry.setExitTime(LocalDateTime.now());
                entry.setExitGate(vehicleEntry.getEntryGate()); // Use the current gate as the exit gate
                entry.setStatus(VehicleStatus.OUT);
                return vehicleEntryRepository.save(entry);
            }
        }

        // If no active entry exists for today, register a new entry
        vehicleEntry.setEntryTime(LocalDateTime.now());
        vehicleEntry.setDate(today);
        vehicleEntry.setStatus(VehicleStatus.IN);
        return vehicleEntryRepository.save(vehicleEntry);
    }

    // ✅ Get all active vehicles
    public List<VehicleEntry> getAllActiveVehicles() {
        return vehicleEntryRepository.findByStatus(VehicleStatus.IN);
    }

    // ✅ Mark vehicle exit
    public VehicleEntry updateExit(String vehicleNumber, int exitGate) {
        List<VehicleEntry> entries = vehicleEntryRepository.findByVehicleNumber(vehicleNumber);
        for (VehicleEntry entry : entries) {
            if (entry.getStatus() == VehicleStatus.IN) {
                entry.setExitTime(LocalDateTime.now());
                entry.setExitGate(exitGate);
                entry.setStatus(VehicleStatus.OUT);
                return vehicleEntryRepository.save(entry);
            }
        }
        throw new RuntimeException("Vehicle not found or already exited");
    }

    // ✅ Get all vehicle entries
    public List<VehicleEntry> getAllEntries() {
        return vehicleEntryRepository.findAll();
    }
}


