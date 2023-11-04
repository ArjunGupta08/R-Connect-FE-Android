package rconnect.retvens.technologies.dashboard.channelManager.DashboardFragment

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class MyValueFormatter : ValueFormatter() {

    private val mFormat: DecimalFormat = DecimalFormat("###,###,###,##0.0")
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return mFormat.format(value) + " $"
    }

}
