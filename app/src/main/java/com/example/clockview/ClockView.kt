package com.example.clockview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class ClockView : View{
    constructor(context: Context) : this(context, null){
       init(null,context)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0){
        init(attrs,context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(attrs,context)
    }
    private var colorHour : Int  =  Color.BLUE
    private var colorMinute : Int = Color.BLUE
    private var colorSecond : Int = Color.BLUE
 private fun init(attrs: AttributeSet?,context: Context){
     Log.d("init","ia tut")
     var a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView)
         colorHour = a.getColor(R.styleable.ClockView_color_hour, Color.BLACK)
     var b = colorHour
     Log.d("init","Znachenie coloroHour = $colorHour a b= $b")
         colorMinute = a.getColor(R.styleable.ClockView_color_minute, Color.BLUE)
         colorSecond = a.getColor(R.styleable.ClockView_color_second, Color.GREEN)
         a.recycle()

 }

    private val circlePaint : Paint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
    }
    private val scaleForCircle: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    private val scaleForSecond: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val scaleForMinute: Paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }
    private val scaleForHour: Paint = Paint().apply {
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
    fun reDraw(){
        invalidate()
    }
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d("SecondPotok","ia v nachale vtorogo potoka")
        Thread(Runnable{
            while (true){
                Thread.sleep(1000)
                reDraw()
        }}).start()
    }

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
        scaleForSecond.color = colorSecond
        scaleForMinute.color= colorMinute
        scaleForHour.color = colorHour
        canvas.drawLine(0f, 0f, xEnd, yEnd, scaleForSecond)

        var df = SimpleDateFormat("h")
        calendar.timeZone = TimeZone.getDefault()
        df.timeZone = TimeZone.getTimeZone("Europe/Moscow")
        Log.d("NowTime",df.format(calendar.time))
        xEnd =  onCalculateCoordinate(df.format(calendar.time).toInt(),maxValueHour.toDouble(),140f,1)
        yEnd = onCalculateCoordinate(df.format(calendar.time).toInt(),maxValueHour.toDouble(),140f,2)
        canvas.drawLine(0f, 0f, xEnd, yEnd, scaleForHour)
        xEnd =  onCalculateCoordinate(calendar.get(Calendar.MINUTE),maxValueMinute.toDouble(),160f,1)
        yEnd = onCalculateCoordinate(calendar.get(Calendar.MINUTE),maxValueMinute.toDouble(),160f,2)
        canvas.drawLine(0f, 0f, xEnd, yEnd, scaleForMinute)
       // invalidate()


        Log.d("Mlog",calendar.get(Calendar.SECOND).toString())



    }
}