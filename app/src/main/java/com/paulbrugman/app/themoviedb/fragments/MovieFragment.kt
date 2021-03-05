package com.paulbrugman.app.themoviedb.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.paulbrugman.app.themoviedb.BuildConfig
import com.paulbrugman.app.themoviedb.R
import com.paulbrugman.app.themoviedb.adapters.MovieAdapter
import com.paulbrugman.app.themoviedb.api.Repository
import com.paulbrugman.app.themoviedb.databinding.FragmentMovieBinding
import com.paulbrugman.app.themoviedb.interfaces.MainCallback
import com.paulbrugman.app.themoviedb.interfaces.MovieCallback
import com.paulbrugman.app.themoviedb.models.movies.Movie
import com.paulbrugman.app.themoviedb.models.movies.Movies
import com.paulbrugman.app.themoviedb.presenter.NetworkPresenter
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.GRID_LAYOUT
import com.paulbrugman.app.themoviedb.utilities.Helper
import com.paulbrugman.app.themoviedb.utilities.PaginationListener
import kotlinx.coroutines.*
import timber.log.Timber

private const val GENRE = "genre"

class MovieFragment : Fragment(), MovieCallback {
    private lateinit var repository: Repository
    private lateinit var callback: MainCallback

    private var param: Int? = null
    private var page: Int = 1
    private val pageLimit = 20
    private var isLoading: Boolean = false

    private var movieBinding: FragmentMovieBinding? = null
    private var adapter: MovieAdapter? = null

    private var movies: MutableList<Movie> = mutableListOf()

    private var mainScope = CoroutineScope(Dispatchers.Main)
    private var ioScope = CoroutineScope(Dispatchers.IO)

    companion object {
        @JvmStatic
        fun newInstance(param: Int?) =
            MovieFragment().apply {
                arguments = Bundle().apply {
                    param?.let { putInt(GENRE, it) }
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
            param = it.getInt(GENRE)
            Timber.e(param.toString())
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        movieBinding = FragmentMovieBinding.inflate(inflater, container, false)
        return movieBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity?)?.setSupportActionBar(movieBinding?.tbMovie)
        (activity as AppCompatActivity?)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        startTask()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        movieBinding = null
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

    override fun loadMovie(data: Movies?) {
        data?.let { it ->
            it.results?.let {
                movies.addAll(it)
                adapter?.notifyDataSetChanged()
            }
        }

        movieBinding?.srlMovie?.isRefreshing = false
        isLoading = false

        mainScope.launch {
            delay(2000)

            movieBinding?.srlMovie?.visibility = View.VISIBLE
            movieBinding?.pbMovie?.visibility = View.GONE
        }
    }

    override fun loadMovieFailed(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun discoverMovie() {
        param?.let {
            if (Helper.isNetworkAvailable(requireContext())) {
                repository = Repository(BuildConfig.BASE_URL)
                NetworkPresenter.discoverMovie(BuildConfig.API_KEY, page, it, ioScope, repository, this)
            } else {
                Toast.makeText(context, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show()
            }
        } ?: kotlin.run {
            Toast.makeText(context, getString(R.string.invalid_param), Toast.LENGTH_SHORT).show()
        }
    }

    private fun setAdapter() {
        adapter = MovieAdapter(context, movies, callback)
        val layoutManager = GridLayoutManager(context, 3)

        movieBinding?.rvMovie?.layoutManager = layoutManager
        movieBinding?.rvMovie?.setHasFixedSize(true)
        movieBinding?.rvMovie?.adapter = adapter

        movieBinding?.rvMovie?.addOnScrollListener(object : PaginationListener(GRID_LAYOUT, layoutManager, pageLimit) {
            override fun loadMoreItems() {
                isLoading = true
                page ++
                discoverMovie()
            }

            override fun isLoading(): Boolean {
                return isLoading
            }
        })
    }

    private fun startTask() {
        movieBinding?.srlMovie?.setOnRefreshListener {
            page = 1
            movies.clear()
            discoverMovie()
        }

        setAdapter()
        discoverMovie()
    }
}