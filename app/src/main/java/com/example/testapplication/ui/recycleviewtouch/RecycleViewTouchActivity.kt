package com.example.testapplication.ui.recycleviewtouch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityRecycleViewTouchBinding

class RecycleViewTouchActivity : BaseActivity<ActivityRecycleViewTouchBinding>() {

    private lateinit var recycleViewTouchAdapter: RecycleViewTouchAdapter
    private lateinit var recycleViewTouchHorizontalAdapter: RecycleViewTouchHorizontalAdapter

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, RecycleViewTouchActivity::class.java)
        }
    }

    override fun inflateBinding(inflater: LayoutInflater): ActivityRecycleViewTouchBinding {
        return ActivityRecycleViewTouchBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRecycleViewTouch()
        setRecycleViewTouchHorizontal()
    }

    private fun setRecycleViewTouch() {
        val items = mutableListOf(
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7",
            "Item 8", "Item 9", "Item 10", "Item 11", "Item 12", "Item 13"
        )

        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.bindingAdapterPosition
                val to = target.bindingAdapterPosition
                recycleViewTouchAdapter.onItemMove(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.apply {
                        alpha = 0.9f
                        scaleX = 1.05f
                        scaleY = 1.05f
                        ViewCompat.setElevation(this, 20f)
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.apply {
                    alpha = 1f
                    scaleX = 1f
                    scaleY = 1f
                    ViewCompat.setElevation(this, 0f)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)

        recycleViewTouchAdapter = RecycleViewTouchAdapter(items) { vh ->
            itemTouchHelper.startDrag(vh)
        }

        binding.recyclerViewTouch.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTouch.adapter = recycleViewTouchAdapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTouch)
    }

    private fun setRecycleViewTouchHorizontal() {
        val items = mutableListOf("Item A", "Item B", "Item C", "Item D", "Item E")

        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = viewHolder.bindingAdapterPosition
                val to = target.bindingAdapterPosition
                recycleViewTouchHorizontalAdapter.onItemMove(from, to)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

            override fun getMoveThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.1f
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.apply {
                        alpha = 0.9f
                        scaleX = 1.05f
                        scaleY = 1.05f
                        ViewCompat.setElevation(this, 20f)
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.apply {
                    alpha = 1f
                    scaleX = 1f
                    scaleY = 1f
                    ViewCompat.setElevation(this, 0f)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(callback)
        recycleViewTouchHorizontalAdapter =
            RecycleViewTouchHorizontalAdapter(items) { vh ->
                itemTouchHelper.startDrag(vh)
            }

        binding.recyclerViewTouchHorizontal.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewTouchHorizontal.adapter = recycleViewTouchHorizontalAdapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTouchHorizontal)
    }
}
