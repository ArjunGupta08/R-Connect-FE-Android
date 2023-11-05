package rconnect.retvens.technologies.dashboard.channelManager.DashboardFragment

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.FileUtils
import rconnect.retvens.technologies.Mobile.ReservationAdapter
import rconnect.retvens.technologies.Mobile.ReservationData
import rconnect.retvens.technologies.Mobile.RevenueAdapter
import rconnect.retvens.technologies.Mobile.RevenueData
import rconnect.retvens.technologies.R
import rconnect.retvens.technologies.databinding.FragmentDashBoardBinding
import rconnect.retvens.technologies.databinding.FragmentDashboardMobileBinding
import rconnect.retvens.technologies.databinding.FragmentLoginBinding
import rconnect.retvens.technologies.databinding.FragmentLoginMobileBinding


class DashBoardFragment : Fragment(), OnChartValueSelectedListener {

    private  var bindingTab:FragmentDashBoardBinding ?= null
    private lateinit var bindingMobile:FragmentDashboardMobileBinding
    private lateinit var bookingDetailsAdapter: BookingDetailsAdapter
    private  var mList:ArrayList<String> = ArrayList()
    val list = ArrayList<ReservationData>()
    val revenueList = ArrayList<RevenueData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val screenSize = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK

        if (screenSize >= Configuration.SCREENLAYOUT_SIZE_LARGE) {

            // Use landscape layout for tablets (e.g., screen width greater than or equal to 600dp)
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            bindingTab = FragmentDashBoardBinding.inflate(layoutInflater)
            return bindingTab!!.root
        }
        else {
            // Use portrait layout for mobile devices
            requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
            bindingMobile = FragmentDashboardMobileBinding.inflate(layoutInflater)
            return bindingMobile!!.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBooking()

        if (bindingTab is FragmentDashBoardBinding) {

            bindingTab!!.bookingDetailRecycler.layoutManager = LinearLayoutManager(requireContext())

            bookingDetailsAdapter = BookingDetailsAdapter(requireContext(), mList)
            bindingTab!!.bookingDetailRecycler.adapter = bookingDetailsAdapter
            bookingDetailsAdapter.notifyDataSetChanged()

            pieChart(bindingTab!!.pieChart)
            addChart(bindingTab!!.barchart)
            lineChart(bindingTab!!.lineChart)
            stackChart(bindingTab!!.stackBar)
        }else{
            pieChart(bindingMobile.pieChart)
            addChart(bindingMobile.barchart)
            lineChart(bindingMobile.lineChart)
            stackChart(bindingMobile.stackBar)

            list.add(ReservationData("Karan Gupta"))
            list.add(ReservationData("Karan Gupta"))
            list.add(ReservationData("Karan Gupta"))
            list.add(ReservationData("Karan Gupta"))
            list.add(ReservationData("Karan Gupta"))
            list.add(ReservationData("Karan Gupta"))
            list.add(ReservationData("Karan Gupta"))
            list.add(ReservationData("Karan Gupta"))
            bindingMobile.reservationRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            val reservationAdapter = ReservationAdapter(list,requireContext())
            bindingMobile.reservationRecycler.adapter = reservationAdapter
            reservationAdapter.notifyDataSetChanged()

            revenueList.add(RevenueData("Deluxe"))
            revenueList.add(RevenueData("Deluxe"))
            revenueList.add(RevenueData("Deluxe"))
            revenueList.add(RevenueData("Deluxe"))
            revenueList.add(RevenueData("Deluxe"))
            revenueList.add(RevenueData("Deluxe"))
            revenueList.add(RevenueData("Deluxe"))
            bindingMobile.recyclerRevenue.layoutManager = LinearLayoutManager(requireContext())
            val revenueAdapter = RevenueAdapter(revenueList,requireContext())
            bindingMobile.recyclerRevenue.adapter = revenueAdapter
            reservationAdapter.notifyDataSetChanged()
        }

    }

    private fun stackChart(bar:BarChart) {
        bar.setOnChartValueSelectedListener(this)

        bar.description.isEnabled = false

// If more than 60 entries are displayed in the chart, no values will be drawn
        bar.maxVisibleCount

// Scaling can now only be done on x- and y-axis separately
        bar.setPinchZoom(false)

        bar.setDrawGridBackground(false)
        bar.setDrawBarShadow(false)

        bar.setDrawValueAboveBar(false)
        bar.isHighlightFullBarEnabled = false

// Change the position of the y-labels
        val leftAxis = bar.axisLeft
        leftAxis.axisMinimum = 0f // This replaces setStartAtZero(true)
        bar.axisRight.isEnabled = false
        leftAxis.setDrawAxisLine(false) // Hide Y-axis line
        leftAxis.setDrawLabels(true) // Show Y-axis values
        leftAxis.setDrawGridLines(false) // Hide Y-axis gridlines// Hide Y-axis gridlines

        val xLabels = bar.xAxis
        xLabels.position = XAxisPosition.BOTTOM
        xLabels.setDrawAxisLine(false) // Hide Y-axis line
        xLabels.setDrawLabels(true) // Show Y-axis values
        xLabels.setDrawGridLines(false) // Hide Y-axis gridlines// Hide Y-axis gridlines
        // Hide X-axis gridlines

// chart.setDrawXLabels(false)
// chart.setDrawYLabels(false)

// Setting data


        val l = bar.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL

        l.formSize = 8f
        l.formToTextSpace = 4f
        l.xEntrySpace = 6f


        setBarChartData(bar, 12, 100)

    }

    private fun pieChart(pieChart: PieChart) {

        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        pieChart.dragDecelerationFrictionCoef = 0.95f


//        pieChart.centerText = generateCenterSpannableText()



        pieChart.setDrawHoleEnabled(true)
        pieChart.setHoleColor(Color.TRANSPARENT)

        pieChart.setDrawEntryLabels(false)


        pieChart.setDrawCenterText(false)

        pieChart.rotationAngle = 0f
// enable rotation of the chart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

// chart.setUnit(" €");
// chart.setDrawUnitsInChart(true);

// add a selection listener
        pieChart.setOnChartValueSelectedListener(this)



        pieChart.animateY(1400, Easing.EaseInOutQuad)

        val l: Legend = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f

// entry label styling
        pieChart.setEntryLabelColor(Color.WHITE)
        pieChart.legend.isEnabled = false
        pieChart.holeRadius = 45f
        pieChart.isDrawSlicesUnderHoleEnabled


        pieChart.setEntryLabelTextSize(0f)

        setPieData(pieChart,4,100f)
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

    private fun setPieData(pieChart: PieChart,count: Int, range: Float) {
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

        pieChart.data = data

        // undo all highlights
        pieChart.highlightValues(null)

        pieChart.invalidate()
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

    }

    override fun onNothingSelected() {

    }

    private fun addChart(barChart: BarChart) {

        barChart.setOnChartValueSelectedListener(this)

        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)

        barChart.description.isEnabled = false

// if more than 60 entries are displayed in the chart, no values will be drawn
        barChart.maxVisibleCount

        barChart.setDrawBorders(false)

// scaling can now only be done on x- and y-axis separately
        barChart.setPinchZoom(false)

        barChart.drawableState

// chart.setDrawYLabels(false);

        val xAxisFormatter: IAxisValueFormatter = DayAxisValueFormatter( barChart)

        val xAxis =  barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.setDrawAxisLine(false) // Remove the X-axis line
        xAxis.setDrawGridLines(false) // Remove the grid lines of the X-axis
        xAxis.setDrawLabels(true)


        val custom: IAxisValueFormatter = MyAxisValueFormatter()

        val leftAxis =  barChart.axisLeft
        leftAxis.labelCount = 8
        leftAxis.setDrawLabels(true)
        leftAxis.setDrawAxisLine(false)
        leftAxis.setDrawGridLines(false)
//        leftAxis.valueFormatter = custom
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val rightAxis =  barChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.labelCount = 8
        rightAxis.setDrawLabels(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
//        rightAxis.valueFormatter = custom
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)


        val l =  barChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.form = Legend.LegendForm.SQUARE
        l.isEnabled = false
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f

//        val mv = XYMarkerView(requireContext(), xAxisFormatter)
//        mv.chartView =  barChart // For bounds control
//        barChart.marker = mv

        setData(barChart,12,60f)
    }

    private fun lineChart(lineChart:LineChart) {
        lineChart.description.isEnabled = false
        lineChart.setDrawGridBackground(false)
        lineChart.data = generateLineData()
        lineChart.animateX(3000)

        val tf = ResourcesCompat.getFont(requireContext(), R.font.roboto_bold)

        val l = lineChart.legend
        l.isEnabled = false
        l.typeface = tf

        val leftAxis = lineChart.axisLeft
        leftAxis.typeface = tf
        leftAxis.axisMaximum = 1.2f
        leftAxis.axisMinimum = -1.2f
        leftAxis.setDrawGridLines(false) // Hide grid lines on the left axis
        leftAxis.setDrawAxisLine(false) // Hide the left axis line
        leftAxis.setDrawLabels(true) // Show labels on the left axis
        leftAxis.isInverted = false // Disable Y-axis inversion

        val rightAxis = lineChart.axisRight
        rightAxis.isEnabled = false // Hide the right axis

        val xAxis = lineChart.xAxis
        xAxis.typeface = tf
        xAxis.setDrawGridLines(false) // Hide grid lines on the X-axis
        xAxis.setDrawAxisLine(false) // Hide the X-axis line
        xAxis.setDrawLabels(true) // Show labels on the X-axis\
        xAxis.position = XAxis.XAxisPosition.BOTTOM
    }
    private fun generateLineData(): LineData? {

        val sets: ArrayList<ILineDataSet> = ArrayList()

        val ds1 = LineDataSet(FileUtils.loadEntriesFromAssets(requireContext().assets, "sine.txt"), "Sine function")
        val ds2 = LineDataSet(FileUtils.loadEntriesFromAssets(requireContext().assets, "cosine.txt"), "Cosine function")

        ds1.lineWidth = 2f
        ds2.lineWidth = 2f

        ds1.setDrawCircles(false)
        ds2.setDrawCircles(false)

        ds1.color = Color.parseColor("#FF92AE")
        ds2.color = Color.parseColor("#9492FF")

        // load DataSets from files in assets folder
        sets.add(ds1)
        sets.add(ds2)

        val d = LineData(sets)

        return d


    }


    private fun setBarChartData(barChart: BarChart, xProgress: Int, yProgress: Int) {
        val values = ArrayList<BarEntry>()

        for (i in 0 until xProgress) {
            val mul = (yProgress + 1).toFloat()
            val val1 = (Math.random() * mul).toFloat() + mul / 3
            val val2 = (Math.random() * mul).toFloat() + mul / 3
            val val3 = (Math.random() * mul).toFloat() + mul / 3
            val val4 = (Math.random() * mul).toFloat() + mul / 3

            values.add(BarEntry(i.toFloat(), floatArrayOf(val1, val2, val3, val4)))
        }

        val set1: BarDataSet

        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            set1 = barChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "Statistics Vienna 2014")
            set1.setDrawIcons(false)

            // Set colors for the 4 stacked bars
            set1.setColors(getColors())

            // Set stack labels
            set1.stackLabels = arrayOf("Births", "Divorces", "Marriages", "Other")

            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)

            // Hide the values on the bars
            data.setDrawValues(false)

            // Set text color for stack labels
            data.setValueTextColor(Color.WHITE)

            barChart.data = data
        }

        barChart.setFitBars(true)
        barChart.invalidate()
    }




    private fun setData(barChart: BarChart,barcount: Int, range: Float) {
        val start = 1f
        val values = ArrayList<BarEntry>()

        for (i in start.toInt() until start.toInt() + barcount) {
            val num = (Math.random() * (range + 1)).toFloat()

            if (Math.random() * 100 < 25) {
                values.add(BarEntry(i.toFloat(), num, resources.getDrawable(R.drawable.check)))
            } else {
                values.add(BarEntry(i.toFloat(), num))
            }
        }

        val set1: BarDataSet

        if (barChart.data != null && barChart.data.dataSetCount > 0) {
            set1 = barChart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            barChart.data.notifyDataChanged()
            barChart.notifyDataSetChanged()

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

            barChart.data = data
        }
    }


    private fun getColors(): List<Int> {
        val colors = ColorTemplate.MATERIAL_COLORS.toList().take(3)
        return colors
    }



}