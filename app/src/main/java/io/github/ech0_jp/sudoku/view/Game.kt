package io.github.ech0_jp.sudoku.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.game.SudokuGameManager
import java.util.*

class Game : AppCompatActivity() {
    private lateinit var difficulty: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        difficulty = intent.extras["difficulty"] as String
        SudokuGameManager.instance.NewGame(difficulty)

        setContentView(R.layout.activity_game)
    }
}
