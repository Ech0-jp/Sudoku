package io.github.ech0_jp.sudoku.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import io.github.ech0_jp.sudoku.game.SudokuGameCell
import io.github.ech0_jp.sudoku.game.SudokuGameManager

class SudokuCell(context: Context, attributes: AttributeSet): View(context, attributes) {
    private var _cell: SudokuGameCell = SudokuGameCell()
    var cell = _cell
        get() = _cell

    private var _relatedCells: MutableList<SudokuCell> = mutableListOf()
    private var number: String = ""
    private var notes: MutableList<Int> = mutableListOf()
    var cellSelected: Boolean = false
    var highlightNumber: Boolean = false

    private val paint: Paint = Paint()

    fun SetRelatedCells(relatedCells: MutableList<SudokuCell>){
        _relatedCells = relatedCells
    }

    fun SetNumber(sudokuCell: SudokuGameCell){
        _cell = sudokuCell
        number = sudokuCell.Value.toString()
        if(sudokuCell.Value == 0)
            number = ""

        invalidate()
    }

    fun SetNumber(number: Int){
        _cell.Value = number
        this.number = number.toString()
        if (number == 0)
            this.number = ""
        notes.clear()
        invalidate()
    }

    fun AddNote(number: Int) {
        if (cell.Value != 0) return

        if (notes.contains(number))
            notes.remove(number)
        else
            notes.add(number)
        notes.sort()
        invalidate()
    }

    fun SetColor(){
        if (_cell.Region % 2 == 1){
            setBackgroundColor(Color.GRAY)
        } else {
            setBackgroundColor(Color.WHITE)
        }
    }

    fun SetColor(color: Int){
        setBackgroundColor(color)
    }

    fun onClick(){
        SudokuGameManager.instance.Cell_OnClick(this)
    }

    fun HighlightRelated(){
        if (cellSelected) {
            SetColor(Color.WHITE)
            for (r in _relatedCells) {
                r.SetColor(Color.LTGRAY)
            }
        }
        else {
            SetColor()
            for (r in _relatedCells) {
                r.SetColor()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        onDraw_SetNumber(canvas)
        onDraw_SetBorder(canvas)
    }

    fun onDraw_SetNumber(canvas: Canvas?){
        if (cellSelected || highlightNumber) paint.color = Color.parseColor("#0087ff")
        else paint.color = Color.BLACK
        paint.textSize = 60F
        paint.style = Paint.Style.FILL

        var rect = Rect()
        paint.getTextBounds(number, 0, number.length, rect)

        canvas!!.drawText(number, (width - rect.width()) / 2F, (height + rect.height()) / 2F, paint)


        paint.color = Color.BLACK
        paint.textSize = 35F
        rect = Rect()
        paint.getTextBounds("1", 0, 1, rect)
        var yOffset = 0F
        var xOffset = 0F
        for ((i, n) in notes.withIndex()) {
            if ((paint.textSize / 3) + (paint.textSize * (i - xOffset)) + paint.textSize > width) {
                yOffset += 1F
                xOffset = i.toFloat()
            }
            canvas.drawText(n.toString(), (paint.textSize / 3) + (paint.textSize * (i - xOffset)), paint.textSize + (paint.textSize * yOffset), paint)
        }
    }

    private fun onDraw_SetBorder(canvas: Canvas?) {
        if (cellSelected) {
            paint.color = Color.parseColor("#0087ff")
            paint.strokeWidth = 10F
        }
        else {
            paint.color = Color.BLACK
            paint.strokeWidth = 5F
        }
        paint.style = Paint.Style.STROKE

        canvas!!.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

    override fun equals(other: Any?): Boolean {
        if (other !is SudokuCell)
            return false
        return _cell.Index == other._cell.Index
    }
}