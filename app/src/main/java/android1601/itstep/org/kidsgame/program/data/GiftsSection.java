package android1601.itstep.org.kidsgame.program.data;

import android.content.Context;

import com.bumptech.glide.util.Util;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.KidsApplication;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.db_utility.AppDatabase;
import android1601.itstep.org.kidsgame.program.enums.SECTIONS_TYPE;

import static android1601.itstep.org.kidsgame.program.enums.SECTIONS_TYPE.CLOTHES;
import static android1601.itstep.org.kidsgame.program.enums.SECTIONS_TYPE.TOYS;

/**
 * Created by roman on 23.04.2017.
 */

@Table(database = AppDatabase.class)
public class GiftsSection extends BaseModel {
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String titleResName;

    public long getId() {
        return id;
    }

    public String getTitleResName() {
        return titleResName;
    }

    private String titleResNameLang;

    public void setId(long id) {
        this.id = id;
    }

    public void setTitleResName(String titleResName) {
        this.titleResName = titleResName;
    }

    public String getTitleLang(int sections_type){
        if(titleResNameLang==null){
            Context context= KidsApplication.Companion.getInstance();
            switch (sections_type) {
                case 2:
                    return context.getString(R.string.furniture);
                case 1:
                    return context.getString(R.string.clothes);
                case 0:
                    return context.getString(R.string.toys);
            }
        }
        return null;
    }

}
