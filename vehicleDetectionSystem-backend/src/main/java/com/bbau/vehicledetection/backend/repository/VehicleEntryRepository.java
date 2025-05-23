package com.bbau.vehicledetection.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleEntry;
import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleStatus;
import com.bbau.vehicledetection.backend.entity.VehicleEntry.VehicleType;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Integer> {

    // Find a vehicle by its number
    List<VehicleEntry> findByVehicleNumber(String vehicleNumber);

    // Get all vehicles still inside (not exited yet)
    List<VehicleEntry> findByStatus(VehicleStatus status);

    // Get all rows for a specific vehicle number
    List<VehicleEntry> findAllByVehicleNumber(String vehicleNumber);

    // Get all rows for a specific date
    List<VehicleEntry> findAllByDate(LocalDate date);

    // Get all rows for a specific vehicle type
    List<VehicleEntry> findAllByVehicleType(VehicleType vehicleType);

}
