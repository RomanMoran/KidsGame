package android1601.itstep.org.kidsgame.program.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.adapters.ToysCollectionAdapter;
import android1601.itstep.org.kidsgame.program.data.Gifts;
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper;
import butterknife.BindView;

/**
 * Created by roman on 14.03.2017.
 */

public class ToysCollectionFragment extends BaseFragment {
    public static final String TAG = ToysCollectionFragment.class.getName();
    @BindView(R.id.toysCollectionRecyclerView)
    RecyclerView toysCollectionRecyclerView;

    private static final String SECTION_KEY = "SECTIONS_KEY";
    private int section;

    private ToysCollectionAdapter mToysCollectionAdapter;

    @Override
    protected int getLayoutResId() {return R.layout.fragment_toys_collection;}

    // Создание нового фрагмента
    public static ToysCollectionFragment newInstance(int section){
        ToysCollectionFragment fragment = new ToysCollectionFragment();
        Bundle args = new Bundle();
        args.putInt(SECTION_KEY,section);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!=null){
            section = getArguments().getInt(SECTION_KEY,1);
        }

        final List<Gifts> giftsList = DBHelper.getGiftsBySection(section);
        mToysCollectionAdapter = new ToysCollectionAdapter((ArrayList) giftsList);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toysCollectionRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        toysCollectionRecyclerView.setAdapter(mToysCollectionAdapter);


    }

}
