package org.example.student.dotsboxgame


import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.lib.matrix.Matrix
import uk.ac.bournemouth.ap.lib.matrix.SparseMatrix
import uk.ac.bournemouth.ap.lib.matrix.ext.Coordinate


class StudentDotsBoxGame(columns: Int, rows: Int, players: List<Player>) : AbstractDotsAndBoxesGame() {

    override val players: List<Player> = players.toList()
    private var currentPlayerSwitch = 0
    override val currentPlayer: Player get()= players[currentPlayerSwitch]

    override val boxes: Matrix<StudentBox> = Matrix(columns, rows, ::StudentBox)
    override val lines: SparseMatrix<StudentLine> = SparseMatrix(
        maxWidth = columns + 1,
        maxHeight = rows * 2 + 1,
        validator = {x, y -> y % 2 == 1 || x < columns},
        init = {x, y -> StudentLine(x, y)}
    )

    override var isFinished: Boolean = false


    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && ! isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }


    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override var isDrawn: Boolean = false

        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                val index1:Coordinate
                val index2:Coordinate
                if(lineY % 2 == 0) {
                    index1 = Coordinate(lineX, lineY / 2)
                    index2 = Coordinate(lineX, lineY / 2 - 1)
                } else {
                    index1 = Coordinate(lineX, lineY / 2)
                    index2 = Coordinate(lineX - 1, lineY / 2)
                }
                val box1 = if(boxes.isValid<Any>(index1)) boxes [index1] else null
                val box2 = if(boxes.isValid<Any>(index2)) boxes [index2] else null

                return Pair(box1, box2)
            }


        override fun drawLine() {
            require(!isDrawn)
            isDrawn = true
            var moveComplete = false
            for (box in adjacentBoxes.toList().filterNotNull()){
                if(box.boundingLines.all {it.isDrawn}) {
                    moveComplete = true
                    box.owningPlayer = currentPlayer
                }
            }

            if(!moveComplete) {
                currentPlayerSwitch = (currentPlayerSwitch + 1) % players.size
                fireGameChange()
            }

            else {
                if(boxes.none(){it.owningPlayer == null}){
                    isFinished = true
                    fireGameChange()
                    val scores = getScores().mapIndexed(){i, score -> players[i] to score}
                    fireGameOver(scores)
                } else {
                    fireGameChange()
                }
            }
            playComputerTurns()
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {
        override var owningPlayer: Player? = null

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() {
                val boundingSide = boxX
                val boundingTop = boxY * 2

                return listOf(
                    lines[boundingSide, boundingTop],
                    lines[boundingSide + 1, boundingTop + 1],
                    lines[boundingSide, boundingTop + 2],
                    lines[boundingSide, boundingTop + 1]
                )
            }


        override fun toString(): String {
            return owningPlayer?.toString() ?: "Empty"
        }
    }
}

