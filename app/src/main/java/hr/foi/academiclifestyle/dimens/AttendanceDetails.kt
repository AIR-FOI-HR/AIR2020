package hr.foi.academiclifestyle.dimens

data class AttendanceDetails (
        val name: String,
        val eventType: String,
        val maxAttend: Int,
        val minAttend: Int,
        val userAttend: Int
)