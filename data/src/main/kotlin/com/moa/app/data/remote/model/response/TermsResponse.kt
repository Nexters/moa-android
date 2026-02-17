package com.moa.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class TermsResponse(
    val terms: List<TermResponse>,
)

@Serializable
data class TermResponse(
    val code: String,
    val title: String,
    val required: Boolean,
    val contentUrl: String,
)
