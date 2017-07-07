package io.github.ech0_jp.sudoku.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.game.SudokuGameManager

class SudokuGridViewAdapter(context: Context): BaseAdapter() {
    private val context = context

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var v: View? = view
        if (v == null){
            v = LayoutInflater.from(context).inflate(R.layout.cell, parent, false)
            var cell = v as SudokuCell
            cell.SetNumber(SudokuGameManager.instance.GetValue(position))
            cell.SetColor()
            cell.setOnClickListener {
                cell.onClick()
            }
            return cell
        }
        return v!!
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