package android1601.itstep.org.kidsgame.program.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide

import java.util.ArrayList

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.Utility
import android1601.itstep.org.kidsgame.program.activity.ToysDetailActivity
import android1601.itstep.org.kidsgame.program.data.Gifts
import butterknife.BindView
import butterknife.ButterKnife

/**
 * Created by roman on 14.03.2017.
 */

class ToysCollectionAdapter(private val items: List<Gifts>) : RecyclerView.Adapter<ToysCollectionAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @BindView(R.id.imageToysCollection)
        internal var imageToysCollection: ImageView? = null

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener { v -> ToysDetailActivity.startThisActivity(v.context, items, adapterPosition) }
        }

        fun bind(gifts: Gifts/*,final OnItemClickListener listener*/) {
            Glide.with(itemView.context)
                    .load(Utility.getDrawableResourceIdByName(
                            if (gifts.isUnlock) gifts.resName!! else gifts.silhouetteResName
                    ))
                    .into(imageToysCollection!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_collection_toy, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gifts = getItem(position) ?: return
// Определяем содержимое нашего RecyclerView
        holder.bind(items!![position]/*,listener*/)

    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    fun getItem(position: Int): Gifts? {
        return if (position < 0 || position >= itemCount) null else items!![position]
    }

}
