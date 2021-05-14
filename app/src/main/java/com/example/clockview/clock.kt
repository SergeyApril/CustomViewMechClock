package com.example.clockview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class clock @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val circlePaint : Paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val scaleForCircle: Paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val scaleForSecond: Paint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val scaleForMinute: Paint = Paint().apply {
        color = Color.GREEN
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

  fun onDrawScale(){
      val maxValueHour = 12
      val maxValueMinute = 60
      var step : Double = Math.PI/ maxValueHour
      for (n in 0..maxValueHour){
          var x1:Float = cos(Math.PI - step*n).toFloat()
          var y1:Float = sin(Math.PI - step*n).toFloat()

      }
  }
    fun onCalculateCoordinate(step: Int, maxValue: Double,radius:Float,typeOfCoordinate: Int): Float {
        when(typeOfCoordinate){
            1 -> return radius * cos(2 * Math.PI * step.toDouble()*1.0 / maxValue).toFloat()
            2 -> return radius * sin(2 * Math.PI * step.toDouble()*1.0  / maxValue).toFloat()
        }

        return 0.0F
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(width/2f, height/2f)
        canvas.rotate(270f)
        canvas.drawCircle(0F, 0F, 200F, circlePaint)
        val maxValueHour: Int = 12
        val maxValueMinute = 60
         var step : Double = 360.0/ maxValueMinute
        var xEnd:Float
        var yEnd:Float
        var xStart:Float
        var yStart:Float
        var startRadius:Float = 180f
        for (n in 1 .. maxValueMinute){
            xEnd =  onCalculateCoordinate(n,maxValueMinute.toDouble(),200f,1)
            yEnd = onCalculateCoordinate(n,maxValueMinute.toDouble(),200f,2)
            if (n%5 == 0 ){
                startRadius =160f;
            }
            else startRadius = 180f;
            xStart = onCalculateCoordinate(n,maxValueMinute.toDouble(),startRadius,1)
            yStart = onCalculateCoordinate(n,maxValueMinute.toDouble(),startRadius,2)
            Log.d("Mlog", "$xStart вторая кордината $yStart")
            canvas.drawLine(xStart, yStart, xEnd, yEnd, scaleForCircle)
        }
        var calendar = Calendar.getInstance()
            xEnd =  onCalculateCoordinate(calendar.get(Calendar.SECOND),maxValueMinute.toDouble(),200f,1)
            yEnd = onCalculateCoordinate(calendar.get(Calendar.SECOND),maxValueMinute.toDouble(),200f,2)
            canvas.drawLine(0f, 0f, xEnd, yEnd, scaleForSecond)

        var df = SimpleDateFormat("h")
        calendar.timeZone = TimeZone.getDefault()
        df.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        Log.d("NowTime",df.format(calendar.time))
        xEnd =  onCalculateCoordinate(df.format(calendar.time).toInt(),maxValueHour.toDouble(),150f,1)
        yEnd = onCalculateCoordinate(df.format(calendar.time).toInt(),maxValueHour.toDouble(),150f,2)
        canvas.drawLine(0f, 0f, xEnd, yEnd, scaleForCircle)
        xEnd =  onCalculateCoordinate(calendar.get(Calendar.MINUTE),maxValueMinute.toDouble(),160f,1)
        yEnd = onCalculateCoordinate(calendar.get(Calendar.MINUTE),maxValueMinute.toDouble(),160f,2)
        canvas.drawLine(0f, 0f, xEnd, yEnd, scaleForMinute)
        invalidate()


        Log.d("Mlog",calendar.get(Calendar.SECOND).toString())



    }
}