package com.paulbrugman.app.themoviedb.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paulbrugman.app.themoviedb.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}