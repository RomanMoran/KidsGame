package android1601.itstep.org.kidsgame.program.activity;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.annotation.IdRes;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import android1601.itstep.org.kidsgame.R;
import android1601.itstep.org.kidsgame.program.Utility.LocaleHelper;
import android1601.itstep.org.kidsgame.program.adapters.CategoryAdapter;
import android1601.itstep.org.kidsgame.program.data.GiftsSection;
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper;
import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final String CARS_FOR_PUZZLE = "CARS_FOR_PUZZLE";
    private static final String MUTE_SOUND = "MUTE_SOUND" ;

    @BindView(R.id.rgLanguages)
    RadioGroup rgLanguages;

    @BindView(R.id.recyclerViewCategory)
    RecyclerView rvCategory;
    private CategoryAdapter categoryAdapter;
    private GiftsSection section;
    public static int positionCategory = 1;

    @BindView(R.id.switchSounds)
    Switch switchSounds;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLanguage();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean prevMute = getIntent().getBooleanExtra(MUTE_SOUND,true);
        initSounds(prevMute);
        List sections = DBHelper.getSections();
        categoryAdapter = new CategoryAdapter(getBaseContext(),(ArrayList)sections);
        //rvCategory.setLayoutManager(new GridLayoutManager(getApplicationContext(),DBHelper.getSections().size()));
        rvCategory.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,true));
        rvCategory.setAdapter(categoryAdapter);
        categoryAdapter.setOnClickListener(this);
    }

    @OnClick(R.id.openEggs) void openEggsClick(){
        BaseActivity.newInstance(this,KinderActivity.class);
    }
    @OnClick(R.id.openCollection) void openCollections(){
        BaseActivity.newInstance(this,CollectionActivity.class);
        Log.d("TAG","OpenCollection");
    }

    @OnClick(R.id.openPuzzle) void openPuzzle(View view){
        if (DBHelper.getUnlockedBySection().size()>=4) {
            BaseActivity.newInstance(this,PuzzleActivity.class);
        }else{
            BaseActivity.newInstance(this,KinderActivity.class,false,false);
        }
    }


    public void initLanguage(){
        final List<String> languages = new ArrayList<>();
        languages.add("en");
        languages.add("ru");

        if (rgLanguages!=null){
            String currentLanguage = LocaleHelper.getLanguage();
            // Возвращает индекс из List'a элемента со строкой
            int languageIndex = languages.indexOf(currentLanguage);
            // Присваиваем View выбранный элемент
            View childView = rgLanguages.getChildAt(languageIndex);
            if (childView != null)
                // Задаем активную кнопку в радиогруппу
                rgLanguages.check(childView.getId());
            rgLanguages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    onLanguageSwitch(languages.get(group.indexOfChild(group.findViewById(checkedId))));
                }
            });
        }
    }
    private void initSounds(boolean prevMute) {
        switchSounds.setChecked(prevMute);
       switchSounds.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               AudioManager audioManager = (AudioManager)getApplicationContext().getSystemService(AUDIO_SERVICE);
                audioManager.setStreamMute(AudioManager.STREAM_MUSIC,!isChecked);
            }
        });
    }


    private void onLanguageSwitch(String language){
        LocaleHelper.setLocale(this,language);
        restartApp();
    }

    private void restartApp(){
        Intent intent = getPackageManager()
                .getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(MUTE_SOUND,switchSounds.isChecked());
        finish();
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        section = new GiftsSection();
        positionCategory = position+1;
        Snackbar.make(view, section.getTitleLang(position),Snackbar.LENGTH_LONG).show();
    }
}
