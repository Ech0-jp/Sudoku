package io.github.ech0_jp.sudoku.game

import io.github.ech0_jp.sudoku.view.SudokuCell
import java.util.*

class SudokuGameManager {
    private object _instance { var instance = SudokuGameManager() }
    companion object {
        val instance: SudokuGameManager by lazy { _instance.instance }
    }

    private var sudokuComplete: MutableList<SudokuGameCell> = mutableListOf()
    private var sudoku: MutableList<SudokuGameCell> = mutableListOf()

    private var _selectedCell: SudokuCell? = null
    var selectedCell: SudokuCell?
        get() = _selectedCell
        set(value){
            if (_selectedCell != null) _selectedCell!!.invalidate()
            _selectedCell = value
            if (_selectedCell != null) _selectedCell!!.invalidate()
        }

    fun GetValue(index: Int): SudokuGameCell{
        return sudoku[index]
    }

    fun SetValue(index: Int, value: Int){
        sudoku[index].Value = value
    }

    //<editor-fold desc="Game Generation">
    fun NewGame(difficulty: String){
        Clear()
        GenerateGrid()
        GenerateBoard(difficulty)
    }

    private fun Clear(){
        sudokuComplete.clear()
        sudoku.clear()
    }

    private fun GenerateBoard(difficulty: String){
        var squaresToRemove: Int = 0
        if (difficulty == "Breezy")
            squaresToRemove = 33
        else if (difficulty == "Easy")
            squaresToRemove = 47
        else if (difficulty == "Medium")
            squaresToRemove = 49
        else if (difficulty == "Hard")
            squaresToRemove = 55
        else if (difficulty == "Expert")
            squaresToRemove = 59

        sudoku.addAll(sudokuComplete)
        for (i in 1..squaresToRemove) {
            val index = GetRandom(0, 80)
            sudoku[index].Value = 0
            sudoku[index].Changeable = true
        }
    }

    fun GenerateGrid(){
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