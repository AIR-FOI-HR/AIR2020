package hr.foi.academiclifestyle.dimens

enum class WindowStatusEnum(val status: String, val value: Int) {
    OPEN("Open", 1),
    CLOSED("Closed", 2),
    TILTED("Tilted", 3);

    companion object {
        fun fromValue(value: Int): WindowStatusEnum? = WindowStatusEnum.values().find { it.value == value }
    }
}