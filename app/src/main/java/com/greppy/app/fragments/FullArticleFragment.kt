package com.greppy.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.baseappsetup.fragment.BaseFragment
import com.greppy.app.R
import com.greppy.app.utils.Constants
import kotlinx.android.synthetic.main.fragment_full_article.*

class FullArticleFragment : BaseFragment() {
    companion object {
        const val TAG = "TAG_FRAGMENT_FULL_ARTICLE"
        fun newInstance() = FullArticleFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_full_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPage()
    }

    private fun loadPage(){
        wv_article.settings.javaScriptEnabled = true
        wv_article.loadUrl(arguments?.getString(Constants.FragmentTags.URL))
    }
}