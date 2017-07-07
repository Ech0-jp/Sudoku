package io.github.ech0_jp.sudoku.view

import android.content.Context
import android.util.AttributeSet
import android.widget.GridView
import android.widget.Toast

class SudokuGridView(context: Context, attributes: AttributeSet): GridView(context, attributes) {
    init {
        adapter = SudokuGridViewAdapter(context)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}