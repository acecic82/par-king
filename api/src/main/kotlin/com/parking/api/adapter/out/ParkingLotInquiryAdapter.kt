package com.parking.api.adapter.out

import com.parking.api.application.port.out.FindParkingLotPort
import com.parking.api.application.vo.ParkingLotLocationVO
import com.parking.domain.entity.ParkingLot
import com.parking.jpa.repositories.ParkingLotRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import kotlin.jvm.optionals.getOrNull

@Component
class ParkingLotInquiryAdapter(
    private val parkingLotRepository: ParkingLotRepository
): FindParkingLotPort {

    override fun findById(parkingLotId: Long): ParkingLot? {
        val parkingLotEntity = parkingLotRepository.findById(parkingLotId).getOrNull()

        return parkingLotEntity?.to()
    }

    override fun findAllByMemberId(memberId: Long, page: Pageable): Page<ParkingLot> {
        return parkingLotRepository.findAllByMemberId(memberId, page).map {
            it.to()
        }
    }

    override fun findAllByLocation(location: ParkingLotLocationVO, page: Pageable): Page<ParkingLot> {
        return parkingLotRepository.findAllByLocation(location.cityName, location.guName, page).map {
            it.to()
        }
    }
}