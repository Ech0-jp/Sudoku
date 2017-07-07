package io.github.ech0_jp.sudoku.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.github.ech0_jp.sudoku.R

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
    }

    fun NewGame_OnClick(view: View): Unit {
        val intent = Intent(this, NewGameMenu::class.java)
        startActivity(intent)
    }
}
