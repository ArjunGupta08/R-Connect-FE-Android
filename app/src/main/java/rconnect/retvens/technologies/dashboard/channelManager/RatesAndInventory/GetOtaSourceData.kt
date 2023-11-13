package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

data class GetOtaSourceData(
    val data:ArrayList<OtaSource>
)


data class OtaSource(
    val propertyId:String,
    val otaId:String,
    val otaName:String
)
