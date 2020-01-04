package android1601.itstep.org.kidsgame.program.activity

import android.os.Bundle
import androidx.viewpager.widget.ViewPager

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.adapters.ViewPagerAdapterCollections
import android1601.itstep.org.kidsgame.program.data.GiftsSection
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import butterknife.BindView
import kotlinx.android.synthetic.main.activity_collection.*

/**
 * Created by roman on 14.03.2017.
 */

class CollectionActivity : BaseActivity() {

    private val mOpenPosition = 0
    private var mViewPagerAdapter: ViewPagerAdapterCollections? = null
    private var sectionsArrayList: List<GiftsSection> = DBHelper.getSections()

    override val layoutResId: Int get() = R.layout.activity_collection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewPagerAdapter = ViewPagerAdapterCollections(supportFragmentManager, sectionsArrayList)
        viewPagerCollection.adapter = mViewPagerAdapter
        viewPagerCollection.addOnPageChangeListener(mViewPagerAdapter!!)
        viewPagerCollection.currentItem = mOpenPosition
        viewPagerCollection.post { mViewPagerAdapter!!.onPageSelected(mOpenPosition) }
    }

    companion object {
        private val TYPE_COLLECTION = "TYPE_COLLECTION"
        private val CURRENT_POSITION = "CURRENT_POSITION"
    }
}
