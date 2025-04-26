package com.bbau.vehicledetection.backend.service;

import com.bbau.vehicledetection.backend.entity.BlockedVehicle.BlockedVehicle;
import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleEntry;
import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleStatus;
import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleType;
import com.bbau.vehicledetection.backend.repository.BlockedVehicleRepository;
import com.bbau.vehicledetection.backend.repository.VehicleEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleEntryService {

    @Autowired
    private VehicleEntryRepository vehicleEntryRepository;

    @Autowired
    private BlockedVehicleRepository blockedVehicleRepository;

    @Autowired
    private NotificationService notificationService;

    // ✅ Handle vehicle entry or exit
    public void handleVehicleEntry(VehicleEntry vehicleEntry) {
        // Check if the vehicle is in the blocked list
        Optional<BlockedVehicle> blockedVehicle = blockedVehicleRepository.findByVehicleNumberAndStatus(
            vehicleEntry.getVehicleNumber(), BlockedVehicle.Status.BLOCKED
        );
    
        if (blockedVehicle.isPresent()) {
            // Send notification to React frontend
            notificationService.sendBlockedVehicleNotification(blockedVehicle.get());
        } else {
            // Check if the vehicle already has an active entry (status = IN)
            List<VehicleEntry> activeEntries = vehicleEntryRepository.findByVehicleNumber(vehicleEntry.getVehicleNumber());
            for (VehicleEntry activeEntry : activeEntries) {
                if (activeEntry.getStatus() == VehicleStatus.IN) {
                    // Mark the active entry as exited
                    activeEntry.setExitTime(LocalDateTime.now());
                    activeEntry.setExitGate(vehicleEntry.getEntryGate()); // Set the exit gate
                    activeEntry.setStatus(VehicleStatus.OUT);
                    vehicleEntryRepository.save(activeEntry);
                    return; // Exit after marking the previous entry as OUT
                }
            }
    
            // If no active entry exists, create a new entry
            vehicleEntry.setEntryTime(LocalDateTime.now());
            vehicleEntry.setDate(LocalDate.now());
            vehicleEntry.setEntryGate(vehicleEntry.getEntryGate()); // Set the entry gate
            vehicleEntry.setStatus(VehicleStatus.IN);
            vehicleEntryRepository.save(vehicleEntry);
        }
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

    // ✅ Get all rows for a specific vehicle number
    public List<VehicleEntry> getEntriesByVehicleNumber(String vehicleNumber) {
        return vehicleEntryRepository.findAllByVehicleNumber(vehicleNumber);
    }

    // ✅ Get all rows for a specific date
    public List<VehicleEntry> getEntriesByDate(LocalDate date) {
        return vehicleEntryRepository.findAllByDate(date);
    }

    // ✅ Get all rows for a specific vehicle type
    public List<VehicleEntry> getEntriesByVehicleType(String vehicleType) {
        VehicleType type = VehicleType.valueOf(vehicleType.toUpperCase()); // Convert String to ENUM
        return vehicleEntryRepository.findAllByVehicleType(type);
    }
}







// package com.bbau.vehicledetection.backend.service;

// import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleEntry;
// import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleStatus;
// import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleType;
// import com.bbau.vehicledetection.backend.repository.VehicleEntryRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import java.time.LocalDateTime;
// import java.time.LocalDate;
// import java.util.List;


// @Service
// public class VehicleEntryService {

//     @Autowired
//     private VehicleEntryRepository vehicleEntryRepository;

//     // ✅ Handle vehicle entry or exit
//     public VehicleEntry handleVehicleEntryOrExit(VehicleEntry vehicleEntry) {
//         // Get today's date
//         LocalDate today = LocalDate.now();

//         // Check if the vehicle has an active entry for today
//         List<VehicleEntry> entries = vehicleEntryRepository.findByVehicleNumber(vehicleEntry.getVehicleNumber());
//         for (VehicleEntry entry : entries) {
//             if (entry.getDate().equals(today) && entry.getStatus() == VehicleStatus.IN) {
//                 // If the vehicle is already inside, mark it as exited
//                 entry.setExitTime(LocalDateTime.now());
//                 entry.setExitGate(vehicleEntry.getEntryGate()); // Use the current gate as the exit gate
//                 entry.setStatus(VehicleStatus.OUT);
//                 return vehicleEntryRepository.save(entry);
//             }
//         }

//         // If no active entry exists for today, register a new entry
//         vehicleEntry.setEntryTime(LocalDateTime.now());
//         vehicleEntry.setDate(today);
//         vehicleEntry.setStatus(VehicleStatus.IN);
//         return vehicleEntryRepository.save(vehicleEntry);
//     }

//     // ✅ Get all active vehicles
//     public List<VehicleEntry> getAllActiveVehicles() {
//         return vehicleEntryRepository.findByStatus(VehicleStatus.IN);
//     }

//     // ✅ Mark vehicle exit
//     public VehicleEntry updateExit(String vehicleNumber, int exitGate) {
//         List<VehicleEntry> entries = vehicleEntryRepository.findByVehicleNumber(vehicleNumber);
//         for (VehicleEntry entry : entries) {
//             if (entry.getStatus() == VehicleStatus.IN) {
//                 entry.setExitTime(LocalDateTime.now());
//                 entry.setExitGate(exitGate);
//                 entry.setStatus(VehicleStatus.OUT);
//                 return vehicleEntryRepository.save(entry);
//             }
//         }
//         throw new RuntimeException("Vehicle not found or already exited");
//     }

//     // ✅ Get all vehicle entries
//     public List<VehicleEntry> getAllEntries() {
//         return vehicleEntryRepository.findAll();
//     }

//     // ✅ Get all rows for a specific vehicle number
//     public List<VehicleEntry> getEntriesByVehicleNumber(String vehicleNumber) {
//         return vehicleEntryRepository.findAllByVehicleNumber(vehicleNumber);
//     }

//     // ✅ Get all rows for a specific date
//     public List<VehicleEntry> getEntriesByDate(LocalDate date) {
//         return vehicleEntryRepository.findAllByDate(date);

    
// }

// // ✅ Get all rows for a specific vehicle type
// public List<VehicleEntry> getEntriesByVehicleType(String vehicleType) {
//         VehicleType type = VehicleType.valueOf(vehicleType.toUpperCase()); // Convert String to ENUM
//         return vehicleEntryRepository.findAllByVehicleType(type);
//     }


// }


