package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class HomeResponse(
    val workplace: String?,
    val workedEarnings: Long,
    val standardSalary: Long,
    val dailyPay: Long,
    val type: HomeType,
    val clockInTime: String?,
    val clockOutTime: String?,
)

@Serializable
enum class HomeType {
    NONE,
    WORK,
    VACATION,
}