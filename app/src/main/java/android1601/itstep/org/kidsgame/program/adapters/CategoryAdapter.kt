package android1601.itstep.org.kidsgame.program.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.LinearLayout

import com.bumptech.glide.Glide

import java.util.ArrayList

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.data.GiftsSection
import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by roman on 24.04.2017.
 */

class CategoryAdapter(private val mContext: Context, internal var sections: List<GiftsSection>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    private val selectedItems: SparseBooleanArray
    private var currentId: Long = 0
    private var onItemClickListener: AdapterView.OnItemClickListener? = null

    init {
        selectedItems = SparseBooleanArray()
    }

    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    inner class ViewHolder(itemView: View, private val mAdapter: CategoryAdapter) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        @BindView(R.id.imageCategory)
        internal var imageCategory: ImageView? = null
        @BindView(R.id.myBackground)
        internal var myBackground: LinearLayout? = null

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)

        }

        fun bind(giftsSection: GiftsSection) {
            Glide.with(itemView.context)
                    .load(Utility.getDrawableResourceIdByName(
                            giftsSection.titleResName!!
                    ))
                    .into(imageCategory!!)
        }

        fun setDateToView(section: GiftsSection) {
            val drawableT = mContext.resources.getDrawable(R.drawable.enable_true)
            val drawableF = mContext.resources.getDrawable(R.drawable.enable_false)
            if (section.id == currentId)
            //myBackground.setBackgroundColor(Color.BLUE);
                myBackground!!.background = drawableT
            else
                myBackground!!.background = drawableF//else myBackground.setBackgroundColor(Color.WHITE);
        }

        override fun onClick(v: View) {
            currentId = sections[adapterPosition].id
            notifyItemRangeChanged(0, sections.size)
            mAdapter.onItemHolderClick(this@ViewHolder)
        }
    }

    fun setOnClickListener(onItemClickListener: AdapterView.OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun onItemHolderClick(holder: ViewHolder) {
        if (onItemClickListener != null)
            onItemClickListener!!.onItemClick(null, holder.itemView, holder.adapterPosition, holder.itemId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_category, parent, false)
        return ViewHolder(itemView, this)
    }

    override fun onBindViewHolder(holder: CategoryAdapter.ViewHolder, position: Int) {
        val section = getItem(position) ?: return
// Определяем содержимое нашего RecyclerView
        holder.bind(sections[position])
        holder.setDateToView(section)

    }

    override fun getItemCount(): Int {
        return sections.size
    }

    fun getItem(position: Int): GiftsSection? {
        return if (position < 0 || position >= itemCount) null else sections[position]
    }

}
