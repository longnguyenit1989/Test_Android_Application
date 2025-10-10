package com.example.testapplication.ui.recycleviewtouch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapplication.R
import com.example.testapplication.base.BaseActivity
import com.example.testapplication.databinding.ActivityRecycleViewTouchBinding

class RecycleViewTouchActivity : BaseActivity<ActivityRecycleViewTouchBinding>() {

    private lateinit var adapter: RecycleViewTouchAdapter

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
    }

    private fun setRecycleViewTouch() {
        val nameItem1 = "${getString(R.string.item)} 1"
        val nameItem2 = "${getString(R.string.item)} 2"
        val nameItem3 = "${getString(R.string.item)} 3"
        val nameItem4 = "${getString(R.string.item)} 4"
        val nameItem5 = "${getString(R.string.item)} 5"
        val items = mutableListOf(nameItem1, nameItem2, nameItem3, nameItem4, nameItem5)

        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPos = viewHolder.bindingAdapterPosition
                val toPos = target.bindingAdapterPosition
                adapter.onItemMove(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

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

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.apply {
                    alpha = 1f
                    scaleX = 1f
                    scaleY = 1f
                    ViewCompat.setElevation(this, 0f)
                }
                adapter.onItemDrop()
            }
        }


        val itemTouchHelper = ItemTouchHelper(callback)

        adapter = RecycleViewTouchAdapter(items) { viewHolder ->
            itemTouchHelper.startDrag(viewHolder)
        }

        binding.recyclerViewTouch.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewTouch.adapter = adapter
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewTouch)
    }
}
