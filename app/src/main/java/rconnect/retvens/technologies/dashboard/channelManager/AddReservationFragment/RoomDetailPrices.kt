package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment

data class RoomDetailPrices(
    var baseChildCount:Int = 0,
    var baseAdultCount:Int = 0,
    var pricePerExtraAdult:Double = 0.0,
    var pricePerExtraChild:Double = 0.0,
    var totalCharges:Double = 0.0,
    var totalPriceForExtraChild:Double = 0.0,
    var totalPriceForExtraAdult:Double = 0.0,
    var totalInclusions:ArrayList<Double>,
) {
}