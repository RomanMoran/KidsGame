package android1601.itstep.org.kidsgame.program.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

import java.util.ArrayList

import android1601.itstep.org.kidsgame.program.data.GiftsSection
import android1601.itstep.org.kidsgame.program.fragments.ToysCollectionFragment

/**
 * Created by roman on 17.04.2017.
 */

class ViewPagerAdapterCollections(fm: FragmentManager, // Должны передавать RecyclerView's
                                  private val mItemsTypes: List<GiftsSection>) : FragmentStatePagerAdapter(fm), ViewPager.OnPageChangeListener {

    private val mFragments = ArrayList<ToysCollectionFragment?>()

    init {
        for (i in mItemsTypes.indices) {
            mFragments.add(null)
        }
    }

    override fun getItem(position: Int): Fragment {
        val toysCollectionFragment = ToysCollectionFragment.newInstance(
                mItemsTypes[position].id.toInt())
        mFragments[position] = toysCollectionFragment
        return toysCollectionFragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mItemsTypes[position].getTitleLang(position)
    }

    override fun getCount(): Int {
        return mItemsTypes.size
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }
}
