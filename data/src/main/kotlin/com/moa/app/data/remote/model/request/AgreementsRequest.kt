package com.moa.app.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AgreementsRequest(
    val agreements: List<AgreementRequest>,
)

@Serializable
data class AgreementRequest(
    val code: String,
    val agreed: Boolean,
)
