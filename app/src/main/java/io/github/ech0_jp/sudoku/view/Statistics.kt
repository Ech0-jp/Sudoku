package io.github.ech0_jp.sudoku.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.github.ech0_jp.sudoku.R
import io.github.ech0_jp.sudoku.Util.Statistics.StatisticsManager
import kotlinx.android.synthetic.main.activity_statistics.*

class Statistics : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        Init()
    }

    private fun Init(){
        var text: String

        val lb = StatisticsManager.instance.GetHighest("Beginner", true)
        val le = StatisticsManager.instance.GetHighest("Easy", true)
        val lm = StatisticsManager.instance.GetHighest("Medium", true)
        val lh = StatisticsManager.instance.GetHighest("Hard", true)
        val lex = StatisticsManager.instance.GetHighest("Expert", true)

        val lba = StatisticsManager.instance.GetAverage("Beginner", true)
        val lea = StatisticsManager.instance.GetAverage("Easy", true)
        val lma = StatisticsManager.instance.GetAverage("Medium", true)
        val lha = StatisticsManager.instance.GetAverage("Hard", true)
        val lexa = StatisticsManager.instance.GetAverage("Expert", true)

        // BEST TIME LEGACY
        text = if(lb == null) "---" else "${lb.minutes}:${lb.seconds}"
        tv_LegacyBestTimeBeginner.text = text

        text = if(le == null) "---" else "${le.minutes}:${le.seconds}"
        tv_LegacyBestTimeEasy.text = text

        text = if(lm == null) "---" else "${lm.minutes}:${lm.seconds}"
        tv_LegacyBestTimeMedium.text = text

        text = if(lh == null) "---" else "${lh.minutes}:${lh.seconds}"
        tv_LegacyBestTimeHard.text = text

        text = if(lex == null) "---" else "${lex.minutes}:${lex.seconds}"
        tv_LegacyBestTimeExpert.text = text

        // AVG TIME LEGACY
        text = if(lba == null) "---" else "${lba.minutes}:${lba.seconds}"
        tv_LegacyAvgTimeBeginner.text = text

        text = if(lea == null) "---" else "${lea.minutes}:${lea.seconds}"
        tv_LegacyAvgTimeEasy.text = text

        text = if(lma == null) "---" else "${lma.minutes}:${lma.seconds}"
        tv_LegacyAvgTimeMedium.text = text

        text = if(lha == null) "---" else "${lha.minutes}:${lha.seconds}"
        tv_LegacyAvgTimeHard.text = text

        text = if(lexa == null) "---" else "${lexa.minutes}:${lexa.seconds}"
        tv_LegacyAvgTimeExpert.text = text

        // LEGACY GAMES PLAYED
        tv_LegacyGamesBeginner.text = StatisticsManager.instance.legacyGamesBeginner.toString()
        tv_LegacyGamesEasy.text = StatisticsManager.instance.legacyGamesEasy.toString()
        tv_LegacyGamesMedium.text = StatisticsManager.instance.legacyGamesMedium.toString()
        tv_LegacyGamesHard.text = StatisticsManager.instance.legacyGamesHard.toString()
        tv_LegacyGamesExpert.text = StatisticsManager.instance.legacyGamesExpert.toString()

        val b = StatisticsManager.instance.GetHighest("Beginner", false)
        val e = StatisticsManager.instance.GetHighest("Easy", false)
        val m = StatisticsManager.instance.GetHighest("Medium", false)
        val h = StatisticsManager.instance.GetHighest("Hard", false)
        val ex = StatisticsManager.instance.GetHighest("Expert", false)

        val ba = StatisticsManager.instance.GetAverage("Beginner", false)
        val ea = StatisticsManager.instance.GetAverage("Easy", false)
        val ma = StatisticsManager.instance.GetAverage("Medium", false)
        val ha = StatisticsManager.instance.GetAverage("Hard", false)
        val exa = StatisticsManager.instance.GetAverage("Expert", false)
    }

    fun Reset_OnClick(view: View) {
        StatisticsManager.instance.Reset(this)
        Init()
    }
}
