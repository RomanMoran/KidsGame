package android1601.itstep.org.kidsgame.program.db_utility

import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.Database

/**
 * Created by roman on 13.03.2017.
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION, insertConflict = ConflictAction.IGNORE, updateConflict = ConflictAction.REPLACE)
object AppDatabase {
    const val NAME = "Gifts"

    const val VERSION = 1
}
