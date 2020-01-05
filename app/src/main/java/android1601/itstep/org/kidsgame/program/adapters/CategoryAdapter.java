package android1601.itstep.org.kidsgame.program.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.Utility;
import android1601.itstep.org.kidsgame.program.data.GiftsSection;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by roman on 24.04.2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    ArrayList<GiftsSection> sections;
    private SparseBooleanArray selectedItems ;
    private long currentId = 0;
    private AdapterView.OnItemClickListener onItemClickListener;
    private Context mContext;

    public CategoryAdapter(Context context,ArrayList<GiftsSection> sections){
        this.mContext = context;
        this.sections = sections;
        selectedItems = new SparseBooleanArray();
    }

    // класс view holder-а с помощью которого мы получаем ссылку на каждый элемент
    // отдельного пункта списка
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageCategory)
        ImageView imageCategory;
        @BindView(R.id.myBackground)
        LinearLayout myBackground;

        private CategoryAdapter mAdapter;

        public ViewHolder(final View itemView, final CategoryAdapter mAdapter) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            this.mAdapter = mAdapter;
            itemView.setOnClickListener(this);

        }
        public void bind(final GiftsSection giftsSection){
            Glide.with(itemView.getContext())
                    .load(Utility.getDrawableResourceIdByName(
                            giftsSection.getTitleResName()
                    ))
                    .into(imageCategory);
        }

        public void setDateToView(GiftsSection section){
            Drawable drawableT = mContext.getResources().getDrawable(R.drawable.enable_true);
            Drawable drawableF = mContext.getResources().getDrawable(R.drawable.enable_false);
            if (section.getId()==currentId)
                //myBackground.setBackgroundColor(Color.BLUE);
                myBackground.setBackground(drawableT);
            //else myBackground.setBackgroundColor(Color.WHITE);
            else myBackground.setBackground(drawableF);
        }

        @Override
        public void onClick(View v) {
            currentId = sections.get(getAdapterPosition()).getId();
            notifyItemRangeChanged(0, sections.size());
            mAdapter.onItemHolderClick(ViewHolder.this);
        }
    }

    public void setOnClickListener(AdapterView.OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void onItemHolderClick(ViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_category,parent,false);
        return new ViewHolder(itemView,this);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        GiftsSection section = getItem(position);
        if (section==null)
            return;
        // Определяем содержимое нашего RecyclerView
        holder.bind(sections.get(position));
        holder.setDateToView(section);

    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    public GiftsSection getItem(int position){
        return position<0 || position>=getItemCount()?null:sections.get(position);
    }

}
