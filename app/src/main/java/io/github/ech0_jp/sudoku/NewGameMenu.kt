package io.github.ech0_jp.sudoku

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

import io.github.ech0_jp.sudoku.R

class NewGameMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game_menu)
    }

    fun GameDifficulty_OnClick(view: View): Unit {
        val difficulty = (view as Button).text
        val intent = Intent(this, Game::class.java)
        intent.putExtra("difficulty", difficulty)
        startActivity(intent)
    }
}
