package android1601.itstep.org.kidsgame.program.enums;

/**
 * Created by roman on 23.04.2017.
 */

public enum SECTIONS_TYPE {
    TOYS(1),CLOTHES(2),UNKNOWN(0);
    private int section;

    SECTIONS_TYPE(int section){this.section = section;}

    public static SECTIONS_TYPE sections_type(int value){
        for (SECTIONS_TYPE type: SECTIONS_TYPE.values()) {
            if (type.getPosition()==value){
                return type;
            }
        }
        return UNKNOWN;
    }

    public int getPosition() {
        return section;
    }
}
