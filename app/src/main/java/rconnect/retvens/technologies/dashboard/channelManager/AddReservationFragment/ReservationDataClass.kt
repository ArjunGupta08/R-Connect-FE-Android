package rconnect.retvens.technologies.dashboard.channelManager.AddReservationFragment


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

data class BookingTypeId(
    val bookingTypeId: String
)

data class BookingSourceId(
    val bookingSourceId: String
)

data class BarRateReservation(
    val bookingTypeId: List<BookingTypeId>,
    val bookingSourceId: List<BookingSourceId>
)

data class RoomTypeId(
    var roomTypeId: String
)

data class RatePlanId(
    val ratePlanId: String
)

data class Adults(
    var adults: String
)

data class Childs(
    var childs: String
)

data class Charge(
    var charge: String
)

data class ExtraAdult(
    val extraAdult: String
)

data class ExtraInclusion(
    val extraInclusion: String
)

data class ExtraChild(
    val extraChild: String
)

data class RoomDetails(
    val roomTypeId: List<RoomTypeId>,
    val ratePlan: List<RatePlanId>,
    val adults: List<Adults>,
    val childs: List<Childs>,
    val charge: List<Charge>,
    val extraAdult: List<ExtraAdult>,
    val extraInclusion: List<ExtraInclusion>,
    val extraChild: List<ExtraChild>
)

data class SpecialRemark(
    val specialRemark: String
)

data class InternalNote(
    val internalNote: String
)

data class Remark(
    val specialRemark: List<SpecialRemark>,
    val internalNote: List<InternalNote>
)

data class RoomCharges(
    val roomCharges: String
)

data class Extras(
    val extras: String
)

data class Taxes(
    val taxes: String
)

data class From(
    val from: String
)

data class To(
    val to: String
)

data class GrandTotal(
    val grandTotal: String
)

data class ReservationSummary(
    val roomCharges: List<RoomCharges>,
    val extras: List<Extras>,
    val taxes: List<Taxes>,
    val from: List<From>,
    val to: List<To>,
    val grandTotal: List<GrandTotal>
)

data class BillTo(
    val billTo: String
)

data class PaymentNote(
    val paymentNote: String
)

data class PaymentDetails(
    val billTo: List<BillTo>,
    val paymentNote: List<PaymentNote>
)

data class CardDetails(
    val nameOnCard: String,
    val cardNumber: String,
    val cvv: String,
    val expiryDate: String
)

data class TaskTitle(
    val taskTitle: String
)

data class Schedule(
    val schedule: String
)

data class Description(
    val description: String
)

data class CreateTask(
    val taskTitle: List<TaskTitle>,
    val schedule: List<Schedule>,
    val description: List<Description>
)

data class ReservationDataClass(
    val userId: String,
    val propertyId: String,
    val checkInDate: String,
    val checkOutDate: String,
    val nightCount: String,
    val createdOn: String,
    val rateTypeId: String,
    val ratePlanName: String,
    val shortCode: String,
    val minimumNights: String,
    val maximumNights: String,
    val packageRateAdjustment: String,
    val inclusionTotal: String,
    val ratePlanTotal: String,
    val ratePlanInclusion: String,
    val guestInfo: List<GuestInfo>,
    val barRateReservation: List<BarRateReservation>,
    val roomDetails: List<RoomDetails>,
    val remark: List<Remark>,
    val reservationSummary: List<ReservationSummary>,
    val applyDiscount: String,
    val paymentDetails: List<PaymentDetails>,
    val cardDetails: List<CardDetails>,
    val createTask: List<CreateTask>
)