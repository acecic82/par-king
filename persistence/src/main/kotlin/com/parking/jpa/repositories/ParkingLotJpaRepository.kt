package com.parking.jpa.repositories

import com.parking.jpa.entity.ParkingLotJpaEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ParkingLotJpaRepository : JpaRepository<ParkingLotJpaEntity, Long>