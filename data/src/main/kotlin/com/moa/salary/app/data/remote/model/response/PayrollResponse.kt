package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PayrollResponse(
    val salaryInputType: String,
    val salaryAmount: Long,
)
