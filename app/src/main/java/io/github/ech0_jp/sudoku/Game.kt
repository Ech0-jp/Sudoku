package io.github.ech0_jp.sudoku

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*

class Game : AppCompatActivity() {
    class Square(var Across: Int = 0,
                 var Down: Int = 0,
                 var Region: Int = 0,
                 var Value: Int = 0,
                 var Index: Int = 0)

    private lateinit var difficulty: String
    private var sudokuComplete: MutableList<Square> = mutableListOf()
    private var sudoku: MutableList<Square> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        difficulty = intent.extras["difficulty"] as String
        GenerateGrid()
        GenerateBoard()

        setContentView(R.layout.activity_game)
    }

    fun GenerateBoard(){
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
        for (i in 1..squaresToRemove)
            sudoku[GetRandom(0, 80)] = Square(Value = -1)
    }

    fun GenerateGrid(){
        Clear()
        var squares: MutableList<Square> = mutableListOf()
        var available: MutableList<MutableList<Int>> = mutableListOf()
        var count = 0

        for (x in 0..80){
            squares.add(Square())
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
                squares[count - 1] = Square()
                count -= 1
            }
        }

        for (i in 0..80)
            sudokuComplete.add(squares[i].Index, squares[i])
    }

    fun Clear(){
        sudokuComplete.clear()
        sudoku.clear()
    }

    private fun GetRandom(min: Int, max: Int): Int{
        if (max == 0) return 0
        val rand = Random()
        return rand.nextInt(max) + min
    }

    private fun Conflicts(currentValues: Array<Square>, test: Square): Boolean{
        return currentValues.any { ((it.Across != 0 && it.Across == test.Across) || (it.Down != 0 && it.Down == test.Down) || (it.Region != 0 && it.Region == test.Region)) && it.Value == test.Value }
    }

    private fun Item(n: Int, v: Int): Square {
        return Square(GetAcrossFromNumber(n + 1), GetDownFromNumber(n + 1), GetRegionFromNumber(n + 1), v, n)
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
}
