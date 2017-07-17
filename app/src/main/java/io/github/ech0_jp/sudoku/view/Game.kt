package io.github.ech0_jp.sudoku.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Xml
import android.view.View
import android.widget.Toast
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.Util.Statistics.StatisticsManager
import io.github.ech0_jp.sudoku.game.SudokuGameManager
import kotlinx.android.synthetic.main.activity_game.*
import java.io.File

class Game : AppCompatActivity() {
    private lateinit var difficulty: String
    private var _startTime: Long = 0
    private var _seconds: Int = 0
    private var _minutes: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val resume: Boolean? = intent.extras["Resume"] as Boolean?
        if (resume != null && resume){
            SudokuGameManager.instance.LoadGame(this, Xml.asAttributeSet(resources.getXml(R.layout.cell)))
            difficulty = SudokuGameManager.instance.GetDifficulty()
        } else {
            difficulty = intent.extras["difficulty"] as String
            SudokuGameManager.instance.NewGame(difficulty, this, Xml.asAttributeSet(resources.getXml(R.layout.cell)))
        }

        setContentView(R.layout.activity_game)
        _startTime = System.currentTimeMillis()
        handler.post(runnable())
    }

    val handler = Handler()
    fun runnable(): Runnable = Runnable {
        val milliseconds = System.currentTimeMillis() - _startTime
        _seconds = (milliseconds / 1000).toInt()
        _minutes = _seconds / 60
        _seconds %= 60
        tv_Timer.text = "Time: $_minutes:$_seconds"
        handler.postDelayed(runnable(), 500)
    }

    private fun GameOver(){
        handler.removeCallbacksAndMessages(null)
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Complete!")
        alert.setMessage("Completed in $_minutes:$_seconds")
        alert.setPositiveButton("OK", { _: DialogInterface, _: Int ->
            run {
                val file = File(this.filesDir.path + SudokuGameManager.instance.SAVE_FILE_NAME)
                if (file.exists()) file.delete()
                StatisticsManager.instance.AddEntry(_minutes, _seconds, difficulty)
                val intent = Intent(this, MainMenu::class.java)
                startActivity(intent)
            }
        })
        alert.setCancelable(false)
        alert.create().show()
    }

    fun btn_OnClick(view: View){
        val number: Int = view.tag.toString().toInt()
        SudokuGameManager.instance.btn_OnClick(number)
        if (SudokuGameManager.instance.GameComplete())
            GameOver()
    }

    override fun onBackPressed() {
        SudokuGameManager.instance.SaveGame()
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }
}
