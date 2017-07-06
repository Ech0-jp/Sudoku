package io.github.ech0_jp.sudoku

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast

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
