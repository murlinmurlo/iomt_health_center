package com.iomt.android.compose.view.bt


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import kotlin.math.abs



@Composable
fun EcgGraph(ecgChannel1: List<Float>) {
//    val ecgData = fileHandler.readPartial_Float("ecg.txt", frequency = 3)
//    val (ecgChannel1, ecgChannel2) = ecgData

//    val ecgChannel1 = floatArrayOf(
//        0.0f, 0.003036087844520807f, 0.002440808573737741f, -0.01077798567712307f, -0.02941250056028366f, -0.02127940207719803f, 0.03379339724779129f, 0.09094017744064331f, 0.05208359658718109f, -0.1133501008152962f, -0.2622079849243164f, -0.1582041531801224f, 0.2832704782485962f, 0.8482071161270142f, 0.202911853790283f, 1.217478036880493f, 1.0064306259f, 0.5614029765129089f, -0.5808430314064026f, -3.093567848205566f, -7.158360481262207f, -12.06765747070312f, -16.67280197143555f, -20.29765510559082f, -23.17350387573242f, -25.97895050048828f, -29.04569435119629f, -32.08361053466797f, -34.61101150512695f, -36.49107360839844f, -37.83483505249023f, -39.2723503112793f, -40.31753540039062f, -41.73135375976562f, -44.40776443481445f, -48.18602752685547f, -50.971923828125f, -49.85507965087891f, -44.25748062133789f, -38.60691070556641f, -40.88425827026367f, -55.82911682128906f, -75.75099182128906f, -75.57111358642578f, -20.18935394287109f, 112.4039306640625f, 302.4443969726562f, 478.810302734375f, 549.542724609375f, 462.8594360351562f, 253.620361328125f, 29.86661911010742f, -99.09449005126953f, -97.12584686279297f, -16.40250968933105f, 55.83222579956055f, 67.73938751220703f, 30.61519622802734f, -9.710158348083496f, -20.65663146972656f, -5.159292697906494f, 12.56078052520752f, 15.47367382049561f, 5.182388305664062f, -4.580784320831299f, -4.939818382263184f, 1.16289758682251f, 5.320072650909424f, 3.525331497192383f, -0.9494332075119019f, -2.781972646713257f, -0.7593205571174622f, 1.852165102958679f, 1.934446334838867f, -0.02557146549224854f, -1.326619863510132f, -0.6621173620223999f, 0.7012819647789001f, 1.002269268035889f, 0.1525551378726959f, -0.5706530809402466f, -0.3565154075622559f, 0.3104846179485321f, 0.5506284832954407f, 0.2197907865047455f, -0.1195622012019157f, -0.04902923107147217f, 0.2566174864768982f, 0.403188943862915f, 0.3047243356704712f, 0.1968544721603394f, 0.2810082137584686f, 0.5909113883972168f, 0.5504930019378662f, 0.9481502175331116f, 0.3396361768245697f, -0.9867785573005676f, -1.6379554271698f, 0.3059036731719971f, 4.80296802520752f, 7.988834857940674f, 4.358085632324219f, -7.128910541534424f, -18.30419731140137f, -13.80635738372803f, 19.77511596679688f, 82.21820831298828f, 152.1483306884766f, 190.4971923828125f, 159.1590728759766f, 49.01980972290039f, -102.276430415726f, 0.07632210850715637f, 0.04921660572290421f, 0.02271043695509434f
//    )


    val pointsData: List<Point> = ecgChannel1.mapIndexed { index, value ->
        Point(index.toFloat(), value)
    }.toList()


    val steps = 10
    val max = getMax(pointsData)
    val min = getMin(pointsData)

    val xAxisData = AxisData.Builder()
        .axisStepSize(10.dp)
        .backgroundColor(Color.Transparent)
        .steps(pointsData.size - 1)
        .labelData { _ -> "" }
        .labelAndAxisLinePadding(15.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(steps)
        .backgroundColor(Color.Transparent)
        .labelAndAxisLinePadding(20.dp)
        .labelData { i ->
            val yScale = (2* getAbsoluteMax(min, max)) / steps.toFloat()
            String.format("%.1f", ((i * yScale) + min))
        }.build()

    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(color = Color.Green),
                    IntersectionPoint(radius = 0.dp),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(),
        backgroundColor = Color.White
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        lineChartData = lineChartData
    )
}

private fun getMax(list: List<Point>): Float{
    var max = -100F
    list.forEach { point ->
        if(max < point.y) max = point.y
    }
    //Log.d("tag2", max.toString())
    return max
}

private fun getMin(list: List<Point>): Float{
    var min = 100F
    list.forEach { point ->
        if(min > point.y) min = point.y
    }
    //Log.d("tag2", min.toString())
    return min
}

fun getAbsoluteMax(a: Float, b: Float): Float {
    //Log.d("tag2", maxOf(abs(a), abs(b)).toString())
    return maxOf(abs(a), abs(b))
}