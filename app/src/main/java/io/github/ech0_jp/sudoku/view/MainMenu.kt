package io.github.ech0_jp.sudoku.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.Util.Statistics.StatisticsManager
import io.github.ech0_jp.sudoku.game.SudokuGameManager
import kotlinx.android.synthetic.main.activity_main_menu.*
import java.io.File

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!StatisticsManager.instance.initialized)
            StatisticsManager.instance.Init(this)
        setContentView(R.layout.activity_main_menu)
        val filePath: String = this.filesDir.path + SudokuGameManager.instance.SAVE_FILE_NAME
        btn_Resume.isEnabled = File(filePath).exists()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val filePath: String = this.filesDir.path + SudokuGameManager.instance.SAVE_FILE_NAME
            btn_Resume.isEnabled = File(filePath).exists()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun Resume_OnClick(view: View){
        val intent = Intent(this, Game::class.java)
        intent.putExtra("Resume", true)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun NewGame_OnClick(view: View) {
        val intent = Intent(this, NewGameMenu::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    @Suppress("UNUSED_PARAMETER")
    fun Stats_OnClick(view: View) {
        val intent = Intent(this, Statistics::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        startActivity(intent)
    }

    override fun onPause() {
        StatisticsManager.instance.Save(this)
        super.onPause()
    }
}
