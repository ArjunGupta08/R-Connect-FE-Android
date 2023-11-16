package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment


data class ReservationDataClass(
    val userId: String,
    val propertyId: String,
    val checkInDate: String,
    val checkOutDate: String,
    val nightCount: String,
    val rateTypeId: String,
    val guestInfo: List<GuestInfo>,
    val barRateReservation: List<BarRateReservation>,
    val roomDetails: List<RoomDetail>,
    val remark: List<Remark>,
    val reservationSummary: List<ReservationSummary>,
    val applyDiscount: String,
    val paymentDetails: List<PaymentDetail>,
    val cardDetails: List<CardDetail>,
    val createTask: List<CreateTask>
)

data class GuestInfo(
    val guestId: String,
    val guestName: String,
    val phoneNumber: String,
    val emailAddress: String,
    val addressLine1: String,
    val addressLine2: String,
    val country: String,
    val state: String,
    val city: String,
    val pincode: String
)

data class BarRateReservation(
    val bookingTypeId: String,
    val bookingSourceId: String
)

data class RoomDetail(
    var roomTypeId: String = "",
    var ratePlan: String = "",
    var adults: String= "",
    var childs: String = "",
    var charge: String = "",
    var extraAdult: String = "",
    val extraInclusion: String = "",
    var extraChild: String = ""
)

data class Remark(
    val specialRemark: String,
    val internalNote: String
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

data class CreateTask(
    val taskTitle: String,
    val schedule: String,
    val description: String
)

data class BookingResponse(
    val message:String
)