package android1601.itstep.org.kidsgame.program.db_utility;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android1601.itstep.org.kidsgame.program.activity.MainActivity;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import android1601.itstep.org.kidsgame.program.data.GiftsSection;
import android1601.itstep.org.kidsgame.program.data.Gifts_Table;

/**
 * Created by roman on 13.03.2017.
 */

public class DBHelper{
    public static final String TAG = DBHelper.class.getName();
    private static DBHelper instance;

    public DBHelper() {

    }

    public static DBHelper getInstance(){
        if (instance==null)instance = new DBHelper();
        return instance;
    }


    public static List <Gifts>getToys(){
        return SQLite.select()
                .from(Gifts.class)
                .queryList();
    }


    public static List<Gifts> getall(){
        int section = MainActivity.positionCategory;
        return SQLite.select().
                from(Gifts.class).
                where(Gifts_Table.objectSection_id.eq(section)).
                queryList();
    }

    public static List<GiftsSection>getSections(){
        return SQLite.select().
                from(GiftsSection.class).
                queryList();
    }


    public static List<Gifts> getGiftsBySection(int section){
        return SQLite.select().
                from(Gifts.class).
                where(Gifts_Table.objectSection_id.eq(section)).
                queryList();
    }


    public static List<Gifts> getUnlockedBySection(){
        int section = 0;
        return SQLite.select().
                from(Gifts.class).
                where(Gifts_Table.unlock.eq(true)).and(Gifts_Table.objectSection_id.eq(section)).
                queryList();
    }

    public static List<Gifts>getRandomFourItems(){
        List<Gifts>container = getUnlockedBySection();
        List<Gifts> rndObjectses = new ArrayList<>();
        int size = getUnlockedBySection().size();
        for (int i = 0; i <4 ; i++) {
            int rndId = new Random().nextInt(size);
            rndObjectses.add(container.get(rndId));
            container.remove(rndId);
            size--;
        }
        return rndObjectses;
    }


    public static List<Gifts> getLocked(){
        int section = MainActivity.positionCategory;
        return SQLite.select().
                from(Gifts.class).
                where(Gifts_Table.unlock.eq(false)).and(Gifts_Table.objectSection_id.eq(section))
                .queryList();
    }

}
