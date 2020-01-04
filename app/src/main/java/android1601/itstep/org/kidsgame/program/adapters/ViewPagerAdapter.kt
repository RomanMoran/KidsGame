package android1601.itstep.org.kidsgame.program.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager

import java.util.ArrayList

import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.fragments.ToyDetailsFragment
import androidx.viewpager.widget.PagerAdapter

/**
 * Created by roman on 10.04.2017.
 */

class ViewPagerAdapter(fm: FragmentManager, items: ArrayList<Gifts>) : FragmentStatePagerAdapter(fm), ViewPager.OnPageChangeListener {
    private val mItems: List<Gifts>
    private val mFragments = ArrayList<ToyDetailsFragment?>()

    init {
        mItems = items
        for (gifts in items) {
            mFragments.add(null)
        }
    }


    override fun getItem(position: Int): Fragment {
        val toyDetailsFragment = ToyDetailsFragment.newInstance(mItems[position])
        mFragments[position] = toyDetailsFragment
        return toyDetailsFragment
    }

    fun getItemCount(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        for (i in mFragments.indices) {
            val detailsFragment = mFragments[i]
            if (detailsFragment != null) {
                if (position == i) {
                    detailsFragment.startVoice()
                } else {
                    detailsFragment.stopAudio()
                }
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    companion object {

        private val TAG = ViewPagerAdapter::class.java.name
    }
}
