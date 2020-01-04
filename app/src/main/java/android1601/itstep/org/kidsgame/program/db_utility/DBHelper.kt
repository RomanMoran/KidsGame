package android1601.itstep.org.kidsgame.program.db_utility

import com.raizlabs.android.dbflow.sql.language.SQLite

import java.util.ArrayList
import java.util.Random

import android1601.itstep.org.kidsgame.program.activity.MainActivity
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.data.GiftsSection
import android1601.itstep.org.kidsgame.program.data.Gifts_Table

/**
 * Created by roman on 13.03.2017.
 */

class DBHelper {
    companion object {
        val TAG = DBHelper::class.java.name
        private var instance: DBHelper? = null

        fun getInstance(): DBHelper {
            if (instance == null) instance = DBHelper()
            return instance as DBHelper
        }


        val toys: List<Gifts>
            get() = SQLite.select()
                    .from(Gifts::class.java)
                    .queryList()


        fun getall(): List<Gifts> {
            val section = MainActivity.positionCategory
            return SQLite.select().from(Gifts::class.java).where(Gifts_Table.objectSection_id.eq(section)).queryList()
        }

        val sections: List<GiftsSection>
            get() = SQLite.select().from(GiftsSection::class.java).queryList()


        fun getGiftsBySection(section: Int): List<Gifts> {
            return SQLite.select().from(Gifts::class.java).where(Gifts_Table.objectSection_id.eq(section)).queryList()
        }


        val unlockedBySection: MutableList<Gifts>
            get() {
                val section = MainActivity.positionCategory
                return SQLite.select().from(Gifts::class.java).where(Gifts_Table.unlock.eq(true)).and(Gifts_Table.objectSection_id.eq(section)).queryList()
            }

        val randomFourItems: List<Gifts>
            get() {
                val container = unlockedBySection
                val rndObjectses = ArrayList<Gifts>()
                var size = unlockedBySection.size
                for (i in 0..3) {
                    val rndId = Random().nextInt(size)
                    rndObjectses.add(container[rndId])
                    container.removeAt(rndId)
                    size--
                }
                return rndObjectses
            }


        val locked: List<Gifts>
            get() {
                val section = MainActivity.positionCategory
                return SQLite.select().from(Gifts::class.java).where(Gifts_Table.unlock.eq(false)).and(Gifts_Table.objectSection_id.eq(section))
                        .queryList()
            }
    }

}
