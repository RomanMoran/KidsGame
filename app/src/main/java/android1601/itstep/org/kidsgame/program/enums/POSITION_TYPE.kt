package android1601.itstep.org.kidsgame.program.enums

/**
 * Created by roman on 03.04.2017.
 */

enum class POSITION_TYPE private constructor(val position: Int) {
    WATER(3), AIR(2), GROUND(1), UNKNOWN(0);


    companion object {

        fun fromPositions(value: Int): POSITION_TYPE {
            for (type in POSITION_TYPE.values()) {
                if (type.position == value) {
                    return type
                }
            }
            return UNKNOWN
        }
    }
}
