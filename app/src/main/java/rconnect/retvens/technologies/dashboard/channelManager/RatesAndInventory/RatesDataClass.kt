package rconnect.retvens.technologies.dashboard.channelManager.RatesAndInventory

data class BaseRate(
    val date: String,
    val baseRate: String,
    val extraAdultRate: String,
    val extraChildRate: String,
    val stopSell: String,
    val COA: String,
    val COD: String,
    val minimumLOS: Int,
    val maximumLOS: Int
)

data class OTA(
    val otaId: String,
    val otaName: String,
    val otaLogo: String,
    val ratePlanId: String
)

data class BarRatePlan(
    val barRatePlanId: String,
    val ratePlanTotal: String,
    val extraAdultRate: String,
    val extraChildRate: String,
    val roomTypeId: String,
    val ratePlanName: String,
    val baseRates: List<BaseRate>,
    val OTA: List<OTA>
)

data class RatesDataClass(
    val data: List<BarRatePlan>,
    val statuscode: Int
)
