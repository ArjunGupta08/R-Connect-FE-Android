package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment


data class ReservationDataClass(
    val userId: String,
    val propertyId: String,
    val checkInDate: String,
    val checkOutDate: String,
    val nightCount: String,
    // Additional properties commented out
    // Uncomment them as needed
    // val shortCode: String,
    // val minimumNights: String,
    // val maximumNights: String,
    // val inclusionTotal: String,
    // val ratePlanTotal: String,
    // val ratePlanInclusion: String,
    val guestInfo: List<GuestInfo>,
    val barRateReservation: List<BarRateReservation>,
    val roomDetails: List<RoomDetail>,
    val reservationSummary: List<ReservationSummary>,
    val applyDiscount: String,
    val paymentDetails: List<PaymentDetail>,
    val cardDetails: List<CardDetail>
)

data class GuestInfo(
    val guestName: String,
    val phoneNumber: String,
    val emailAddress: String,
    val addressLine1: String,
    val addressLine2: String,
    val country: String,
    val state: String,
    val city: String,
    val pincode: String,
    val c_form: List<CForm>
)

data class CForm(
    val address: String,
    val state: String,
    val city: String,
    val pinCode: String,
    val arrivedFrom: String,
    val dateOfArrival: String,
    val passportNo: String,
    val placeOfIssue: String,
    val issueDate: String,
    val expiryDate: String,
    val visaNo: String,
    val visaType: String,
    val whetherEmployedInIndia: String,
    val guardianName: String,
    val age: String,
    val purposeOfVisit: String,
    val nextDestinationPlace: String,
    val nextDestinationState: String,
    val nextDestinationcity: String,
    val contactNo: String,
    val parmanentResidentContactNo: String,
    val mobileNo: String,
    val parmanentResidentMobileNo: String,
    val remark: String
)

data class BarRateReservation(
    val bookingTypeId: String,
    val rateType: String,
    val bookingSourceId: String
)

data class RoomDetail(
    var roomTypeId: String,
    val ratePlanId: String,
    var adults: String,
    var childs: String,
    var charge: String,
    var extraAdult: String,
    var extraChild: String,
    val extraInclusionId: List<String>,
    val remark: List<Remark>,
    val createTask: List<CreateTask>
)

data class Remark(
    val specialRemark: String,
    val internalNote: String
)

data class CreateTask(
    val taskTitle: String,
    val schedule: String,
    val description: String
)

data class ReservationSummary(
    val roomCharges: String,
    val extras: String,
    val taxes: String,
    val from: String,
    val to: String,
    val grandTotal: String
)

data class PaymentDetail(
    val billTo: String,
    val paymentNote: String
)

data class CardDetail(
    val nameOnCard: String,
    val cardNumber: String,
    val cvv: String,
    val expiryDate: String
)

data class RoomDetailCharges(
    var roomTypeId: String,
    val ratePlanId: String,
    var adults: String,
    var childs: String,
    var charge: String,
    var extraAdult: String,
    var extraChild: String,
    val extraInclusion:String,
    val remark: List<Remark>,
    val createTask: List<CreateTask>
)

data class BookingResponse(
    val message:String
)