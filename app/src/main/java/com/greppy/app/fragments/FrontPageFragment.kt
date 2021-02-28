package com.greppy.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.baseappsetup.data.network.NetworkEventBus
import app.baseappsetup.data.network.NetworkState
import app.baseappsetup.fragment.NetworkBaseFragment
import app.baseappsetup.helpers.extensions.addTo
import app.baseappsetup.helpers.extensions.logErrorMessage
import app.baseappsetup.helpers.extensions.showDebugToast
import com.greppy.app.R
import com.greppy.app.adapters.FrontPageNewsAdapter
import com.greppy.app.api.APIClientRepository
import com.greppy.app.models.Article
import com.greppy.app.models.NewsResponse
import com.greppy.app.utils.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_landing_page.*

class FrontPageFragment : NetworkBaseFragment() {

    companion object {
        const val TAG = "TAG_FRAGMENT_HOME_PAGE"
        fun newInstance() = FrontPageFragment()
    }

    private lateinit var newsAdapter: FrontPageNewsAdapter
    private val newsList = mutableListOf<Article>()
    private var page = 0
    private var results = 0

    override fun registerNetworkEventBus() =
        NetworkEventBus.register(this, Consumer { networkState ->
            when (networkState) {
                NetworkState.NoInternet ->
                    showInternetAlert()

                NetworkState.BadRequest -> {
                    handleBadRequest(networkState)
                }

                NetworkState.OK, NetworkState.Created, NetworkState.Accepted -> {
                    val successMessage =
                        "SUCCESS: Request result state -> ${networkState.name}. API: ${networkState.api}"
                    context?.showDebugToast(successMessage)

                    networkState.message?.let { message ->
                        mAlertCallback?.showAlert(message)
                    }
                }

                else -> {
                    "Request result state: ${networkState.name}".logErrorMessage()
                    mAlertCallback?.showAlert(
                        message = networkState.message ?: getString(R.string.alert_message_unknown),
                        isCancelable = true
                    )
                }
            }
            hideProgressDialog()
        })

    private fun handleBadRequest(networkState: NetworkState) {
        networkState.message?.let { message ->
            mAlertCallback?.showAlert(message)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_landing_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        fetchNews()
    }

    override fun onStop() {
        super.onStop()
        resetParameters()
    }

    private fun fetchNews() {
        showProgressDialog()

        page++

        APIClientRepository.getNews(
            Constants.APIRelated.API_KEY,
            page,
            Constants.APIRelated.API_COUNTRY
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ newsResponse ->
                handleResponse(newsResponse)
            },
                { error ->
                    error.message?.logErrorMessage(TAG)
                }
            ).addTo(autoDisposable)
    }

    private fun setupRecyclerView() {
        newsAdapter = FrontPageNewsAdapter(newsList) {
            val bundle = Bundle()
            bundle.putString(Constants.FragmentTags.STRING_FRAGMENT_OBJECT, it)
            onReplaceFragmentByTAG(ArticleFragment.TAG, bundle)
        }
        val viewManager = LinearLayoutManager(context)

        rv_news.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount = viewManager.childCount
                val pastVisibleItem = viewManager.findFirstVisibleItemPosition()
                val total = newsAdapter.itemCount
                if (visibleItemCount + pastVisibleItem >= total) {
                    fetchNews()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        rv_news.apply {
            setHasFixedSize(true)
            adapter = newsAdapter
            layoutManager = viewManager
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun resetParameters() {
        newsList.clear()
        page = 0
    }

    private fun handleResponse(newsResponse: NewsResponse) {
        results = newsResponse.totalResults
        newsList.addAll(newsResponse.articles)
        newsAdapter.notifyDataSetChanged()
    }
}