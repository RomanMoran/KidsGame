package android1601.itstep.org.kidsgame.program.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.adapters.ViewPagerAdapter;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import butterknife.BindView;

/**
 * Created by roman on 10.04.2017.
 */

public class ToysDetailActivity extends BaseActivity {
    private static final String GIFTS = "GIFTS";
    private static final String CURRENT_POSITION = "CURRENT_POSITION";
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private ArrayList<Gifts> mGiftsList;
    private int mOpenPosition = 0;
    private ViewPagerAdapter mViewPagerAdapter;

    public static void startThisActivity(Context context, ArrayList<Gifts> giftsArrayList, int currentPosition){
        Intent intent = new Intent(context,ToysDetailActivity.class);
        intent.putParcelableArrayListExtra(GIFTS, giftsArrayList);
        intent.putExtra(CURRENT_POSITION,currentPosition);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_toys_detail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGiftsList = getIntent().getParcelableArrayListExtra(GIFTS);
        mOpenPosition = getIntent().getIntExtra(CURRENT_POSITION, 0);

        mViewPagerAdapter = new ViewPagerAdapter(getCurrentFragmentManager(), mGiftsList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(mViewPagerAdapter);
        mViewPager.setCurrentItem(mOpenPosition);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPagerAdapter.onPageSelected(mOpenPosition);
            }
        });
    }

}
