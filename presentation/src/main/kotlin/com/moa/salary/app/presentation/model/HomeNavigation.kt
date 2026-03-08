package com.moa.salary.app.presentation.model

import com.moa.salary.app.core.model.work.Home
import kotlinx.serialization.Serializable

@Serializable
sealed interface HomeNavigation : RootNavigation {
    @Serializable
    data class BeforeWork(
        val home: Home,
    ) : HomeNavigation

    @Serializable
    data class Working(
        val home: Home,
    ) : HomeNavigation

    @Serializable
    data class AfterWork(
        val home: Home,
    ) : HomeNavigation
}