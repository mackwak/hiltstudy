package com.example.uppmanageapp1.login

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    // 현재 로그인된 유저 확인
    val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    // 로그인 여부 확인
    fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    // 로그아웃
    fun logout() {
        firebaseAuth.signOut()
    }

    // 이메일 로그인 함수 예시
    fun signIn(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // 유저 생성(회원가입)
    fun signUp(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 선택: 이메일 인증 보내기
                    firebaseAuth.currentUser?.sendEmailVerification()
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // 유저 UID 가져오기
    fun getUserId(): String? = firebaseAuth.currentUser?.uid
}