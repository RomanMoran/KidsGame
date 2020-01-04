package android1601.itstep.org.kidsgame.program.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.viewpager.widget.ViewPager

import java.util.ArrayList

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.adapters.ViewPagerAdapter
import android1601.itstep.org.kidsgame.program.data.Gifts
import butterknife.BindView

/**
 * Created by roman on 10.04.2017.
 */

class ToysDetailActivity : BaseActivity() {
    @BindView(R.id.viewPager)
    internal var mViewPager: ViewPager? = null
    private var mGiftsList: ArrayList<Gifts>? = null
    private var mOpenPosition = 0
    private var mViewPagerAdapter: ViewPagerAdapter? = null

    override val layoutResId: Int
        get() = R.layout.activity_toys_detail


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGiftsList = intent.getParcelableArrayListExtra(GIFTS)
        mOpenPosition = intent.getIntExtra(CURRENT_POSITION, 0)

        mViewPagerAdapter = ViewPagerAdapter(currentFragmentManager, mGiftsList!!)
        mViewPager!!.adapter = mViewPagerAdapter
        mViewPager!!.addOnPageChangeListener(mViewPagerAdapter!!)
        mViewPager!!.currentItem = mOpenPosition
        mViewPager!!.post { mViewPagerAdapter!!.onPageSelected(mOpenPosition) }
    }

    companion object {
        private val GIFTS = "GIFTS"
        private val CURRENT_POSITION = "CURRENT_POSITION"

        fun startThisActivity(context: Context, giftsArrayList: List<Gifts>, currentPosition: Int) {
            val intent = Intent(context, ToysDetailActivity::class.java)
            intent.putParcelableArrayListExtra(GIFTS, giftsArrayList.toMutableList() as ArrayList<out Parcelable>)
            intent.putExtra(CURRENT_POSITION, currentPosition)
            context.startActivity(intent)
        }
    }

}
