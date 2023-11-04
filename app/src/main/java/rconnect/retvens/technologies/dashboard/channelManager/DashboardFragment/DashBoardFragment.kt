package rconnect.retvens.technologies.dashboard.channelManager.DashboardFragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentDashBoardBinding


class DashBoardFragment : Fragment(), OnChartValueSelectedListener {

    private lateinit var bindingTab:FragmentDashBoardBinding
    private lateinit var bookingDetailsAdapter: BookingDetailsAdapter
    private  var mList:ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       bindingTab = FragmentDashBoardBinding.inflate(layoutInflater,container,false)
        return bindingTab.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBooking()

        bindingTab.bookingDetailRecycler.layoutManager = LinearLayoutManager(requireContext())

        bookingDetailsAdapter = BookingDetailsAdapter(requireContext(),mList)
        bindingTab.bookingDetailRecycler.adapter = bookingDetailsAdapter
        bookingDetailsAdapter.notifyDataSetChanged()

        pieChart()
        addChart()

    }

    private fun pieChart() {

        bindingTab.pieChart.setUsePercentValues(true)
        bindingTab.pieChart.description.isEnabled = false
        bindingTab.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        bindingTab.pieChart.dragDecelerationFrictionCoef = 0.95f


//        bindingTab.pieChart.centerText = generateCenterSpannableText()



        bindingTab.pieChart.setDrawHoleEnabled(true)
        bindingTab.pieChart.setHoleColor(Color.TRANSPARENT)

        bindingTab.pieChart.setDrawEntryLabels(false)


        bindingTab.pieChart.setDrawCenterText(false)

        bindingTab.pieChart.rotationAngle = 0f
// enable rotation of the chart by touch
        bindingTab.pieChart.isRotationEnabled = true
        bindingTab.pieChart.isHighlightPerTapEnabled = true

// chart.setUnit(" €");
// chart.setDrawUnitsInChart(true);

// add a selection listener
        bindingTab.pieChart.setOnChartValueSelectedListener(this)



        bindingTab.pieChart.animateY(1400, Easing.EaseInOutQuad)

        val l: Legend = bindingTab.pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

// entry label styling
        bindingTab.pieChart.setEntryLabelColor(Color.WHITE)
        bindingTab.pieChart.legend.isEnabled = false
        bindingTab.pieChart.holeRadius = 45f
        bindingTab.pieChart.isDrawSlicesUnderHoleEnabled


        bindingTab.pieChart.setEntryLabelTextSize(0f)

        setPieData(4,100f)
    }


    private fun setBooking() {
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")
        mList.add("1")


    }

    private fun setPieData(count: Int, range: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()

        val parties = arrayOf(
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
        )


        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size]
                )
            )
        }

        val emptyValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return "" // Return an empty string to remove the label
            }
        }

        val dataSet = PieDataSet(entries, null)

        dataSet.setDrawIcons(false)
        dataSet.setDrawValues(false)
        dataSet.colors = listOf(Color.WHITE, Color.WHITE, Color.WHITE) // Use the hole color (white) for inner slices

        dataSet.valueFormatter = emptyValueFormatter

//        dataSet.sliceSpace = 3f
//        dataSet.iconsOffset = MPPointF(0f, 40f)
//        dataSet.selectionShift = 5f

        // add a lot of colors


        val colors = ArrayList<Int>()
        colors.add(Color.parseColor("#FFC656"))
        colors.add(Color.parseColor("#67E9F1"))
        colors.add(Color.parseColor("#4B5970"))
        colors.add(Color.parseColor("#DF513A"))

//
//        for (c in ColorTemplate.VORDIPLOM_COLORS)
//            colors.add(c)
//
//        for (c in ColorTemplate.JOYFUL_COLORS)
//            colors.add(c)
//
//        for (c in ColorTemplate.COLORFUL_COLORS)
//            colors.add(c)
//
//        for (c in ColorTemplate.LIBERTY_COLORS)
//            colors.add(c)
//
//        for (c in ColorTemplate.PASTEL_COLORS)
//            colors.add(c)

//        colors.add(ColorTemplate.getHoloBlue())

        dataSet.colors = colors
        //dataSet.selectionShift = 0f;

        val data = PieData(dataSet)
//        data.setValueFormatter(PercentFormatter())
//        data.setValueTextSize(11f)
//        data.setValueTextColor(Color.WHITE)

        bindingTab.pieChart.data = data

        // undo all highlights
        bindingTab.pieChart.highlightValues(null)

        bindingTab.pieChart.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }

    private fun addChart() {

        bindingTab.barchart.setOnChartValueSelectedListener(this)

        bindingTab.barchart.setDrawBarShadow(false)
        bindingTab.barchart.setDrawValueAboveBar(true)

        bindingTab.barchart.description.isEnabled = false

// if more than 60 entries are displayed in the chart, no values will be drawn
        bindingTab.barchart.maxVisibleCount

        bindingTab.barchart.setDrawBorders(false)

// scaling can now only be done on x- and y-axis separately
        bindingTab.barchart.setPinchZoom(false)

        bindingTab.barchart.drawableState

// chart.setDrawYLabels(false);

        val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter( bindingTab.barchart)

        val xAxis =  bindingTab.barchart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.setDrawAxisLine(false) // Remove the X-axis line
        xAxis.setDrawGridLines(false) // Remove the grid lines of the X-axis
        xAxis.setDrawLabels(true)


        val custom: IAxisValueFormatter = MyAxisValueFormatter()

        val leftAxis =  bindingTab.barchart.axisLeft
        leftAxis.labelCount = 8
        leftAxis.setDrawLabels(true)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
//        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis =  bindingTab.barchart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.labelCount = 8
        rightAxis.setDrawLabels(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
//        rightAxis.valueFormatter = custom
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val l =  bindingTab.barchart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.form = Legend.LegendForm.SQUARE
        l.isEnabled = false
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f

        val mv = XYMarkerView(requireContext(), xAxisFormatter)
        mv.chartView =  bindingTab.barchart // For bounds control
        bindingTab.barchart.marker = mv

        setData(12,60f)
    }


    private fun setData(count: Int, range: Float) {
        val start = 1f
        val values = ArrayList<BarEntry>()

        for (i in start.toInt() until start.toInt() + count) {
            val num = (Math.random() * (range + 1)).toFloat()

            if (Math.random() * 100 < 25) {
                values.add(BarEntry(i.toFloat(), num, resources.getDrawable(R.drawable.check)))
            } else {
                values.add(BarEntry(i.toFloat(), num))
            }
        }

        val set1: BarDataSet

        if (bindingTab.barchart.data != null && bindingTab.barchart.data.dataSetCount > 0) {
            set1 = bindingTab.barchart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            bindingTab.barchart.data.notifyDataChanged()
            bindingTab.barchart.notifyDataSetChanged()

        } else {
            set1 = BarDataSet(values, "The year 2017")
            set1.setDrawIcons(false)

            // Set the desired bar width (increase the width)


// Set the bar color
            set1.color = Color.parseColor("#CDE387") // Set the desired color



            val startColor1 = ContextCompat.getColor(requireContext(), android.R.color.holo_orange_light)
            val startColor2 = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_light)
            val startColor3 = ContextCompat.getColor(requireContext(), android.R.color.holo_orange_light)
            val startColor4 = ContextCompat.getColor(requireContext(), android.R.color.holo_green_light)
            val startColor5 = ContextCompat.getColor(requireContext(), android.R.color.holo_red_light)
            val endColor1 = ContextCompat.getColor(requireContext(), android.R.color.holo_blue_dark)
            val endColor2 = ContextCompat.getColor(requireContext(), android.R.color.holo_purple)
            val endColor3 = ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark)
            val endColor4 = ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark)
            val endColor5 = ContextCompat.getColor(requireContext(), android.R.color.holo_orange_dark)





            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)


            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setDrawValues(false)


            data.barWidth = 0.7f

            bindingTab.barchart.data = data
        }
    }



}