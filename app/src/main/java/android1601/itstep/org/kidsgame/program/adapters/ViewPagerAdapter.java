package android1601.itstep.org.kidsgame.program.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import android1601.itstep.org.kidsgame.program.data.Gifts;
import android1601.itstep.org.kidsgame.program.fragments.ToyDetailsFragment;

/**
 * Created by roman on 10.04.2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {

    private static final String TAG = ViewPagerAdapter.class.getName();
    private List<Gifts> mItems;
    private ArrayList<ToyDetailsFragment> mFragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm,ArrayList<Gifts>items) {
        super(fm);
        mItems = items;
        for (Gifts gifts : items){
            mFragments.add(null);
        }
    }


    @Override
    public Fragment getItem(int position) {
        ToyDetailsFragment toyDetailsFragment = ToyDetailsFragment.newInstance(mItems.get(position));
        mFragments.set(position,toyDetailsFragment);
        return toyDetailsFragment;
    }

    public int getItemCount(Object object){ return POSITION_NONE;}

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mFragments.size(); i++) {
            ToyDetailsFragment detailsFragment = mFragments.get(i);
            if (detailsFragment != null) {
                if (position == i) {
                    detailsFragment.startVoice();
                } else {
                    detailsFragment.stopAudio();
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
