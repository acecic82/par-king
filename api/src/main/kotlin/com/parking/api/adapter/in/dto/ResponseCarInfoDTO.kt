package com.parking.api.adapter.`in`.dto

import com.parking.api.application.vo.ResponseCarInfoVO
import com.parking.domain.entity.DibsOnParkingLotStatus
import java.time.LocalDateTime

data class ResponseCarInfoDTO(
    val carId: Long? = null,
    var carNumber: String,
    val dibsOnParkingLotName: String? = null,
    var dibsOnParkingLotStatus: DibsOnParkingLotStatus? = null,
    var startDibsOnTime: LocalDateTime? = null
) {
    companion object {
        fun from(carInfo: ResponseCarInfoVO) = ResponseCarInfoDTO(
            carId = carInfo.carId,
            carNumber = carInfo.carNumber,
            dibsOnParkingLotName = carInfo.dibsOnParkingLotName,
            dibsOnParkingLotStatus = carInfo.dibsOnParkingLotStatus,
            startDibsOnTime = carInfo.startDibsOnTime
        )
    }
}
