package android1601.itstep.org.kidsgame.program.adapters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.activity.ToysDetailActivity;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by roman on 14.03.2017.
 */

public class ToysCollectionAdapter extends RecyclerView.Adapter<ToysCollectionAdapter.ViewHolder> {
    private final ArrayList<Gifts> items;

    public ToysCollectionAdapter(ArrayList<Gifts> items) {
        this.items = items;
    }

     public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageToysCollection)
        ImageView imageToysCollection;
        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToysDetailActivity.startThisActivity(v.getContext(),items,getAdapterPosition());
                }
            });
        }

         public void bind(final Gifts gifts/*,final OnItemClickListener listener*/){
             Glide.with(itemView.getContext())
                     .load(Utility.getDrawableResourceIdByName(
                             gifts.isUnlock() ? gifts.getResName(): gifts.getSilhouetteResName()
                     ))
                     .into(imageToysCollection);
         }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.list_item_collection_toy,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Gifts gifts = getItem(position);
        if (gifts == null)
            return;
        // Определяем содержимое нашего RecyclerView
        holder.bind(items.get(position)/*,listener*/);

    }

    @Override
    public int getItemCount() {
        return items==null?0:items.size();
    }

    public Gifts getItem(int position){
        return position<0 || position>=getItemCount()?null:items.get(position);
    }

}
