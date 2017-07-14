package io.github.ech0_jp.sudoku.game

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.Util.XmlParser
import io.github.ech0_jp.sudoku.view.SudokuCell
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SudokuGameManager {
    private object _instance { var instance = SudokuGameManager() }
    companion object {
        val instance: SudokuGameManager by lazy { _instance.instance }
    }

    private lateinit var context: Context
    private var _difficulty: String = ""
    fun GetDifficulty(): String { return _difficulty }

    private var sudokuCells: MutableList<SudokuCell> = mutableListOf()
    private var sudokuComplete: MutableList<SudokuGameCell> = mutableListOf()
    private var sudoku: MutableList<SudokuGameCell> = mutableListOf()
    private var numbersLeft: Int = 0

    private var _selectedCell: SudokuCell? = null

    fun GetCell(index: Int): SudokuCell{
        return sudokuCells[index]
    }

    fun GetValue(index: Int): SudokuGameCell{
        return sudoku[index]
    }

    fun SetValue(index: Int, value: Int){
        sudoku[index].Value = value
    }

    //<editor-fold desc="OnClick">
    fun Cell_OnClick(cell: SudokuCell){
        if (_selectedCell != null) {
            _selectedCell!!.cellSelected = false
            _selectedCell!!.HighlightRelated()
            sudokuCells.filter { _selectedCell!!.cell.Value == it.cell.Value }
                       .forEach { it.highlightNumber = false; it.invalidate() }
            _selectedCell!!.invalidate()
        }
        if (_selectedCell != null && _selectedCell!!.equals(cell)) {
            _selectedCell = null
            return
        }
        _selectedCell = cell
        _selectedCell!!.cellSelected = true
        _selectedCell!!.HighlightRelated()
        sudokuCells.filter { _selectedCell!!.cell.Value == it.cell.Value }
                   .forEach { it.highlightNumber = true; it.invalidate() }
        _selectedCell!!.invalidate()
    }

    fun btn_OnClick(number: Int){
        if (_selectedCell == null || !_selectedCell!!.cell.Changeable || _selectedCell!!.cell.Value == number) return

        SetValue(_selectedCell!!.cell.Index, number)
        _selectedCell!!.SetNumber(number)

        if (number == 0)
            numbersLeft++
        else
            numbersLeft--

        if (numbersLeft == 0){
            if (CheckComplete())
                Toast.makeText(context, "Game is complete", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(context, "Game is NOT complete", Toast.LENGTH_LONG).show()
        }
    }
    //</editor-fold>

    private fun CheckComplete(): Boolean{
        return sudokuComplete
                .filterIndexed { index, value -> !value.equals(sudoku[index]) }
                .none()
    }

    //<editor-fold desc="Game Generation">
    fun NewGame(difficulty: String, context: Context, attributeSet: AttributeSet){
        Clear()
        _difficulty = difficulty
        this.context = context
        GenerateGrid()
        GenerateBoard(difficulty)
        GenerateCells(context, attributeSet)
    }

    fun SaveGame(): Boolean {
        try {
            val filePath: String = context.filesDir.path + "/Sudoku_SaveGame"
            Log.d("SudokuGameManager", "Saving game to: $filePath")
            val file: File = File(filePath)
            if (file.exists()) file.delete()
            file.createNewFile()

            file.appendText("<SudokuGameManager>")
            file.appendText(XmlParser.ToXml("_difficulty", _difficulty))
            file.appendText(XmlParser.ToXml("sudokuComplete", sudokuComplete.toTypedArray()))
            file.appendText(XmlParser.ToXml("sudoku", sudoku.toTypedArray()))
            file.appendText(XmlParser.ToXml("numbersLeft", numbersLeft))
            file.appendText("</SudokuGameManager>")
            return true
        } catch (e: Exception) {
            Log.e("SudokuGameManager", "Failed to saved game..\n${e.message}\n$e")
            return false
        }
    }

    fun LoadGame(context: Context, attributeSet: AttributeSet): Boolean {
        try {
            val filePath: String = context.filesDir.path + "/Sudoku_SaveGame"
            Log.d("SudokuGameManager", "Loading game from: $filePath")

            val file: File = File(filePath)
            if (!file.exists()) return false

            val fileText = file.readText()

            val tmpDifficulty = XmlParser.FromXml(fileText, "_difficulty", _difficulty) ?: throw Exception("difficulty is null!")
            val tmpSudokuComplete = XmlParser.FromXml(fileText, "sudokuComplete", SudokuGameCell().javaClass) ?: throw Exception("sudokuComplete is null!")
            val tmpSudoku = XmlParser.FromXml(fileText, "sudoku", SudokuGameCell().javaClass) ?: throw Exception("sudoku is null!")
            val tmpNumbersLeft = XmlParser.FromXml(fileText, "numbersLeft", numbersLeft) ?: throw Exception("numbersLeft is null!")

            this.context = context
            _difficulty = tmpDifficulty.toString()
            sudokuComplete.addAll(tmpSudokuComplete)
            sudoku.addAll(tmpSudoku)
            numbersLeft = tmpNumbersLeft as Int
            GenerateCells(context, attributeSet)

            return true
        } catch (e: Exception) {
            Log.e("SudokuGameManager", "Failed to load game..\nMessage:${e.message}\nError:$e")
            return false
        }
    }

    private fun Clear(){
        sudokuComplete.clear()
        sudoku.clear()
        sudokuCells.clear()
    }

    private fun GenerateGrid(){
        Clear()
        var squares: MutableList<SudokuGameCell> = mutableListOf()
        var available: MutableList<MutableList<Int>> = mutableListOf()
        var count = 0

        for (x in 0..80){
            squares.add(SudokuGameCell())
            available.add(x, mutableListOf())
            for (i in 1..9) available[x].add(i)
        }

        while (count != 81){
            if(available[count].size != 0){
                val i = GetRandom(0, available[count].size - 1)
                val z = available[count][i]
                if (!Conflicts(squares.toTypedArray(), Item(count, z))) {
                    squares[count] = Item(count, z)
                    available[count].removeAt(i)
                    count += 1
                } else {
                    available[count].removeAt(i)
                }
            } else {
                for (i in 1..9){
                    available[count].add(i)
                }
                squares[count - 1] = SudokuGameCell()
                count -= 1
            }
        }

        for (i in 0..80)
            sudokuComplete.add(squares[i].Index, squares[i])
    }

    private fun GenerateBoard(difficulty: String){
        if (difficulty == "Beginner")
            numbersLeft = 33
        else if (difficulty == "Easy")
            numbersLeft = 5 //33
        else if (difficulty == "Medium")
            numbersLeft = 49
        else if (difficulty == "Hard")
            numbersLeft = 55
        else if (difficulty == "Expert")
            numbersLeft = 59

        sudoku.addAll(sudokuComplete)
        for (i in 1..numbersLeft) {
            val index = GetRandom(0, 80)
            sudoku[index].Value = 0
            sudoku[index].Changeable = true
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun GenerateCells(context: Context, attributeSet: AttributeSet){
        for (v in sudoku){
            val cell = LayoutInflater.from(context).inflate(R.layout.cell, null, false) as SudokuCell
            cell.SetNumber(v)
            cell.SetColor()
            cell.setOnClickListener {
                cell.onClick()
            }
            sudokuCells.add(cell)
        }

        for (v in sudokuCells){
            val related: MutableList<SudokuCell> = sudokuCells
                    .filter { v.cell.Index != it.cell.Index && (v.cell.Down == it.cell.Down || v.cell.Across == it.cell.Across || v.cell.Region == it.cell.Region) }
                    .toMutableList()
            v.SetRelatedCells(related)
        }
    }

    private fun GetRandom(min: Int, max: Int): Int{
        if (max == 0) return 0
        val rand = Random()
        return rand.nextInt(max) + min
    }

    private fun Conflicts(currentValues: Array<SudokuGameCell>, test: SudokuGameCell): Boolean{
        return currentValues.any { ((it.Across != 0 && it.Across == test.Across) || (it.Down != 0 && it.Down == test.Down) || (it.Region != 0 && it.Region == test.Region)) && it.Value == test.Value }
    }

    private fun Item(n: Int, v: Int): SudokuGameCell {
        return SudokuGameCell(GetAcrossFromNumber(n + 1), GetDownFromNumber(n + 1), GetRegionFromNumber(n + 1), v, n)
    }

    private fun GetAcrossFromNumber(n: Int): Int{
        val k = n % 9
        if(k == 0)
            return 9
        else
            return k
    }

    private fun GetDownFromNumber(n: Int): Int{
        if (GetAcrossFromNumber(n) == 9)
            return n / 9
        else
            return n / 9 + 1
    }

    private fun GetRegionFromNumber(n: Int): Int{
        val a: Int = GetAcrossFromNumber(n)
        val d: Int = GetDownFromNumber(n)

        if (a in 1..3 && d in 1..3)
            return 1
        else if (a in 4..6 && d in 1..3)
            return 2
        else if (a in 7..9 && d in 1..3)
            return 3
        else if (a in 1..3 && d in 4..6)
            return 4
        else if (a in 4..6 && d in 4..6)
            return 5
        else if (a in 7..9 && d in 4..6)
            return 6
        else if (a in 1..3 && d in 7..9)
            return 7
        else if (a in 4..6 && d in 7..9)
            return 8
        else // a in 7..9 && d in 7..9
            return 9
    }
    //</editor-fold>
}