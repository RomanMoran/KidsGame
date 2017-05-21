package android1601.itstep.org.kidsgame.program.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import android1601.itstep.org.kidsgame.program.data.Gifts;
import android1601.itstep.org.kidsgame.program.data.GiftsSection;
import android1601.itstep.org.kidsgame.program.enums.SECTIONS_TYPE;
import android1601.itstep.org.kidsgame.program.fragments.ToysCollectionFragment;

/**
 * Created by roman on 17.04.2017.
 */

public class ViewPagerAdapterCollections extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

    private ArrayList<ToysCollectionFragment> mFragments = new ArrayList<>();

    // Должны передавать RecyclerView's
    private List<GiftsSection> mItemsTypes;

    public ViewPagerAdapterCollections(FragmentManager fm,List<GiftsSection>sections) {
        super(fm);
        mItemsTypes = sections;
        for (int i = 0; i< mItemsTypes.size(); i++){
            mFragments.add(null);
        }
    }

    @Override
    public Fragment getItem(int position) {
        ToysCollectionFragment toysCollectionFragment = ToysCollectionFragment.newInstance(
                (int)mItemsTypes.get(position).getId());
        mFragments.set(position,toysCollectionFragment);
        return toysCollectionFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mItemsTypes.get(position).getTitleLang(position);
    }

    @Override
    public int getCount() {
        return mItemsTypes.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
