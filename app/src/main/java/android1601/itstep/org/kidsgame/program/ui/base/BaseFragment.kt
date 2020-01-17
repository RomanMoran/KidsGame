package android1601.itstep.org.kidsgame.program.ui.base

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android1601.itstep.org.kidsgame.R
import android1601.itstep.org.kidsgame.program.ui.navigation.FragmentViewNavigator
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


abstract class BaseFragment : Fragment() {

    abstract val layoutRes: Int

    val navigator by lazy { FragmentViewNavigator(this) }

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, state: Bundle?): View? {
        return inflater.createContentView()
    }

    private fun LayoutInflater.createContentView() = FrameLayout(context).apply {
        layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
        if (layoutRes != 0) inflate(layoutRes, this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MobileAds.initialize(context!!, "ca-app-pub-2590399222273494~8054060087")
        val phoneid = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
        val adRequest = AdRequest.Builder()
                //.addTestDevice(phoneid)
                .build()
        val adView = view.findViewById<AdView>(R.id.adView)
        adView?.loadAd(adRequest)
    }

}