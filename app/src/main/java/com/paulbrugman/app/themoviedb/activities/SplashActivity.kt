package com.paulbrugman.app.themoviedb.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.paulbrugman.app.themoviedb.databinding.ActivitySplashBinding
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    private var splashBinding: ActivitySplashBinding? = null
    private var mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initiate layout
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding?.root)

        // Run coroutine to delay and start MainActivity.class
        mainScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // set binding to null for GC process
        splashBinding = null

        mainScope.let {
            // If coroutine still has task, cancel it
            if (it.isActive) { it.cancel() }
        }
    }
}