package com.moa.app.presentation.manager

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

object KakaoLoginManager {
    interface OnKakaoLoginResultListener {
        fun onSuccess(accessToken: String)
        fun onFailure(errorMsg: String)
    }

    private val kakaoApiClient = UserApiClient.instance

    fun loginWithKakao(
        context: Context,
        listener: OnKakaoLoginResultListener,
    ) {
        if (kakaoApiClient.isKakaoTalkLoginAvailable(context)) {
            kakaoApiClient.loginWithKakaoTalk(context) { token, error ->
                setLoginResult(token, error, listener)
            }
        } else {
            kakaoApiClient.loginWithKakaoAccount(context) { token, error ->
                setLoginResult(token, error, listener)
            }
        }
    }

    private fun setLoginResult(
        token: OAuthToken?,
        error: Throwable?,
        listener: OnKakaoLoginResultListener
    ) {
        when {
            error != null -> listener.onFailure(error.message ?: "카카오 계정으로 로그인 실패")
            token != null -> listener.onSuccess(token.accessToken)
            else -> listener.onFailure("카카오 계정으로 로그인 실패")
        }
    }

    fun logoutKakao() {
        kakaoApiClient.logout { error ->
            if (error != null) {
                Log.e("kakao", error.message.toString())
            } else {
                Log.d("kakao", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
        }
    }
}