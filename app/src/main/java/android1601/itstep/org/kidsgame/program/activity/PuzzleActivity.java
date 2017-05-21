package android1601.itstep.org.kidsgame.program.activity;

import android.os.Bundle;

import java.util.List;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper;

public class PuzzleActivity extends BaseActivity {

    public static final String TAG = PuzzleActivity.class.getName();

    @Override
    public int getLayoutResId() {return R.layout.activity_with_fragment;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Gifts> giftsSection = DBHelper.getRandomFourItems();
        showPuzzleFragment(giftsSection);
    }
}
