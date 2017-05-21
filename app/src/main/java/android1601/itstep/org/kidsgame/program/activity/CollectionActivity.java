package android1601.itstep.org.kidsgame.program.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.List;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.adapters.ViewPagerAdapterCollections;
import android1601.itstep.org.kidsgame.program.data.GiftsSection;
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper;
import butterknife.BindView;

/**
 * Created by roman on 14.03.2017.
 */

public class CollectionActivity extends BaseActivity{
    private static final String TYPE_COLLECTION = "TYPE_COLLECTION";
    private static final String CURRENT_POSITION = "CURRENT_POSITION";
    @BindView(R.id.viewPagerCollection)
    ViewPager mViewPagerCollection;
    private int mOpenPosition = 0;
    private ViewPagerAdapterCollections mViewPagerAdapter;
    private List<GiftsSection> sectionsArrayList;

    @Override
    public int getLayoutResId() {return R.layout.activity_collection;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sectionsArrayList = DBHelper.getSections();
        mViewPagerAdapter = new ViewPagerAdapterCollections(getSupportFragmentManager(),sectionsArrayList);
        mViewPagerCollection.setAdapter(mViewPagerAdapter);
        mViewPagerCollection.addOnPageChangeListener(mViewPagerAdapter);
        mViewPagerCollection.setCurrentItem(mOpenPosition);
        mViewPagerCollection.post(new Runnable() {
            @Override
            public void run() {
                mViewPagerAdapter.onPageSelected(mOpenPosition);
            }
        });
        //showToysCollectionFragment(1);
    }
}
