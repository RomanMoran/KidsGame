package android1601.itstep.org.kidsgame.program.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.franmontiel.localechanger.LocaleChanger;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.KidsKotlinApplication;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.db_utility.AppDatabase;
import android1601.itstep.org.kidsgame.program.enums.POSITION_TYPE;
import android1601.itstep.org.kidsgame.program.enums.SECTIONS_TYPE;

/**
 * Created by roman on 13.03.2017.
 */

@Table(database = AppDatabase.class)
public class Gifts extends BaseModel implements Parcelable {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private boolean unlock;
    @Column
    private String resName;

    @Column
    private int positionType;

    @Column
    private int objectSection_id;

    private int imageResId;
    private int silhouetteResId;
    private int nameResId;
    private int voiceResId;
    private int soundResId;

    protected Gifts(Parcel in) {
        id = in.readLong();
        unlock = in.readByte() != 0;
        resName = in.readString();
        positionType = in.readInt();
        objectSection_id = in.readInt();
    }

    public static final Creator<Gifts> CREATOR = new Creator<Gifts>() {
        @Override
        public Gifts createFromParcel(Parcel in) {
            return new Gifts(in);
        }

        @Override
        public Gifts[] newArray(int size) {
            return new Gifts[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeByte((byte) (unlock ? 1 : 0));
        dest.writeString(resName);
        dest.writeInt(positionType);
        dest.writeInt(objectSection_id);
    }

    public Gifts() {
    }

    public long getId() {
        return id;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public String getResName() {
        return resName;
    }

    public int getObjectSection_id() {
        return objectSection_id;
    }

    public int getPositionType() {
        return positionType;
    }

    public POSITION_TYPE getPositionEnum(){
        return POSITION_TYPE.fromPositions(positionType);
    }

    public SECTIONS_TYPE getSectionType(){
        return SECTIONS_TYPE.sections_type(objectSection_id);
    }

    public void setObjectSection_id(int objectSection_id) {
        this.objectSection_id = objectSection_id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }



    public int getImageResId(){
        return Utility.getDrawableResourceIdByName(resName);
    }
    public String getSilhouetteResName(){
        return resName+"_inactive";
    }

    public int getSilhouetteResId(){
        return Utility.getDrawableResourceIdByName(getSilhouetteResName());
    }

    public int getTextId(){
        if (nameResId==0)
            nameResId = resName != null? Utility.getStringResourceIdByName(resName) : 0;
        return nameResId;
    }


    public String getVoiceResName(){
        return resName+"_voice"+R.string.sound_locale;
    }

/*    public int getVoiceRawId(){

        return Utility.getRawIdByName(getVoiceResName());
    }*/

    public String getSoundResName(){
        return resName+"_sound";
    }

    public int getVoiceRawId(){
        Context context = KidsKotlinApplication.Companion.getInstance();
        String locale = LocaleChanger.getLocale().getLanguage();
        if (voiceResId == 0)
            voiceResId = resName!=null?Utility.getRawIdByName(resName+"_"+locale):0;
        return voiceResId;
    }

    public int getSoundRawId(){
        Context context = KidsKotlinApplication.Companion.getInstance();
        if (soundResId == 0)
            soundResId = resName!=null?Utility.getRawIdByName(resName+"_sound"):0;
        return soundResId;
    }

    public void setPositionType(int positionType) {
        this.positionType = positionType;
    }

}
