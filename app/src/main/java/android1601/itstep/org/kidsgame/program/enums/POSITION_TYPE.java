package android1601.itstep.org.kidsgame.program.enums;

/**
 * Created by roman on 03.04.2017.
 */

public enum POSITION_TYPE {
    WATER(3),AIR(2),GROUND(1),UNKNOWN(0);
    private int position;

    POSITION_TYPE(int position) {
        this.position = position;
    }

    public static POSITION_TYPE fromPositions(int value){
        for (POSITION_TYPE type: POSITION_TYPE.values()) {
            if (type.getPosition()==value){
                return type;
            }
        }
        return UNKNOWN;
    }

    public int getPosition() {
        return position;
    }
}
