package com.moa.salary.app.data.remote.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
    val id: Long,
    val provider: String,
)
