package android1601.itstep.org.kidsgame.program.activity;

import android.os.Bundle;

import android1601.itstep.org.kidsgame.R;

public class KinderActivity extends BaseActivity {

    @Override
    public int getLayoutResId() {
        return R.layout.activity_with_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean carsForPuzzle = getIntent().getBooleanExtra(MainActivity.CARS_FOR_PUZZLE,true);
        showScratchEggFragment(carsForPuzzle);
    }

}
