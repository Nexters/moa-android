package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AgreementsResponse(
    val agreements: List<AgreementResponse>,
    val hasRequiredTermsAgreed: Boolean,
)

@Serializable
data class AgreementResponse(
    val code: String,
    val agreed: Boolean,
)
