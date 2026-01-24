package com.moa.app.presentation.model

enum class Terms(
    val title: String,
    val url: String,
) {
    Service(
        title = "이용약관",
        url = "https://naver.com",
    ),
    Privacy(
        title = "개인정보처리방침",
        url = "https://naver.com",
    ),
    Marketing(
        title = "마케팅 정보 수신 이용약관",
        url = "https://naver.com",
    ),
}