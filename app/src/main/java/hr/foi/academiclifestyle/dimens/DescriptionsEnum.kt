package hr.foi.academiclifestyle.dimens

enum class DescriptionsEnum(val description: String) {
    WINDOW_OPEN("It appears your window is completely open and there is plenty air circulation."),
    WINDOW_CLOSED("It appears your window is closed and there is no air circulation."),
    WINDOW_TILTED("It appears your window is lightly open and there is enough air circulation."),

    AIR_QUALITY_NORMAL("It appears the air quality in this room is good and the amount of oxygen is normal."),
    AIR_QUALITY_LOW("Looking at the overall quality of air in this room, it is advised to let some fresh air in."),

    TARDINESS_LATE("Looking at your class arrival times, it has been observed that you tend to arrive to class just in time the academic quarter expires or a couple of minutes late."),
    TARDINESS_EARLY("Looking at your class arrival times, it has been observed that you tend to arrive to class before the academic quarter or a couple of minutes early."),
    TARDINESS_NORMAL("Looking at your class arrival times, it has been observed that you tend to arrive to class in time."),

    NO_DATA("No data");
}