package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PayrollResponse(
    val effectiveFrom: String,
    val salaryInputType: String,
    val salaryAmount: Long,
    val paydayDay: Int,
)
