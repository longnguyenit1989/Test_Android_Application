package com.example.testapplication.ui.canvas

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.testapplication.R

class CoordinateView(context: Context, attrs: AttributeSet, defStyle: Int) : View(context, attrs, defStyle) {
    private var mainColor = 0
    private var myCanvas: Canvas? = null
    private var paintRect = Paint()
    private var painText = Paint()
    private var painLine = Paint()
    private var painPoint = Paint()
    private var textSizeCustom = 20f
    private val offSetDiagonalLine = 20f
    private val offSetHorizontalLine = 50f  /*distance from left(or right) to HorizontalLine*/
    private val offSetVerticalLine = 50f   /*distance from top(or bottom) of canvas to VerticalLine*/
    private val distanceXPerValue = 70f      /*distance X of value*/
    private val distanceYPerValue = 70f      /*distance Y of value*/
    private val offSetValue = 10f           /*distance from HorizontalLine to top of value*/

    private var distanceFromRootCoordinateToTopY = 0f
    private var distanceFromRootCoordinateToTopX = 0f

    private var countValueX = 0
    private var countValueY = 0

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    init {
        mainColor = ContextCompat.getColor(context, R.color.black)
        paintRect = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            textSize = this@CoordinateView.textSizeCustom
            textAlign = Paint.Align.CENTER
        }
        painText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            textSize = this@CoordinateView.textSizeCustom
            textAlign = Paint.Align.CENTER
        }
        painLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            textSize = this@CoordinateView.textSizeCustom
            textAlign = Paint.Align.CENTER
        }
        painPoint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            textSize = this@CoordinateView.textSizeCustom
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
            strokeWidth = 1f
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        myCanvas = canvas
        distanceFromRootCoordinateToTopY = ((canvas.height).toFloat()/2) - offSetVerticalLine
        distanceFromRootCoordinateToTopX = ((canvas.width).toFloat()/2) - offSetHorizontalLine
        countValueX = (distanceFromRootCoordinateToTopX/distanceXPerValue).toInt()
        countValueY = (distanceFromRootCoordinateToTopY/distanceYPerValue).toInt()
        drawCoordinate(canvas)
        drawEquation(a = 3, b = 2, canvas = canvas)
    }

    private fun drawCoordinate(canvas: Canvas) {
        /*veritical line*/
        val lineVerticalStartX = (canvas.width / 2).toFloat()
        val lineVerticalStartY = offSetVerticalLine
        val lineVerticalStopX = (canvas.width / 2).toFloat()
        val lineVerticalStopY = (canvas.height).toFloat() - offSetVerticalLine
        drawLineCustom(lineVerticalStartX, lineVerticalStartY, lineVerticalStopX, lineVerticalStopY, painLine, canvas)

        /*horizontal line*/
        val lineHorizontalStartX = offSetHorizontalLine
        val lineHorizontalStartY = (canvas.height / 2).toFloat()
        val lineHorizontalStopX = (canvas.width).toFloat() - offSetHorizontalLine
        val lineHorizontalStop = (canvas.height / 2).toFloat()
        drawLineCustom(lineHorizontalStartX, lineHorizontalStartY,  lineHorizontalStopX, lineHorizontalStop, painLine, canvas)

        /*root Coordinate: 0*/
        val rootCoordinateX = (canvas.width/2).toFloat()- offSetDiagonalLine
        val rootCoordinateY = (canvas.height/2).toFloat() + offSetDiagonalLine + 10f
        drawTextCustom("0",rootCoordinateX, rootCoordinateY, painText,canvas)

        /*diagonal line of Y 1*/
        val lineDiagonalYStartX1 = (canvas.width/2).toFloat()
        val lineDiagonalYStartY1 = offSetVerticalLine
        val lineDiagonalYStopX1 = (canvas.width/2).toFloat() - offSetDiagonalLine
        val lineDiagonalYStopY1 = offSetVerticalLine + offSetDiagonalLine
        drawLineCustom(lineDiagonalYStartX1, lineDiagonalYStartY1, lineDiagonalYStopX1, lineDiagonalYStopY1, painLine, canvas)

        /*diagonal line of Y 2*/
        val lineDiagonalYStartX2 = (canvas.width/2).toFloat()
        val lineDiagonalYStartY2 = offSetVerticalLine
        val lineDiagonalYStopX2 = (canvas.width/2).toFloat() + offSetDiagonalLine
        val lineDiagonalYStopY2 = offSetVerticalLine + offSetDiagonalLine
        drawLineCustom(lineDiagonalYStartX2, lineDiagonalYStartY2, lineDiagonalYStopX2, lineDiagonalYStopY2, painLine, canvas)

        /*draw Text Y*/
        val yTextX = (canvas.width/2).toFloat() - offSetDiagonalLine
        val yTextY = offSetVerticalLine - offSetDiagonalLine
        drawTextCustom("y", yTextX, yTextY, painText, canvas)

        /*diagonal line of X 1*/
        val lineDiagonalXStartX1 = (canvas.width).toFloat() - offSetHorizontalLine - offSetDiagonalLine
        val lineDiagonalXStartY1 = (canvas.height/2).toFloat() - offSetDiagonalLine
        val lineDiagonalXStopX1 = (canvas.width).toFloat() - offSetHorizontalLine
        val lineDiagonalXStopY1 = (canvas.height / 2).toFloat()
        drawLineCustom(lineDiagonalXStartX1, lineDiagonalXStartY1, lineDiagonalXStopX1, lineDiagonalXStopY1, painLine, canvas)

        /*diagonal line of X 2*/
        val lineDiagonalXStartX2 = (canvas.width).toFloat() - offSetHorizontalLine
        val lineDiagonalXStartY2 = (canvas.height / 2).toFloat()
        val lineDiagonalXStopX2 = (canvas.width).toFloat() - offSetHorizontalLine - offSetDiagonalLine
        val lineDiagonalXStopY2 = (canvas.height/2).toFloat() + offSetDiagonalLine
        drawLineCustom(lineDiagonalXStartX2, lineDiagonalXStartY2, lineDiagonalXStopX2, lineDiagonalXStopY2, painLine, canvas)

        /*draw Text X*/
        val xTextX = (canvas.width).toFloat() - offSetHorizontalLine + (offSetDiagonalLine)
        val xTextY = (canvas.height / 2).toFloat() + offSetDiagonalLine
        drawTextCustom("x", xTextX, xTextY, painText, canvas)

        /*draw horizontal (X) value*/
        for (i in 0 .. countValueX) {
            /*draw positive value line x*/
            val valueXStartPositiveX = (canvas.width/ 2).toFloat() + (i*distanceXPerValue)
            val valueXStartPositiveY = (canvas.height/2).toFloat() - offSetValue
            val valueXStopPositiveX = (canvas.width/ 2).toFloat() + (i*distanceXPerValue)
            val valueXStopPositiveY = (canvas.height/2).toFloat() + offSetValue
            drawLineCustom(valueXStartPositiveX, valueXStartPositiveY, valueXStopPositiveX, valueXStopPositiveY, painLine, canvas)

            if (i != 0){
                /*draw positive number value x*/
                val textXPositiveValueX = (canvas.width/ 2).toFloat() + (i*distanceXPerValue)
                val textXPositiveValueY = (canvas.height/2).toFloat() + (2*offSetDiagonalLine)
                drawTextCustom( (i).toString(), textXPositiveValueX, textXPositiveValueY, painText, canvas)
                /*draw negative number value x*/
                val textXNegativeValueX = (canvas.width/ 2).toFloat() - (i*distanceXPerValue)
                val textXNegativeValueY = (canvas.height/ 2).toFloat() + (2*offSetDiagonalLine)
                drawTextCustom( (-i).toString(), textXNegativeValueX, textXNegativeValueY, painText, canvas)
            }

            /*draw negative value line y*/
            val valueXStartNegativeX = (canvas.width/ 2).toFloat() - (i*distanceXPerValue)
            val valueXStartNegativeY = (canvas.height/2).toFloat() - offSetValue
            val valueXStopNegativeX = (canvas.width/ 2).toFloat() - (i*distanceXPerValue)
            val valueXStopNegativeY = (canvas.height/2).toFloat() + offSetValue
            drawLineCustom(valueXStartNegativeX, valueXStartNegativeY, valueXStopNegativeX, valueXStopNegativeY, painLine, canvas)
        }

        /*draw vertical (Y) value*/
        for (i in 0 .. countValueY) {
            /*draw positive value line y*/
            val valueYStartPositiveX = (canvas.width/2).toFloat() - offSetValue
            val valueYStartPositiveY = (canvas.height/2).toFloat() - (i*distanceYPerValue)
            val valueYStopPositiveX = (canvas.width/2).toFloat() + offSetValue
            val valueYStopPositiveY = (canvas.height/2).toFloat() - (i*distanceYPerValue)
            drawLineCustom(valueYStartPositiveX, valueYStartPositiveY, valueYStopPositiveX, valueYStopPositiveY, painLine, canvas)

            if (i != 0){
                /*draw positive number value y*/
                val textYPositiveValueX = (canvas.width/2).toFloat() - (2*offSetDiagonalLine)
                val textYPositiveValueY = (canvas.height/2).toFloat() - (i*distanceYPerValue)
                drawTextCustom( (i).toString(), textYPositiveValueX, textYPositiveValueY, painText, canvas)
                /*draw negative number value y*/
                val textYNegativeValueX = (canvas.width/2).toFloat() - (2*offSetDiagonalLine)
                val textYNegativeValueY = (canvas.height/2).toFloat() + (i*distanceYPerValue)
                drawTextCustom( (-i).toString(), textYNegativeValueX, textYNegativeValueY, painText, canvas)
            }

            /*draw negative value line y*/
            val valueYStartNegativeX = (canvas.width/2).toFloat() - offSetValue
            val valueYStartNegativeY = (canvas.height/2).toFloat() + (i*distanceYPerValue)
            val valueYStopNegativeX = (canvas.width/2).toFloat() + offSetValue
            val valueYStopNegativeY = (canvas.height/2).toFloat() + (i*distanceYPerValue)
            drawLineCustom(valueYStartNegativeX, valueYStartNegativeY, valueYStopNegativeX, valueYStopNegativeY, painLine, canvas)
        }
    }

    private fun drawLineCustom(lineStartX: Float, lineStartY: Float, lineStopX:Float,lineVerticalStopY:Float, paintLine: Paint, canvas: Canvas){
        canvas.drawLine(lineStartX, lineStartY, lineStopX, lineVerticalStopY, paintLine)
    }

    private fun drawTextCustom(text:String,textX:Float, textY:Float, paintText: Paint, canvas: Canvas){
        canvas.drawText(text, textX, textY, paintText)
    }

    private fun drawRountRect(canvas: Canvas) {
        val bgLeft = 50f
        val bgTop = 50f
        var bgRight = 100f
        val bgBottom = 70f
        val conerRect = 4f
        canvas.drawRoundRect(bgLeft, bgTop, bgRight, bgBottom, conerRect, conerRect, paintRect)
        canvas.drawCircle(
            (canvas.width / 2).toFloat(),
            (canvas.height / 2).toFloat(),
            50f,
            paintRect
        )
    }

    private fun drawEquation(a: Int, b: Int, canvas: Canvas) {
        for (i in (-countValueX) .. countValueX) {
            val x1 = i.toFloat()
            val y1  = -(a*x1 + b.toFloat())
            val x2 = (i +1).toFloat()
            val y2 = -(a*x2 + b.toFloat())
            val realX1 = x1 * distanceXPerValue + (canvas.width/2).toFloat()    /*dung truc toa do cua minh*/
            val realY1 = y1 * distanceYPerValue + (canvas.height/2).toFloat()
            val realX2 = x2 * distanceXPerValue + (canvas.width/2).toFloat()
            val realY2 = y2 * distanceYPerValue + (canvas.height/2).toFloat()
            drawLineCustom(realX1, realY1, realX2,realY2,painLine,canvas)
        }
    }

}