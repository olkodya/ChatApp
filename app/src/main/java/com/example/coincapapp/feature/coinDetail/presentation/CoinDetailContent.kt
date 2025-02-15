package com.example.coincapapp.feature.coinDetail.presentation

import android.view.MotionEvent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.coincapapp.R
import com.example.coincapapp.components.ErrorState
import com.example.coincapapp.components.LoadingState
import com.example.coincapapp.ui.theme.DecreaseColor
import com.example.coincapapp.ui.theme.IncreaseColor
import com.example.coincapapp.utils.DecimalValueFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import kotlinx.collections.immutable.persistentListOf
import java.math.BigDecimal

@Composable
fun CoinDetailContent(
    state: CoinDetailScreenState,
    handleAction: (CoinDetailViewModel.CoinDetailAction) -> Unit,
) {
    Scaffold(topBar = {
        CoinDetailTopAppBar(
            coinName = state.coinName, handleAction
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.current_price, state.currentPrice),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (state.coinPriceIncreased) {
                        IncreaseColor
                    } else {
                        DecreaseColor
                    }
                )

                when {

                    state.isLoading -> {
                        LoadingState(Modifier.fillMaxSize())
                    }

                    state.hasError != null -> {
                        (if (state.hasError.equals("null")) stringResource(R.string.error_string) else state.hasError)?.let {
                            ErrorState(
                                Modifier.fillMaxSize(),
                                message = it,
                            ) {
                                handleAction(
                                    CoinDetailViewModel.CoinDetailAction.OnRetryClick(
                                        state.coinId, state.coinName, state.currentPrice
                                    )
                                )
                            }
                        }
                    }

                    state.coinPriceHistory.isNotEmpty() -> {
                        Chart(state, handleAction)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoinDetailTopAppBar(
    coinName: String,
    handleAction: (CoinDetailViewModel.CoinDetailAction) -> Unit,
) {
    TopAppBar(title = { Text(text = coinName) }, navigationIcon = {
        IconButton(onClick = { handleAction(CoinDetailViewModel.CoinDetailAction.OnBackClick) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
            )
        }
    })
}

@Composable
fun Chart(
    state: CoinDetailScreenState, handleAction: (CoinDetailViewModel.CoinDetailAction) -> Unit
) {
    LineGraph(
        data = state.coinPriceHistory.map { it.data },
        dataLabel = "${state.coinName} price",
        xLabels = state.coinPriceHistory.mapIndexed { index, it -> if (index % 2 == 0) it.time else "" },
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        position = state.currentChartPosition,
        handleAction = handleAction,
    )
}

@Composable
fun LineGraph(
    data: List<Entry>,
    xLabels: List<String>,
    dataLabel: String,
    modifier: Modifier = Modifier,
    position: Float,
    handleAction: (CoinDetailViewModel.CoinDetailAction) -> Unit,
) {
    val context = LocalContext.current
    val lineChart = LineChart(context)
    val color = MaterialTheme.colorScheme.primary.toArgb()
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface.toArgb()
    setupChart(lineChart, data, dataLabel, color, onSurfaceColor, xLabels) {}
    AndroidView(modifier = modifier.fillMaxSize(), factory = {
        setupChart(lineChart, data, dataLabel, color, onSurfaceColor, xLabels) {}
        val visibleEntries = lineChart.highestVisibleX
        if (position != 0.0f) {
            lineChart.moveViewToX(position - 9)
        } else {
            lineChart.moveViewToX(data.last().x)
        }
        handleAction(
            CoinDetailViewModel.CoinDetailAction.OnChangePosition(visibleEntries)
        )
        lineChart
    }, update = { view ->
        setupChart(view, data, dataLabel, color, onSurfaceColor, xLabels) {}
        val visibleEntries = view.highestVisibleX
        val isAtEnd = visibleEntries >= data.size - 9
        if (isAtEnd && data.isNotEmpty()) {
            view.moveViewToX(data.last().x)
        } else view.invalidate()
        handleAction(
            CoinDetailViewModel.CoinDetailAction.OnChangePosition(visibleEntries)
        )
    })
}

fun setupChart(
    chart: LineChart,
    data: List<Entry>,
    dataLabel: String,
    chartColor: Int,
    onSurfaceColor: Int,
    xLabels: List<String>,
    handleAction: (CoinDetailViewModel.CoinDetailAction) -> Unit,
) {
    val dataSet = LineDataSet(data, dataLabel).apply {
        lineWidth = 4f
        circleRadius = 6f
        color = chartColor
        setCircleColor(chartColor)
        valueTextSize = 14f
        valueTextColor = onSurfaceColor
        valueFormatter = DecimalValueFormatter()
    }
    chart.data = LineData(dataSet)
    chart.setExtraOffsets(10f, 10f, 10f, 10f)
    chart.setTouchEnabled(true)
    chart.isDragEnabled = true
    chart.description.isEnabled = false
    chart.isScaleXEnabled = false
    chart.isScaleYEnabled = false
    chart.legend.isEnabled = false
    chart.xAxis.setDrawGridLines(false)
    chart.xAxis.axisLineColor = onSurfaceColor
    chart.xAxis.textColor = onSurfaceColor
    chart.xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
    chart.xAxis.apply {
        textSize = 12f
        position = XAxis.XAxisPosition.BOTTOM
    }
    chart.axisRight.apply {
        textSize = 12f
        spaceMax = 40f
    }
    chart.axisLeft.isEnabled = false
    chart.axisRight.setDrawGridLines(true)
    chart.axisRight.axisLineColor = onSurfaceColor
    chart.axisRight.textColor = onSurfaceColor
    chart.axisRight.gridColor = onSurfaceColor
    chart.setVisibleXRange(9f, 9f)
    chart.onChartGestureListener = object : OnChartGestureListener {
        override fun onChartGestureStart(
            me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?
        ) {
        }

        override fun onChartGestureEnd(
            me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?
        ) {
        }

        override fun onChartLongPressed(me: MotionEvent?) {
        }

        override fun onChartDoubleTapped(me: MotionEvent?) {
        }

        override fun onChartSingleTapped(me: MotionEvent?) {
        }

        override fun onChartFling(
            me1: MotionEvent?, me2: MotionEvent?, velocityX: Float, velocityY: Float
        ) {
        }

        override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
        }

        override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
            handleAction(
                CoinDetailViewModel.CoinDetailAction.OnChangePosition(dX)
            )
        }

    }
}

@Composable
@Preview(showBackground = true)
fun CoinDetailChartPreview() {
    val state = CoinDetailScreenState(
        coinId = "bitcoin",
        coinName = "Bitcoin",
        coinPriceHistory = persistentListOf(
            CoinDetailState(data = Entry(0F, 95000.43F), time = "00:38:00"),
            CoinDetailState(data = Entry(1F, 95000.41F), time = "00:38:05"),
            CoinDetailState(data = Entry(2F, 95001.43F), time = "00:38:10"),
            CoinDetailState(data = Entry(3F, 95001.02F), time = "00:38:15"),
        ),
        currentPrice = BigDecimal("95000.0"),
        loading = false,
        errorMessage = null,
        currentChartPosition = 3f
    )
    CoinDetailContent(state) {}
}

@Composable
@Preview(showBackground = true)
fun CoinDetailErrorStatePreview() {
    val state = CoinDetailScreenState(
        coinId = "bitcoin",
        coinName = "Bitcoin",
        coinPriceHistory = persistentListOf(),
        currentPrice = BigDecimal("95000.0"),
        loading = false,
        errorMessage = "Error",
        currentChartPosition = 0f
    )
    CoinDetailContent(state) {}
}

@Composable
@Preview(showBackground = true)
fun CoinDetailLoadingStatePreview() {
    val state = CoinDetailScreenState(
        coinId = "bitcoin",
        coinName = "Bitcoin",
        coinPriceHistory = persistentListOf(),
        currentPrice = BigDecimal("95000.0"),
        loading = true,
        errorMessage = null,
        currentChartPosition = 0f
    )
    CoinDetailContent(state) {}
}
