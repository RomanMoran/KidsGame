package android1601.itstep.org.kidsgame.program.fragments

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View

import java.util.ArrayList

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.adapters.ToysCollectionAdapter
import android1601.itstep.org.kidsgame.program.data.Gifts
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import butterknife.BindView
import kotlinx.android.synthetic.main.fragment_toys_collection.*

/**
 * Created by roman on 14.03.2017.
 */

class ToysCollectionFragment : BaseFragment() {
    private var section: Int = 0

    private var mToysCollectionAdapter: ToysCollectionAdapter? = null

    protected override val layoutResId: Int
        get() = R.layout.fragment_toys_collection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            section = arguments!!.getInt(SECTION_KEY, 1)
        }

        val giftsList = DBHelper.getGiftsBySection(section)
        mToysCollectionAdapter = ToysCollectionAdapter(giftsList)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toysCollectionRecyclerView!!.layoutManager = GridLayoutManager(context, 3)
        toysCollectionRecyclerView!!.adapter = mToysCollectionAdapter


    }

    companion object {
        val TAG = ToysCollectionFragment::class.java.name

        private val SECTION_KEY = "SECTIONS_KEY"

        // Создание нового фрагмента
        fun newInstance(section: Int): ToysCollectionFragment {
            val fragment = ToysCollectionFragment()
            val args = Bundle()
            args.putInt(SECTION_KEY, section)
            fragment.arguments = args
            return fragment
        }
    }

}
