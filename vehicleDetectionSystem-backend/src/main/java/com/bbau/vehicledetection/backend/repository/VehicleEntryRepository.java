package com.bbau.vehicledetection.backend.repository;

import com.bbau.vehicledetection.backend.entity.VehicleEntry;
import com.bbau.vehicledetection.backend.entity.VehicleStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleEntryRepository extends JpaRepository<VehicleEntry, Integer> {

    // Find a vehicle by its number
    List<VehicleEntry> findByVehicleNumber(String vehicleNumber);

    // Get all vehicles still inside (not exited yet)
    List<VehicleEntry> findByStatus(VehicleStatus status);
}
