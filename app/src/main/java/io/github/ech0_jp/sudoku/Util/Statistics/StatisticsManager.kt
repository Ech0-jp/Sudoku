package io.github.ech0_jp.sudoku.Util.Statistics

import android.content.Context
import android.util.Log
import io.github.ech0_jp.sudoku.Util.XmlParser
import java.io.File

class StatisticsManager {
    private object _instance { var instance = StatisticsManager() }
    companion object {
        val instance: StatisticsManager by lazy { _instance.instance }
    }

    private val SAVE_PATH = "/Statistics"

    private var _initialized: Boolean = false
    val initialized: Boolean
        get() = _initialized

    private val legacyStatsBeginner: MutableList<StatisticsEntry> = mutableListOf()
    private val legacyStatsEasy: MutableList<StatisticsEntry> = mutableListOf()
    private val legacyStatsMedium: MutableList<StatisticsEntry> = mutableListOf()
    private val legacyStatsHard: MutableList<StatisticsEntry> = mutableListOf()
    private val legacyStatsExpert: MutableList<StatisticsEntry> = mutableListOf()

    private val statsBeginner: MutableList<StatisticsEntry> = mutableListOf()
    private val statsEasy: MutableList<StatisticsEntry> = mutableListOf()
    private val statsMedium: MutableList<StatisticsEntry> = mutableListOf()
    private val statsHard: MutableList<StatisticsEntry> = mutableListOf()
    private val statsExpert: MutableList<StatisticsEntry> = mutableListOf()

    val legacyGamesBeginner get() = legacyStatsBeginner.size
    val legacyGamesEasy get() = legacyStatsEasy.size
    val legacyGamesMedium get() = legacyStatsMedium.size
    val legacyGamesHard get() = legacyStatsHard.size
    val legacyGamesExpert get() = legacyStatsExpert.size

    val gamesBeginner get() = statsBeginner.size
    val gamesEazy get() = statsEasy.size
    val gamesMedium get() = statsMedium.size
    val gamesHard get() = statsHard.size
    val gamesExpert get() = statsExpert.size

    fun Init(context: Context){
        try {
            val filePath: String = context.filesDir.path + SAVE_PATH
            Log.d("StatisticsManager", "Loading statistics from: $filePath")

            val file: File = File(filePath)
            if (!file.exists()) return

            val fileText = file.readText()

            val tmpLegacyStatsBeginner = XmlParser.FromXml(fileText, "legacyStatsBeginner", StatisticsEntry().javaClass)
            val tmpLegacyStatsEasy = XmlParser.FromXml(fileText, "legacyStatsEasy", StatisticsEntry().javaClass)
            val tmpLegacyStatsMedium = XmlParser.FromXml(fileText, "legacyStatsMedium", StatisticsEntry().javaClass)
            val tmpLegacyStatsHard = XmlParser.FromXml(fileText, "legacyStatsHard", StatisticsEntry().javaClass)
            val tmpLegacyStatsExpert = XmlParser.FromXml(fileText, "legacyStatsExpert", StatisticsEntry().javaClass)

            if (tmpLegacyStatsBeginner != null && tmpLegacyStatsBeginner.isNotEmpty()) legacyStatsBeginner.addAll(tmpLegacyStatsBeginner)
            if (tmpLegacyStatsEasy != null && tmpLegacyStatsEasy.isNotEmpty()) legacyStatsEasy.addAll(tmpLegacyStatsEasy)
            if (tmpLegacyStatsMedium != null && tmpLegacyStatsMedium.isNotEmpty()) legacyStatsMedium.addAll(tmpLegacyStatsMedium)
            if (tmpLegacyStatsHard != null && tmpLegacyStatsHard.isNotEmpty()) legacyStatsHard.addAll(tmpLegacyStatsHard)
            if (tmpLegacyStatsExpert != null && tmpLegacyStatsExpert.isNotEmpty()) legacyStatsExpert.addAll(tmpLegacyStatsExpert)

            val tmpStatsBeginner = XmlParser.FromXml(fileText, "StatsBeginner", StatisticsEntry().javaClass)
            val tmpStatsEasy = XmlParser.FromXml(fileText, "StatsEasy", StatisticsEntry().javaClass)
            val tmpStatsMedium = XmlParser.FromXml(fileText, "StatsMedium", StatisticsEntry().javaClass)
            val tmpStatsHard = XmlParser.FromXml(fileText, "StatsHard", StatisticsEntry().javaClass)
            val tmpStatsExpert = XmlParser.FromXml(fileText, "StatsExpert", StatisticsEntry().javaClass)

            if (tmpStatsBeginner != null && tmpStatsBeginner.isNotEmpty()) statsBeginner.addAll(tmpStatsBeginner)
            if (tmpStatsEasy != null && tmpStatsEasy.isNotEmpty()) statsEasy.addAll(tmpStatsEasy)
            if (tmpStatsMedium != null && tmpStatsMedium.isNotEmpty()) statsMedium.addAll(tmpStatsMedium)
            if (tmpStatsHard != null && tmpStatsHard.isNotEmpty()) statsHard.addAll(tmpStatsHard)
            if (tmpStatsExpert != null && tmpStatsExpert.isNotEmpty()) statsExpert.addAll(tmpStatsExpert)

            _initialized = true
        } catch (e: Exception) {
            Log.e("StatisticsManager", "Failed to load stats..\n${e.message}")
        }
    }

    fun AddEntry(minutes: Int, seconds: Int, difficulty: String) {
        Log.d("StatisticsManager", "Adding new entry: minutes=$minutes, seconds=$seconds, difficulty=$difficulty")
        when (difficulty) {
            "Beginner" -> legacyStatsBeginner.add(StatisticsEntry(minutes, seconds))
            "Easy" -> legacyStatsEasy.add(StatisticsEntry(minutes, seconds))
            "Medium" -> legacyStatsMedium.add(StatisticsEntry(minutes, seconds))
            "Hard" -> legacyStatsHard.add(StatisticsEntry(minutes, seconds))
            "Expert" -> legacyStatsExpert.add(StatisticsEntry(minutes, seconds))
        }
    }

    fun AddEntry(minutes: Int, seconds: Int, score: Int, difficulty: String){
        Log.d("StatisticsManager", "Adding new entry: minutes=$minutes, seconds=$seconds, score=$score, difficulty=$difficulty")
        when (difficulty) {
            "Beginner" -> statsBeginner.add(StatisticsEntry(minutes, seconds, score))
            "Easy" -> statsEasy.add(StatisticsEntry(minutes, seconds, score))
            "Medium" -> statsMedium.add(StatisticsEntry(minutes, seconds, score))
            "Hard" -> statsHard.add(StatisticsEntry(minutes, seconds, score))
            "Expert" -> statsExpert.add(StatisticsEntry(minutes, seconds, score))
        }
    }

    fun Reset(context: Context){
        val file = File(context.filesDir.path + SAVE_PATH)
        if (file.exists()) file.delete()

        legacyStatsBeginner.clear()
        legacyStatsEasy.clear()
        legacyStatsMedium.clear()
        legacyStatsHard.clear()
        legacyStatsExpert.clear()

        statsBeginner.clear()
        statsEasy.clear()
        statsMedium.clear()
        statsHard.clear()
        statsExpert.clear()
    }

    fun GetHighest(difficulty: String, legacy: Boolean): StatisticsEntry? {
        if (legacy) {
            when(difficulty) {
                "Beginner" -> return GetHighest(legacyStatsBeginner)
                "Easy" -> return GetHighest(legacyStatsEasy)
                "Medium" -> return GetHighest(legacyStatsMedium)
                "Hard" -> return GetHighest(legacyStatsHard)
                "Expert" -> return GetHighest(legacyStatsExpert)
            }
        } else {
            when(difficulty) {
                "Beginner" -> return GetHighest(statsBeginner)
                "Easy" -> return GetHighest(statsEasy)
                "Medium" -> return GetHighest(statsMedium)
                "Hard" -> return GetHighest(statsHard)
                "Expert" -> return GetHighest(statsExpert)
            }
        }
        return null
    }

    private fun GetHighest(list: MutableList<StatisticsEntry>): StatisticsEntry? {
        if (list.size == 0) return null
        var best = list[0]
        for (e in list) {
            best = best.ReturnBest(e)
        }
        return best
    }

    fun GetAverage(difficulty: String, legacy: Boolean): StatisticsEntry? {
        if (legacy) {
            when(difficulty) {
                "Beginner" -> return GetAverage(legacyStatsBeginner)
                "Easy" -> return GetAverage(legacyStatsEasy)
                "Medium" -> return GetAverage(legacyStatsMedium)
                "Hard" -> return GetAverage(legacyStatsHard)
                "Expert" -> return GetAverage(legacyStatsExpert)
            }
        } else {
            when(difficulty) {
                "Beginner" -> return GetAverage(statsBeginner)
                "Easy" -> return GetAverage(statsEasy)
                "Medium" -> return GetAverage(statsMedium)
                "Hard" -> return GetAverage(statsHard)
                "Expert" -> return GetAverage(statsExpert)
            }
        }
        return null
    }

    private fun GetAverage(list: MutableList<StatisticsEntry>): StatisticsEntry? {
        if (list.size == 0) return null
        var minutes = 0
        var seconds = 0
        var score = 0
        for (e in list) {
            minutes += e.minutes
            seconds += e.seconds
            score += e.score
        }
        minutes /= list.size
        seconds /= list.size
        score /= list.size
        return StatisticsEntry(minutes, seconds, score)
    }

    fun Save(context: Context){
        Log.d("StatisticsManager", "Saving stats to ${context.filesDir.path + SAVE_PATH}")
        val file = File(context.filesDir.path + SAVE_PATH)
        if (!file.exists())
            file.createNewFile()
        else {
            file.delete()
            file.createNewFile()
        }
        file.appendText("<StatisticsManager>\n")

        file.appendText(XmlParser.ToXml("legacyStatsBeginner", legacyStatsBeginner.toTypedArray()))
        file.appendText(XmlParser.ToXml("legacyStatsEasy", legacyStatsEasy.toTypedArray()))
        file.appendText(XmlParser.ToXml("legacyStatsMedium", legacyStatsMedium.toTypedArray()))
        file.appendText(XmlParser.ToXml("legacyStatsHard", legacyStatsHard.toTypedArray()))
        file.appendText(XmlParser.ToXml("legacyStatsExpert", legacyStatsExpert.toTypedArray()))

        file.appendText(XmlParser.ToXml("statsBeginner", statsBeginner.toTypedArray()))
        file.appendText(XmlParser.ToXml("statsEasy", statsEasy.toTypedArray()))
        file.appendText(XmlParser.ToXml("statsMedium", statsMedium.toTypedArray()))
        file.appendText(XmlParser.ToXml("statsHard", statsHard.toTypedArray()))
        file.appendText(XmlParser.ToXml("statsExpert", statsExpert.toTypedArray()))

        file.appendText("</StatisticsManager>")
    }
}