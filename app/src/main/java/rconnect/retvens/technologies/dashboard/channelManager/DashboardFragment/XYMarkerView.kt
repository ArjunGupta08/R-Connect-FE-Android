package rconnect.retvens.technologies.dashboard.channelManager.DashboardFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.widget.TextView
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import rconnect.retvens.technologies.R
import java.text.DecimalFormat

@SuppressLint("ViewConstructor")
class XYMarkerView(context: Context, private val xAxisValueFormatter: IAxisValueFormatter) : MarkerView(context, R.layout.custom_marker_view) {

    private val tvContent: TextView = findViewById(R.id.tvContent)
    private val format = DecimalFormat("###.0")

    override fun refreshContent(e: Entry, highlight: Highlight) {
        tvContent.text = String.format(
            "x: %s, y: %s",
            xAxisValueFormatter.getFormattedValue(e.x, null),
            format.format(e.y)
        )
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
    }
}