package com.parking.domain.entity

import java.math.BigDecimal

data class ParkingLot(
    var parkingLotId: Long? = null,
    //주차장을 등록한 주인의 MemberId
    val memberId: Long,
    var parkingLotInfo: ParkingLotInfo,
    var parkingLotLocation: ParkingLotLocation
) {
    fun modifyParkingLot(parkingLotInfo: ParkingLotInfo, parkingLotLocation: ParkingLotLocation) {
        this.parkingLotInfo = parkingLotInfo
        this.parkingLotLocation = parkingLotLocation
    }

    fun getParkingLotName(): String {
        return parkingLotInfo.name
    }

    fun occupyParkingLot(): Boolean {
        if (parkingLotInfo.totalSpace <= parkingLotInfo.occupiedSpace) return false

        parkingLotInfo.occupiedSpace++

        return true
    }
}

data class ParkingLotInfo(
    val name: String,
    val fullAddress: String?,
    val totalSpace: Long,
    var occupiedSpace: Long = 0L,
    //1시간 기준 기본 요금
    val cost: BigDecimal = BigDecimal.ZERO,
    //1시간이 지난 후 10분마다 추가되는 요금
    val extraCost: BigDecimal = BigDecimal.ZERO
)

data class ParkingLotLocation(
    val cityName: String,
    val guName: String
)
