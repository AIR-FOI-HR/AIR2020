package hr.foi.academiclifestyle.dimens

import java.lang.reflect.Type

enum class CoursesEnum(val programName: String, val programId: Int) {
    IPS("Informacijski i poslovni sustavi", 1),
    EP("Ekonomika poduzetništva", 3),
    PITUP("Primjena informacijske tehnologije u poslovanju", 4),

    IPI("Informacijsko i programsko inženjerstvo", 2),
    OPS("Organizacija poslovnih sustava", 5),
    BPBZ("Baze podataka i baze znanja", 6),
    IO("Informatika u obrazovanju", 7);

    companion object {
        fun fromId(id: Int): CoursesEnum? = values().find { it.programId == id }
    }
}