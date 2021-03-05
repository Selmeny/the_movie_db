package com.paulbrugman.app.themoviedb.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.paulbrugman.app.themoviedb.R
import com.paulbrugman.app.themoviedb.databinding.ActivityMainBinding
import com.paulbrugman.app.themoviedb.fragments.DetailFragment
import com.paulbrugman.app.themoviedb.fragments.HomeFragment
import com.paulbrugman.app.themoviedb.fragments.MovieFragment
import com.paulbrugman.app.themoviedb.interfaces.MainCallback
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.DETAIL_FRAGMENT
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.HOME_FRAGMENT
import com.paulbrugman.app.themoviedb.utilities.Constants.Companion.MOVIE_FRAGMENT
import timber.log.Timber

class MainActivity : AppCompatActivity(), MainCallback {
    private var mainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding?.root)

        addFragment(HomeFragment.newInstance(), savedInstanceState, HOME_FRAGMENT)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        mainBinding = null
    }

    override fun onBackPressed() {
        Timber.e("onBackPressed()")
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun addFragment(tag: String, data: Int?) {
        when (tag) {
            HOME_FRAGMENT -> {
                addFragment(HomeFragment.newInstance(), null, HOME_FRAGMENT)
            }

            MOVIE_FRAGMENT -> {
                addFragment(MovieFragment.newInstance(data), null, MOVIE_FRAGMENT)
            }

            DETAIL_FRAGMENT -> {
                addFragment(DetailFragment.newInstance(data), null, DETAIL_FRAGMENT)
            }
        }
    }

    override fun backToHome() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            addFragment(HomeFragment.newInstance(), null, HOME_FRAGMENT)
        }
    }

    private fun addFragment(fragment: Fragment, savedInstanceState: Bundle?, tag: String?) {
        if (savedInstanceState == null) {

            if (supportFragmentManager.fragments.size != 0) {
                val topFragment = supportFragmentManager.findFragmentByTag(tag)
                if (topFragment?.tag == tag) {
                    return
                }
            }

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add(R.id.main_fragment_container_view, fragment, tag)
                addToBackStack(tag)
            }
        }
    }
}