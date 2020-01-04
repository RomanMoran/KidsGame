package android1601.itstep.org.kidsgame.program.enums

/**
 * Created by roman on 23.04.2017.
 */

enum class SECTIONS_TYPE private constructor(val position: Int) {
    TOYS(1), CLOTHES(2), UNKNOWN(0);


    companion object {

        fun sections_type(value: Int): SECTIONS_TYPE {
            for (type in SECTIONS_TYPE.values()) {
                if (type.position == value) {
                    return type
                }
            }
            return UNKNOWN
        }
    }
}
