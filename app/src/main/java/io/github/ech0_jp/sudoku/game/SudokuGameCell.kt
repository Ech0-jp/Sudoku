package io.github.ech0_jp.sudoku.game

class SudokuGameCell(var Across: Int = 0,
                     var Down: Int = 0,
                     var Region: Int = 0,
                     var Value: Int = 0,
                     var Index: Int = 0,
                     var Changeable: Boolean = false)
