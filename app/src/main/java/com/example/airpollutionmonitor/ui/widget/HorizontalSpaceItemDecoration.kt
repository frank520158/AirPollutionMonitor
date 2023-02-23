package com.example.airpollutionmonitor.ui.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import dp2px

class HorizontalSpaceItemDecoration(private val startLeftMargin:Int, private val horizontalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.left = dp2px(startLeftMargin)
            outRect.right = dp2px(horizontalSpaceHeight)
        } else {
            outRect.left = dp2px(horizontalSpaceHeight)
            outRect.right = dp2px(horizontalSpaceHeight)
        }
    }
}