package io.github.ech0_jp.sudoku.game

class SudokuGameCell(var Across: Int = 0,
                     var Down: Int = 0,
                     var Region: Int = 0,
                     var Value: Int = 0,
                     var Index: Int = 0,
                     var Changeable: Boolean = false){
//    override fun toString(): String {
//        return "SudokuGameCell: Across:$Across Down:$Down Region:$Region Value:$Value Index:$Index Changeable:$Changeable "
//        return "SudokuGameCell:\n" +
//                "\tAcross: $Across" +
//                "\tDown: $Down" +
//                "\tRegion: $Region" +
//                "\tValue: $Value" +
//                "\tIndex: $Index" +
//                "\tChangeable: $Changeable"
//    }

    override fun equals(other: Any?): Boolean {
        if (other !is SudokuGameCell) return false
        if (Index == other.Index && Value == other.Value) return true
        return false
    }
}
