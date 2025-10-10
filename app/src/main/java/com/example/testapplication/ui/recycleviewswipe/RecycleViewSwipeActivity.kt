package com.example.testapplication.ui.recycleviewswipe

import SwipeHelper
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapplication.R
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
        val nameItem1 = "${getString(R.string.item)} 1"
        val nameItem2 = "${getString(R.string.item)} 2"
        val nameItem3 = "${getString(R.string.item)} 3"
        val nameItem4 = "${getString(R.string.item)} 4"
        val nameItem5 = "${getString(R.string.item)} 5"
        val items = mutableListOf(nameItem1, nameItem2, nameItem3, nameItem4, nameItem5)

        adapter = RecycleViewSwipeAdapter(items) { position ->
            adapter.removeAt(position)
        }

        binding.recyclerViewSwipe.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSwipe.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(SwipeHelper())
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewSwipe)
    }
}