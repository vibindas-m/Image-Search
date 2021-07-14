package com.example.imagesearch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.imagesearch.ui.main.MainViewModel
import org.koin.android.ext.android.get

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel = get()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }
}