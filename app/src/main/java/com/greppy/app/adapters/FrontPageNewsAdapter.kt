package com.greppy.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.greppy.app.R
import com.greppy.app.models.Article
import com.greppy.app.utils.toDate
import com.greppy.app.utils.untilNow
import kotlinx.android.synthetic.main.item_news_list.view.*
import java.util.*

class FrontPageNewsAdapter(private val articleList: MutableList<Article>,
                           private val onArticleSelected: (String) -> Unit) :
    RecyclerView.Adapter<FrontPageNewsAdapter.NewsViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        context = parent.context

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_news_list, parent, false)

        return NewsViewHolder(v)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        holder.itemConstraintLayout?.setOnClickListener {
                val article = Gson().toJson(articleList[position]).toString()
                onArticleSelected.invoke(article)
        }


        when (position) {

            0 -> {
                holder.articlePictureImageView?.visibility = View.GONE
                holder.headlinePhotoImageView?.visibility = View.VISIBLE
                holder.headlinePhotoImageView?.let {
                    Glide.with(context)
                        .load(articleList[position].urlToImage)
                        .into(it)
                }
                holder.headlineTextView?.text = articleList[position].title
                holder.timestampTextView?.text =
                    articleList[position].publishedAt.toDate()?.untilNow(Date())
            }

            else -> {
                holder.headlinePhotoImageView?.visibility = View.GONE
                holder.headlineTextView?.text = articleList[position].title
                holder.timestampTextView?.text =
                    articleList[position].publishedAt.toDate()?.untilNow(Date())

                holder.articlePictureImageView?.let {
                    Glide.with(context)
                        .load(articleList[position].urlToImage)
                        .into(it)
                }
            }
        }
    }

    override fun getItemCount() = articleList.size

    open inner class NewsViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val timestampTextView: TextView? = v.findViewById(R.id.tv_time)
        val headlineTextView: TextView? = v.findViewById(R.id.tv_article_headline)
        val articlePictureImageView: ImageView? = v.findViewById(R.id.iv_article_photo)
        val headlinePhotoImageView: ImageView? = v.findViewById(R.id.iv_headline_photo)
        val itemConstraintLayout: ConstraintLayout? = v.findViewById(R.id.cl_item)
    }
}