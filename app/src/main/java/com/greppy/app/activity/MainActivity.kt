package com.greppy.app.activity

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.greppy.app.R
import app.baseappsetup.activity.BaseFragmentActivity
import com.greppy.app.fragments.ArticleFragment
import com.greppy.app.fragments.FrontPageFragment
import com.greppy.app.fragments.FullArticleFragment

class MainActivity : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setActionBarVisibility(false)

        onReplaceFragment(FrontPageFragment.TAG)
    }

    override fun onBackPressed() {

        when {

            (getContainerFragmentManager()?.backStackEntryCount ?: 0) > 0 ->
                getContainerFragmentManager()?.popBackStack()

            else ->
                super.onBackPressed()
        }
    }

    override fun getFragmentContainer(): Int {
        return R.id.frame
    }

    override fun getContainerFragmentManager(): FragmentManager? = mFragmentManager

    override fun getFragmentByTag(TAG: String) = when (TAG) {

        FrontPageFragment.TAG -> FrontPageFragment.newInstance()

        ArticleFragment.TAG -> ArticleFragment.newInstance()

        FullArticleFragment.TAG -> FullArticleFragment.newInstance()

        else -> null
    }


}