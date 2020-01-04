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
import kotlinx.android.synthetic.main.activity_toys_detail.*

/**
 * Created by roman on 10.04.2017.
 */

class ToysDetailActivity : BaseActivity() {
    private var mGiftsList: ArrayList<Gifts>? = null
    private var mOpenPosition = 0
    private var viewPagerAdapter: ViewPagerAdapter? = null

    override val layoutResId: Int
        get() = R.layout.activity_toys_detail


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mGiftsList = intent.getParcelableArrayListExtra(GIFTS)
        mOpenPosition = intent.getIntExtra(CURRENT_POSITION, 0)

        viewPagerAdapter = ViewPagerAdapter(currentFragmentManager, mGiftsList!!)
        viewPager.adapter = viewPagerAdapter
        viewPager.addOnPageChangeListener(viewPagerAdapter!!)
        viewPager.currentItem = mOpenPosition
        viewPager.post { viewPagerAdapter!!.onPageSelected(mOpenPosition) }
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
