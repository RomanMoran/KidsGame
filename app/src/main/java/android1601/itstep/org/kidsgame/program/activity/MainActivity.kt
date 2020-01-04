package android1601.itstep.org.kidsgame.program.activity

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import androidx.annotation.IdRes
import com.google.android.material.snackbar.Snackbar

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Switch

import java.util.ArrayList

import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.Utility.LocaleHelper
import android1601.itstep.org.kidsgame.program.adapters.CategoryAdapter
import android1601.itstep.org.kidsgame.program.data.GiftsSection
import android1601.itstep.org.kidsgame.program.db_utility.DBHelper
import butterknife.BindView
import butterknife.OnClick

class MainActivity : BaseActivity(), AdapterView.OnItemClickListener {

    @BindView(R.id.rgLanguages)
    internal var rgLanguages: RadioGroup? = null

    @BindView(R.id.recyclerViewCategory)
    internal var rvCategory: RecyclerView? = null
    private var categoryAdapter: CategoryAdapter? = null
    private var section: GiftsSection? = null

    @BindView(R.id.switchSounds)
    internal var switchSounds: Switch? = null

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLanguage()
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val prevMute = intent.getBooleanExtra(MUTE_SOUND, true)
        initSounds(prevMute)
        val sections = DBHelper.sections
        categoryAdapter = CategoryAdapter(baseContext, sections)
        //rvCategory.setLayoutManager(new GridLayoutManager(getApplicationContext(),DBHelper.getSections().size()));
        rvCategory!!.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, true)
        rvCategory!!.adapter = categoryAdapter
        categoryAdapter!!.setOnClickListener(this)
    }

    @OnClick(R.id.openEggs)
    internal fun openEggsClick() {
        BaseActivity.newInstance(this, KinderActivity::class.java)
    }

    @OnClick(R.id.openCollection)
    internal fun openCollections() {
        BaseActivity.newInstance(this, CollectionActivity::class.java)
        Log.d("TAG", "OpenCollection")
    }

    @OnClick(R.id.openPuzzle)
    internal fun openPuzzle(view: View) {
        if (DBHelper.unlockedBySection.size >= 4) {
            BaseActivity.newInstance(this, PuzzleActivity::class.java)
        } else {
            BaseActivity.newInstance(this, KinderActivity::class.java, false, false)
        }
    }


    fun initLanguage() {
        val languages = ArrayList<String>()
        languages.add("en")
        languages.add("ru")

        if (rgLanguages != null) {
            val currentLanguage = LocaleHelper.language
            // Возвращает индекс из List'a элемента со строкой
            val languageIndex = languages.indexOf(currentLanguage)
            // Присваиваем View выбранный элемент
            val childView = rgLanguages!!.getChildAt(languageIndex)
            if (childView != null)
            // Задаем активную кнопку в радиогруппу
                rgLanguages!!.check(childView.id)
            rgLanguages!!.setOnCheckedChangeListener { group, checkedId -> onLanguageSwitch(languages[group.indexOfChild(group.findViewById(checkedId))]) }
        }
    }

    private fun initSounds(prevMute: Boolean) {
        switchSounds!!.isChecked = prevMute
        switchSounds!!.setOnCheckedChangeListener { buttonView, isChecked ->
            val audioManager = applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, !isChecked)
        }
    }


    private fun onLanguageSwitch(language: String) {
        LocaleHelper.setLocale(this, language)
        restartApp()
    }

    private fun restartApp() {
        val intent = packageManager
                .getLaunchIntentForPackage(packageName)
        intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra(MUTE_SOUND, switchSounds!!.isChecked)
        finish()
        startActivity(intent)
    }


    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        section = GiftsSection()
        positionCategory = position + 1
        Snackbar.make(view, section!!.getTitleLang(position)!!, Snackbar.LENGTH_LONG).show()
    }

    companion object {

        val CARS_FOR_PUZZLE = "CARS_FOR_PUZZLE"
        private val MUTE_SOUND = "MUTE_SOUND"
        var positionCategory = 1
    }
}
