package com.moa.app.data.repository

import com.moa.app.core.extensions.toCurrencyFormat
import com.moa.app.core.model.widget.Widget
import com.moa.app.core.util.TimeUtil
import javax.inject.Inject
import kotlin.random.Random

class WidgetRepositoryImpl @Inject constructor() : WidgetRepository {
    override suspend fun getWidget(): Widget {
        val widgetType = Random.nextInt(3)
        val currentTime = TimeUtil.getCurrentTimeKorean()

        return when (widgetType) {
            0 -> {
                val daySalary = Random.nextInt(50_000, 300_001)
                Widget.Working(
                    daySalary = daySalary.toCurrencyFormat(),
                    time = currentTime
                )
            }
            1 -> {
                val daySalary = Random.nextInt(50_000, 300_001)
                Widget.Vacation(
                    daySalary = daySalary.toCurrencyFormat(),
                    time = currentTime
                )
            }
            else -> {
                val monthlySalary = Random.nextInt(2_000_000, 6_000_001)
                Widget.Finish(
                    monthlySalary = monthlySalary.toCurrencyFormat()
                )
            }
        }
    }
}