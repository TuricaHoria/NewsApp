package com.greppy.app.fragments

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import app.baseappsetup.fragment.BaseFragment
import com.bumptech.glide.Glide
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.gson.Gson
import com.greppy.app.R
import com.greppy.app.models.Article
import com.greppy.app.utils.Constants
import kotlinx.android.synthetic.main.fragment_article.*

class ArticleFragment : BaseFragment() {

    companion object {
        const val TAG = "TAG_FRAGMENT_ARTICLE"
        fun newInstance() = ArticleFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = Gson().fromJson(
            arguments?.getString(Constants.FragmentTags.STRING_FRAGMENT_OBJECT),
            Article::class.java
        )
        setupFragment(article)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setupFragment(article: Article) {
        context?.let {
            Glide.with(it)
                .load(article.urlToImage)
                .into(iv_article_header_photo)
        }
        tv_article_description.text = article.description
        tv_article_content.text = article.content
        btn_full_article.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.FragmentTags.URL, article.url)
            onReplaceFragmentByTAG(FullArticleFragment.TAG, bundle)
        }
        tv_share.setOnClickListener {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, article.url)
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Check this out!")
            startActivity(Intent.createChooser(shareIntent, "Share this article via"))
        }

        val radius = resources.getDimension(R.dimen.small_margin)

        val shapeAppearanceModel = ShapeAppearanceModel()
            .toBuilder()
            .setBottomRightCorner(CornerFamily.CUT, radius)
            .build()

        val shapeDrawable = MaterialShapeDrawable(shapeAppearanceModel)
        shapeDrawable.fillColor = context?.getColorStateList(R.color.white)
        ViewCompat.setBackground(cl_back_tag, shapeDrawable)

        cl_back_tag.setOnClickListener {
            onPopFragment()
        }
    }


}