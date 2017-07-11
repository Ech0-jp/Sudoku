package io.github.ech0_jp.sudoku.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Xml
import android.view.View
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.game.SudokuGameManager
import java.util.*

class Game : AppCompatActivity() {
    private lateinit var difficulty: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        difficulty = intent.extras["difficulty"] as String
        SudokuGameManager.instance.NewGame(difficulty, applicationContext, Xml.asAttributeSet(resources.getXml(R.layout.cell)))
        SudokuGameManager.instance.AssignGameContext(this)

        setContentView(R.layout.activity_game)
    }

    fun btn_OnClick(view: View){
        val number: Int = view.tag.toString().toInt()
        SudokuGameManager.instance.btn_OnClick(number)
    }
}
