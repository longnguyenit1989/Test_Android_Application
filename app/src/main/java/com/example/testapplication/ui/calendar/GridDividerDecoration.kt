package com.example.testapplication.ui.calendar

import android.graphics.Canvas
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView

class GridDividerDecoration(
    private val color: Int,
    private val thickness: Int,
    private val spanCount: Int
) :
    RecyclerView.ItemDecoration() {

    private val paint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = thickness.toFloat()
        this.color = this@GridDividerDecoration.color
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val position = parent.getChildAdapterPosition(child)
            if (position == RecyclerView.NO_POSITION) continue

            val params = child.layoutParams as RecyclerView.LayoutParams

            val left = child.left.toFloat() - params.leftMargin
            val right = child.right.toFloat() + params.rightMargin
            val top = child.top.toFloat() - params.topMargin
            val bottom = child.bottom.toFloat() + params.bottomMargin

            val column = position % spanCount
            val row = position / spanCount

            // Draw the left border only for the first column
            if (column == 0) {
                c.drawLine(left, top, left, bottom, paint)
            }

            // Draw the top border only for the first row
            if (row == 0) {
                c.drawLine(left, top, right, top, paint)
            }

            // Always draw the right and bottom borders, this ensures every cell has all 4 edges.
            // but shared edges are drawn only once.
            c.drawLine(right, top, right, bottom, paint)
            c.drawLine(left, bottom, right, bottom, paint)
        }
    }
}


