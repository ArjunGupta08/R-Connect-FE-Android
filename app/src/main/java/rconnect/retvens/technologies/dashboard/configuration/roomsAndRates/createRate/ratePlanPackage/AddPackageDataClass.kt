package rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.createRate.ratePlanPackage

import rconnect.retvens.technologies.dashboard.configuration.roomsAndRates.inclusions.GetInclusionsData

data class AddPackageDataClass(
    val userId : String,
    val propertyId : String,
    val rateType : String,
    val roomTypeId : String,
    val ratePlanId : String,
    val shortCode : String,
    val ratePlanInclusion : ArrayList<GetInclusionsData>,
    val ratePlanName : String,
    val minimumNights : String,
    val maximumNights : String,
    val packageRateAdjustment : ArrayList<PackageRateAdjustmentData>,
    val inclusionTotal : String,
    val ratePlanTotal : String,
)

data class PackageRateAdjustmentData(
    val adjustment : String,
    val percentage : String,
    val amount : String,
    val packageTotal : String,
)