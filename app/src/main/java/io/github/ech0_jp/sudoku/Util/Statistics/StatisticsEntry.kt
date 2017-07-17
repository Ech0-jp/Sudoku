package io.github.ech0_jp.sudoku.Util.Statistics

class StatisticsEntry(var minutes: Int = 0, var seconds: Int = 0, var score: Int = 0) {
    fun ReturnBest(other: StatisticsEntry): StatisticsEntry {
        if (score > other.score)
            return this
        else if (score < other.score)
            return other

        if (minutes < other.minutes)
            return this
        else if (minutes > other.minutes)
            return other

        if (seconds < other.seconds)
            return this
        else
            return other
    }
}