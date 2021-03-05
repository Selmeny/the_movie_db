package com.paulbrugman.app.themoviedb.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.paulbrugman.app.themoviedb.BuildConfig
import com.paulbrugman.app.themoviedb.R
import com.paulbrugman.app.themoviedb.adapters.ReviewAdapter
import com.paulbrugman.app.themoviedb.api.Repository
import com.paulbrugman.app.themoviedb.databinding.FragmentDetailBinding
import com.paulbrugman.app.themoviedb.interfaces.DetailCallback
import com.paulbrugman.app.themoviedb.interfaces.MainCallback
import com.paulbrugman.app.themoviedb.interfaces.ReviewCallback
import com.paulbrugman.app.themoviedb.models.moviedetail.Details
import com.paulbrugman.app.themoviedb.models.reviews.Review
import com.paulbrugman.app.themoviedb.models.reviews.Reviews
import com.paulbrugman.app.themoviedb.presenter.NetworkPresenter
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.LINEAR_LAYOUT
import com.paulbrugman.app.themoviedb.utilities.Helper
import com.paulbrugman.app.themoviedb.utilities.PaginationListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.*
import timber.log.Timber

private const val DETAIL = "detail"

class DetailFragment : Fragment(), DetailCallback, ReviewCallback {
    private lateinit var repository: Repository
    private lateinit var callback: MainCallback

    private var param: Int? = null
    private var page: Int = 1
    private val pageLimit = 10
    private var isLoading: Boolean = false

    private var detailBinding: FragmentDetailBinding? = null
    private var adapter: ReviewAdapter? = null

    private var reviews: MutableList<Review> = mutableListOf()

    private var mainScope = CoroutineScope(Dispatchers.Main)
    private var ioScope = CoroutineScope(Dispatchers.IO)

    companion object {
        @JvmStatic
        fun newInstance(param: Int?) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        param?.let { putInt(DETAIL, it) }
                    }
                }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as MainCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param = it.getInt(DETAIL)
            Timber.e(param.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailBinding = FragmentDetailBinding.inflate(inflater, container, false)
        return detailBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)?.setSupportActionBar(detailBinding?.tbDetail)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        startTask()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        detailBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (ioScope.isActive) {
            ioScope.cancel()
        }

        if (mainScope.isActive) {
            mainScope.cancel()
        }
    }

    override fun loadDetail(data: Details?) {
        data?.let { it ->
            it.videos?.results?.get(0)?.key?.let {
                loadTrailer(it)
            }

            loadInfo(it)
        }

        mainScope.launch {
            delay(2000)

            detailBinding?.lnDetail?.visibility = View.VISIBLE
            detailBinding?.pbDetail?.visibility = View.GONE
        }

    }

    override fun loadDetailFailed(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    override fun loadReview(data: Reviews?) {
        data?.let { it ->
            it.results?.let {
                reviews.addAll(it)
                adapter?.notifyDataSetChanged()
            }
        }

        isLoading = false

        mainScope.launch {
            delay(2000)

            detailBinding?.lnDetail?.visibility = View.VISIBLE
            detailBinding?.pbDetail?.visibility = View.GONE
        }
    }

    override fun loadReviewFailed(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun getDetails() {
        param?.let {
            if (Helper.isNetworkAvailable(requireContext())) {
                NetworkPresenter.getDetails(BuildConfig.API_KEY, it, ioScope, repository, this)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } ?: kotlin.run {
            Toast.makeText(context, getString(R.string.invalid_param), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getReviews() {
        param?.let {
            if (Helper.isNetworkAvailable(requireContext())) {
                NetworkPresenter.getReviews(BuildConfig.API_KEY, page, it, ioScope, repository, this)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.check_internet_connection),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } ?: kotlin.run {
            Toast.makeText(context, getString(R.string.invalid_param), Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadTrailer(key: String) {
        val youTubePlayerView: YouTubePlayerView? = view?.findViewById(R.id.yt_detail_trailer)
        youTubePlayerView?.apply {
            lifecycle.addObserver(this)
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(key, 0f)
                }
            })
        }
    }

    private fun loadInfo(movie: Details) {
        movie.title?.let {
            detailBinding?.tvDetailTitle?.text = "Title: $it"
        }

        movie.release_date?.let {
            detailBinding?.tvDetailReleaseDate?.text = "Release date: $it"
        }

        movie.vote_average?.let {
            detailBinding?.tvDetailVoteAverage?.text = "Score: $it"
        }
    }

    private fun startTask() {
        repository = Repository(BuildConfig.BASE_URL)

        setAdapter()
        getDetails()
        getReviews()
    }

    private fun setAdapter() {
        adapter = ReviewAdapter(context, reviews)
        val layoutManager = LinearLayoutManager(context)

        detailBinding?.rvDetailReviews?.layoutManager = layoutManager
        detailBinding?.rvDetailReviews?.setHasFixedSize(true)
        detailBinding?.rvDetailReviews?.adapter = adapter

        detailBinding?.rvDetailReviews?.addOnScrollListener(object : PaginationListener(LINEAR_LAYOUT, layoutManager, pageLimit) {
            override fun loadMoreItems() {
                isLoading = true
                page ++
                getReviews()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }


}