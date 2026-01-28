package com.moa.app.presentation.model

enum class WithdrawalReason(val title: String) {
    Error("앱 오류로 사용하기 불편해요"),
    Feature("원하는 기능이 부족해요"),
    Usability("서비스 이용이 복잡하거나 불편해요"),
    Salary("급여 계산이 실제와 달라요"),
    Usage("자주 사용하지 않아요"),
    Privacy("개인정보 · 보안이 걱정돼요"),
}