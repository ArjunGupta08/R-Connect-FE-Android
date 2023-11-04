package rconnect.retvens.technologies.dashboard.channelManager.DashboardFragment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import rconnect.retvens.technologies.R

class ArcProgressBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val arcPaint = Paint().apply {
        // Configure paint properties for the arc (color, style, etc.)
    }

    private val backgroundPaint = Paint().apply {
        // Configure paint properties for the background
    }

    private var progress = 0 // Current progress
    private var max = 100    // Maximum progress value

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }

    init {
        // Obtain custom attributes from the XML layout
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar)
        arcPaint.color = typedArray.getColor(R.styleable.ArcProgressBar_arcColor, Color.BLUE)
        backgroundPaint.color = typedArray.getColor(R.styleable.ArcProgressBar_backgroundColor, Color.GRAY)
        progress = typedArray.getInt(R.styleable.ArcProgressBar_progress, 0)
        max = typedArray.getInt(R.styleable.ArcProgressBar_max, 100)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = Math.min(centerX, centerY)

        // Draw the background circle
        canvas.drawCircle(centerX, centerY, radius, backgroundPaint)

        // Draw the arc based on the progress
        val startAngle = -90f // Start angle at 12 o'clock position
        val sweepAngle = 360f * progress / max
        canvas.drawArc(0f, 0f, width.toFloat(), height.toFloat(), startAngle, sweepAngle, false, arcPaint)
    }


}