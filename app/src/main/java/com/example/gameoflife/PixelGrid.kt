package com.example.gameoflife

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View


class PixelGrid(context: Context?) :
    View(context) {
    var numColumns = 30
    var numRows = 30
    private var cellWidth = 0
    private var cellHeight = 0
    private val blackPaint: Paint = Paint()
    private var cellChecked = arrayOf<Array<Boolean>>()

    private fun calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return
        }

        cellWidth = 1320 / numColumns
        cellHeight = 1320 / numRows
        cellChecked = Array(numColumns) { Array(numRows) { false } }

        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        calculateDimensions()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        if (numColumns == 0 || numRows == 0) {
            return
        }

        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                if (cellChecked[i][j]) {
                    blackPaint.style = Paint.Style.FILL
                    canvas.drawRect(
                        (i * cellWidth).toFloat(),
                        (j * cellHeight).toFloat(),
                        ((i + 1) * cellWidth).toFloat(),
                        ((j + 1) * cellHeight).toFloat(),
                        blackPaint
                    )
                } else {
                    blackPaint.style = Paint.Style.STROKE
                    canvas.drawRect(
                        (i * cellWidth).toFloat(),
                        (j * cellHeight).toFloat(),
                        ((i + 1) * cellWidth).toFloat(),
                        ((j + 1) * cellHeight).toFloat(),
                        blackPaint
                    )
                }
            }
        }
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val column = (event.x / cellWidth).toInt()
            val row = (event.y / cellHeight).toInt()
            if (column < 0 || column >= numColumns || row < 0 || row >= numRows) {
                return false
            }
            cellChecked[column][row] = !cellChecked[column][row]
        }
        return true
    }

    private fun countNeighbours(copyCells: Array<Array<Boolean>>, i: Int, j: Int): Int {
        var neighbours = 0
        var topCellRow = j - 1
        var bottomCellRow = j + 1
        var leftCellCol = i - 1
        var rightCellCol = i + 1

        if (leftCellCol < 0) {
            leftCellCol = numColumns - 1
        }
        if (rightCellCol >= numColumns) {
            rightCellCol = 0
        }
        if (topCellRow < 0) {
            topCellRow = numRows - 1
        }
        if (bottomCellRow >= numRows) {
            bottomCellRow = 0
        }

        if (copyCells[leftCellCol][j]) {
            neighbours++
        }
        if (copyCells[leftCellCol][topCellRow]) {
            neighbours++
        }
        if (copyCells[i][topCellRow]) {
            neighbours++
        }
        if (copyCells[rightCellCol][topCellRow]) {
            neighbours++
        }
        if (copyCells[rightCellCol][j]) {
            neighbours++
        }
        if (copyCells[rightCellCol][bottomCellRow]) {
            neighbours++
        }
        if (copyCells[i][bottomCellRow]) {
            neighbours++
        }
        if (copyCells[leftCellCol][bottomCellRow]) {
            neighbours++
        }

        return neighbours
    }


    fun clearGrid() {
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                cellChecked[i][j] = false
            }
        }
    }

    fun generateRandom() {
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                cellChecked[i][j] = randomState()
            }
        }
    }

    private fun randomState(): Boolean {
        return Math.random() * 100 <= 50
    }

    fun gameOfLifeRules() {
        val copyCells = copyTheCells ()
        for (i in 0 until numColumns) {
            for (j in 0 until numRows) {
                val neighbours = countNeighbours(copyCells, i, j)
                if (copyCells[i][j]) {
                    cellChecked[i][j] = neighbours == 2 || neighbours == 3
                } else {
                    cellChecked[i][j] = neighbours == 3
                }
            }
        }
    }

    private fun copyTheCells(): Array<Array<Boolean>> {
        var copyCells = arrayOf<Array<Boolean>>()

        for (i in 0 until numColumns) {
            var internalArray = arrayOf<Boolean>()
            for (j in 0 until numRows) {
                internalArray += cellChecked[i][j]
            }
            copyCells += internalArray
        }
        return copyCells
    }
}