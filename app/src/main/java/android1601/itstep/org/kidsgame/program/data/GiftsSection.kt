package android1601.itstep.org.kidsgame.program.data

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.KidsApplication
import android1601.itstep.org.kidsgame.program.db_utility.AppDatabase
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel

/**
 * Created by roman on 23.04.2017.
 */

@Table(database = AppDatabase::class)
class GiftsSection : BaseModel() {
    @PrimaryKey(autoincrement = true)
    var id: Long = 0

    @Column
    var titleResName: String? = null

    private val titleResNameLang: String? = null

    fun getTitleLang(sections_type: Int): String? {
        if (titleResNameLang == null) {
            val context = KidsApplication.instance
            when (sections_type) {
                2 -> return context!!.getString(R.string.furniture)
                1 -> return context!!.getString(R.string.clothes)
                0 -> return context!!.getString(R.string.toys)
            }
        }
        return null
    }

}
