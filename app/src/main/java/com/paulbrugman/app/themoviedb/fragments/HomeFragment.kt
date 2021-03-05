package com.paulbrugman.app.themoviedb.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.paulbrugman.app.themoviedb.BuildConfig
import com.paulbrugman.app.themoviedb.R
import com.paulbrugman.app.themoviedb.adapters.GenreAdapter
import com.paulbrugman.app.themoviedb.api.Repository
import com.paulbrugman.app.themoviedb.databinding.FragmentHomeBinding
import com.paulbrugman.app.themoviedb.interfaces.GenreCallback
import com.paulbrugman.app.themoviedb.interfaces.MainCallback
import com.paulbrugman.app.themoviedb.models.genre.Genre
import com.paulbrugman.app.themoviedb.models.genre.Genres
import com.paulbrugman.app.themoviedb.presenter.NetworkPresenter
import com.paulbrugman.app.themoviedb.utilities.Helper
import kotlinx.coroutines.*

class HomeFragment : Fragment(), GenreCallback {
    private lateinit var repository: Repository
    private lateinit var callback: MainCallback

    private var homeBinding: FragmentHomeBinding? = null
    private var adapter: GenreAdapter? = null

    private var genres: MutableList<Genre> = mutableListOf()

    private var mainScope = CoroutineScope(Dispatchers.Main)
    private var ioScope = CoroutineScope(Dispatchers.IO)

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = context as MainCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return homeBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startTask()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
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

    override fun loadGenre(data: Genres?) {
        data?.let { it ->
            it.genres?.let {
                genres.clear()
                genres.addAll(it)
                adapter?.notifyDataSetChanged()
            }
        }

        homeBinding?.srlHome?.isRefreshing = false

        mainScope.launch {
            delay(2000)

            homeBinding?.clHome?.visibility = View.VISIBLE
            homeBinding?.pbHome?.visibility = View.GONE
        }
    }

    override fun loadGenreFailed(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun setAdapter() {
        adapter = GenreAdapter(context, genres, callback)
        homeBinding?.rvHome?.layoutManager = LinearLayoutManager(context)
        homeBinding?.rvHome?.setHasFixedSize(true)
        homeBinding?.rvHome?.adapter = adapter
    }

    private fun getGenres() {
        if (Helper.isNetworkAvailable(requireContext())) {
            repository = Repository(BuildConfig.BASE_URL)
            NetworkPresenter.getGenres(BuildConfig.API_KEY, ioScope, repository, this)
        } else {
            Toast.makeText(context, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startTask() {
        homeBinding?.srlHome?.setOnRefreshListener {
            getGenres()
        }

        setAdapter()
        getGenres()
    }

}