package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class EarningsResponse(
    val workedEarnings: Long,
    val standardSalary: Long,
    val workedMinutes: Int,
    val standardMinutes: Int,
)