package com.moa.app.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class PayrollResponse(
    val effectiveFrom: String,
    val salaryInputType: String,
    val salaryAmount: Int,
    val paydayDay: Int,
)
