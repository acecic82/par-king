package com.parking.api.adapter.out

import com.parking.api.application.port.out.SaveParkingLotPort
import com.parking.domain.entity.ParkingLot
import com.parking.jpa.entity.ParkingLotJpaEntity
import com.parking.jpa.repositories.ParkingLotJpaRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ParkingLotCommandAdapter(
    private val parkingLotJpaRepository: ParkingLotJpaRepository
): SaveParkingLotPort {
    override fun save(parkingLot: ParkingLot): ParkingLot {
        return parkingLotJpaRepository.save(ParkingLotJpaEntity.from(parkingLot, null)).to()
    }

    override fun deleteUpdate(parkingLot: ParkingLot, deletedAt: LocalDateTime) {
        parkingLotJpaRepository.save(ParkingLotJpaEntity.from(parkingLot, deletedAt))
    }
}