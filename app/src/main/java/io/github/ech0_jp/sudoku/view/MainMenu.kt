package io.github.ech0_jp.sudoku.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.game.SudokuGameManager
import java.io.File

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        val filePath: String = this.filesDir.path + "/Sudoku_SaveGame"
        findViewById(R.id.btn_Resume).isEnabled = File(filePath).exists()
        Log.d("MainMenu", "Main Menu created!")
    }

    @Suppress("UNUSED_PARAMETER")
    fun Resume_OnClick(view: View){
        val intent = Intent(this, Game::class.java)
        intent.putExtra("Resume", true)
        startActivity(intent)
        //SudokuGameManager.instance.LoadGame(this)
    }

    @Suppress("UNUSED_PARAMETER")
    fun NewGame_OnClick(view: View) {
        val intent = Intent(this, NewGameMenu::class.java)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun Stats_OnClick(view: View) {
        val filePath: String = this.filesDir.path + "/Sudoku_SaveGame"
        val file = File(filePath)
        if (file.exists()) {
            Toast.makeText(this, "File exists!", Toast.LENGTH_SHORT).show()
            val string = file.readText()
            Log.d("MainMenu", string)
        }
        else
            Toast.makeText(this, "File DOES NOT exists!", Toast.LENGTH_SHORT).show()
    }
}
