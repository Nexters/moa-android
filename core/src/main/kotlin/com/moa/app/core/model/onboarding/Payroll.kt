package com.moa.app.core.model.onboarding

data class Payroll(
    val salaryType: SalaryType,
    val salary: String,
    val paydayDay: Int = 25,
) {
    enum class SalaryType(val title: String) {
        MONTHLY("월급"),
        ANNUAL("연봉")
    }
}
