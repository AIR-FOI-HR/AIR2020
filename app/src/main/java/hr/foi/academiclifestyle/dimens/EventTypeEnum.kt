package hr.foi.academiclifestyle.dimens

enum class EventTypeEnum(val eventName: String, val eventTypeId: Int) {
    LECTURE("Lectures", 3),
    LAB("Labs", 2),
    SEMINAR("Seminars", 1);

    companion object {
        fun fromId(id: Int): EventTypeEnum? = values().find { it.eventTypeId == id }
    }
}