package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class PayrollRequest(
    val effectiveFrom: String = LocalDate.now().toString(),
    val salaryInputType: String,
    val salaryAmount: Long,
    val paydayDay: Int,
)