package io.github.ech0_jp.sudoku.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.github.ech0_jp.sudoku.game.SudokuGameManager

@Suppress("UNUSED_PARAMETER")
class SudokuGridViewAdapter(context: Context): BaseAdapter() {
    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        return SudokuGameManager.instance.GetCell(position)
    }

    override fun getItem(p0: Int): Any {
        return -1
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return 81
    }
}