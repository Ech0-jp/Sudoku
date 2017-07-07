package io.github.ech0_jp.sudoku.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import io.github.ech0_jp.sudoku.game.SudokuGameCell
import io.github.ech0_jp.sudoku.game.SudokuGameManager

class SudokuCell(context: Context, attributes: AttributeSet): View(context, attributes) {
    private var cell: SudokuGameCell = SudokuGameCell()
    private var number: String = ""

    private val paint: Paint = Paint()

    fun SetNumber(sudokuCell: SudokuGameCell){
        cell = sudokuCell
        number = sudokuCell.Value.toString()
        if(sudokuCell.Value == 0)
            number = ""

        invalidate()
    }

    fun SetColor(){
        if (cell.Region % 2 == 1){
            val bg = background as GradientDrawable
            bg.setColor(Color.GRAY)
        } else {
            val bg = background as GradientDrawable
            bg.setColor(Color.WHITE)
        }
    }

    fun onClick(){
        val prev = SudokuGameManager.instance.selectedCell
        SudokuGameManager.instance.selectedCell = this
        invalidate()
        if (prev != null)
            prev!!.invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        onDraw_SetNumber(canvas)
    }

    fun onDraw_SetNumber(canvas: Canvas?){
        if (SudokuGameManager.instance.selectedCell != null){
            if (SudokuGameManager.instance.selectedCell!!.equals(this)){
                Log.d("onDraw_SetNumber", "Selected cell is this ${cell.Index} !")
                paint.color = Color.BLUE
            }
        }
        else{
            Log.d("", "Not selected cell")
            paint.color = Color.BLACK
        }
        paint.textSize = 60F

        val rect = Rect()
        paint.getTextBounds(number, 0, number.length, rect)

        canvas!!.drawText(number, (width - rect.width()) / 2F, (height + rect.height()) / 2F, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SudokuCell)
            return false
        return cell.Index == other.cell.Index
    }
}