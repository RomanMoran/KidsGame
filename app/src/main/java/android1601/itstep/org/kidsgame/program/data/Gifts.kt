package android1601.itstep.org.kidsgame.program.data

import android.os.Parcel
import android.os.Parcelable
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.KidsApplication
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.db_utility.AppDatabase
import android1601.itstep.org.kidsgame.program.enums.POSITION_TYPE
import android1601.itstep.org.kidsgame.program.enums.SECTIONS_TYPE
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * Created by roman on 13.03.2017.
 */


@Table(database = AppDatabase::class)
class Gifts constructor() : BaseModel(), Parcelable {

    @PrimaryKey(autoincrement = true)
    var id: Long = 0

    @Column
    var isUnlock: Boolean = false
    @Column
    var resName: String? = null

    @Column
    var positionType: Int = 0

    @Column
    var objectSectionId: Int = 0

    val imageResId = Utility.getDrawableResourceIdByName(resName)

    private var nameResId: Int = 0
    private var voiceResId: Int = 0
    private var soundResId: Int = 0

    val positionEnum: POSITION_TYPE
        get() = POSITION_TYPE.fromPositions(positionType)

    val sectionType: SECTIONS_TYPE
        get() = SECTIONS_TYPE.sections_type(objectSectionId)

    val silhouetteResName: String
        get() = resName!! + "_inactive"

    val silhouetteResId = Utility.getDrawableResourceIdByName(silhouetteResName)

    val textId: Int
        get() {
            if (nameResId == 0)
                nameResId = if (resName != null) Utility.getStringResourceIdByName(resName!!) else 0
            return nameResId
        }


    val voiceResName: String
        get() = resName + "_voice" + R.string.sound_locale

    /*    public int getVoiceRawId(){

        return Utility.getRawIdByName(getVoiceResName());
    }*/

    val soundResName: String
        get() = resName!! + "_sound"

    val voiceRawId: Int
        get() {
            val context = KidsApplication.instance
            if (voiceResId == 0)
                voiceResId = if (resName != null) Utility.getRawIdByName(resName + "_" + context!!.getString(R.string.sound_locale)) else 0
            return voiceResId
        }

    val soundRawId: Int
        get() {
            val context = KidsApplication.instance
            if (soundResId == 0)
                soundResId = if (resName != null) Utility.getRawIdByName(resName!! + "_sound") else 0
            return soundResId
        }

    constructor(`in`: Parcel) : this() {
        id = `in`.readLong()
        isUnlock = `in`.readByte().toInt() != 0
        resName = `in`.readString()
        positionType = `in`.readInt()
        objectSectionId = `in`.readInt()

    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeByte((if (isUnlock) 1 else 0).toByte())
        dest.writeString(resName)
        dest.writeInt(positionType)
        dest.writeInt(objectSectionId)
    }


    companion object CREATOR : Parcelable.Creator<Gifts> {
        override fun createFromParcel(parcel: Parcel): Gifts {
            return Gifts(parcel)
        }

        override fun newArray(size: Int): Array<Gifts?> {
            return arrayOfNulls(size)
        }
    }

}
