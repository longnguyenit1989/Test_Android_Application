package com.example.testapplication.ui.recycleviewswipe

import SwipeHelper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityRecycleViewSwipeBinding

class RecycleViewSwipeActivity: BaseActivity<ActivityRecycleViewSwipeBinding>() {

    private lateinit var adapter: RecycleViewSwipeAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RecycleViewSwipeActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityRecycleViewSwipeBinding {
        return ActivityRecycleViewSwipeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRecycleViewSwipe()
    }

    private fun setRecycleViewSwipe() {
        val items = mutableListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")

        adapter = RecycleViewSwipeAdapter(items) { position ->
            adapter.removeAt(position)
        }

        binding.recyclerViewSwipe.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSwipe.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeHelper())
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewSwipe)
    }
}