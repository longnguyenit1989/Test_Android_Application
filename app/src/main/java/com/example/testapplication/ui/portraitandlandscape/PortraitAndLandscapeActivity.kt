package com.example.testapplication.ui.portraitandlandscape

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityPortraitAndLandscapeBinding

class PortraitAndLandscapeActivity : BaseActivity<ActivityPortraitAndLandscapeBinding>() {

    private lateinit var adapter: ItemAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PortraitAndLandscapeActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityPortraitAndLandscapeBinding {
        return ActivityPortraitAndLandscapeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val items = List(20) { "${getString(R.string.item)} ${it + 1}" }
        adapter = ItemAdapter(items)

        val spanCount =
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
        binding.recyclerView.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerView.adapter = adapter
    }
}
