package rconnect.retvens.technologies.onboarding.authentication

data class DesignationDataClass(
    val data : List<Designation>
) {
}

data class Designation(
    val designationId:String,
    val designation:String
)