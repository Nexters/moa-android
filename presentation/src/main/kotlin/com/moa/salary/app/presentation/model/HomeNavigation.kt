package com.moa.salary.app.presentation.model

import com.moa.salary.app.core.model.work.Home
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavigation : RootNavigation {
    val home : Home

    @Serializable
    data class BeforeWork(
        override val home: Home,
    ) : HomeNavigation

    @Serializable
    data class Working(
        override val home: Home,
        val showWorkCompletionOverlay: Boolean = false,
    ) : HomeNavigation

    @Serializable
    data class AfterWork(
        override val home: Home,
    ) : HomeNavigation
}