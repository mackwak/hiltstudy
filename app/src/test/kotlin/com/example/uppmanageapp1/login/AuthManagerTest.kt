package com.example.uppmanageapp1.login

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import org.mockito.kotlin.whenever // Add this import
import org.mockito.kotlin.any      // Add this import
import org.mockito.kotlin.mock     // Add this import
import java.lang.Exception
import java.util.concurrent.atomic.AtomicBoolean

class AuthManagerTest {

    @Test
    fun isUserLoggedIn_returnsFalse_whenNoCurrentUser() {
        val mockAuth = mock<FirebaseAuth>()
        whenever(mockAuth.currentUser).thenReturn(null)

        val manager = AuthManager(mockAuth)
        assertFalse(manager.isUserLoggedIn())
    }

    @Test
    fun isUserLoggedIn_returnsTrue_whenCurrentUserPresent() {
        val mockAuth = mock<FirebaseAuth>()
        val mockUser = mock<FirebaseUser>()
        whenever(mockAuth.currentUser).thenReturn(mockUser)

        val manager = AuthManager(mockAuth)
        assertTrue(manager.isUserLoggedIn())
    }

    @Test
    fun signUp_sendsEmailVerification_and_invokesCallbackOnSuccessAndFailure() {
        val mockAuth = mock<FirebaseAuth>()
        val successTask = mock<Task<AuthResult>>()
        val failureTask = mock<Task<AuthResult>>()
        val mockUser = mock<FirebaseUser>()

        // FIX: Using whenever avoids 'when' keyword issues
        whenever(successTask.isSuccessful).thenReturn(true)
        whenever(failureTask.isSuccessful).thenReturn(false)

        val failureEx = Exception("CreateFailed")
        whenever(failureTask.exception).thenReturn(failureEx)

        // For Firebase Tasks, we need to mock the listener behavior
        doAnswer { invocation ->
            val listener = invocation.getArgument<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(successTask)
            successTask
        }.whenever(successTask).addOnCompleteListener(any())

        doAnswer { invocation ->
            val listener = invocation.getArgument<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(failureTask)
            failureTask
        }.whenever(failureTask).addOnCompleteListener(any())

        whenever(mockAuth.createUserWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(successTask)
            .thenReturn(failureTask)

        whenever(mockAuth.currentUser).thenReturn(mockUser)

        val manager = AuthManager(mockAuth)

        // Test Success Case
        val createdFlag = AtomicBoolean(false)
        manager.signUp("new@user.com", "pwd") { ok, _ -> createdFlag.set(ok) }
        assertTrue(createdFlag.get())
        verify(mockUser).sendEmailVerification()

        // Test Failure Case
        val failureFlag = AtomicBoolean(true)
        manager.signUp("new@user.com", "bad") { ok, _ -> failureFlag.set(ok) }
        assertFalse(failureFlag.get())
    }

    @Test
    fun logout_callsFirebaseSignOut() {
        val mockAuth = mock<FirebaseAuth>()
        val manager = AuthManager(mockAuth)

        manager.logout()

        verify(mockAuth).signOut()
    }

    @Test
    fun currentUser_returnsUnderlyingFirebaseUser() {
        val mockAuth = mock<FirebaseAuth>()
        val mockUser = mock<FirebaseUser>()
        whenever(mockAuth.currentUser).thenReturn(mockUser)

        val manager = AuthManager(mockAuth)

        assertSame(mockUser, manager.currentUser)
    }

    @Test
    fun getUserId_returnsUidWhenUserPresent() {
        val mockAuth = mock<FirebaseAuth>()
        val mockUser = mock<FirebaseUser>()
        whenever(mockUser.uid).thenReturn("uid123")
        whenever(mockAuth.currentUser).thenReturn(mockUser)

        val manager = AuthManager(mockAuth)

        assertEquals("uid123", manager.getUserId())
    }

    @Test
    fun getUserId_returnsNullWhenNoUser() {
        val mockAuth = mock<FirebaseAuth>()
        whenever(mockAuth.currentUser).thenReturn(null)

        val manager = AuthManager(mockAuth)

        assertNull(manager.getUserId())
    }

    @Test
    fun signIn_invokesCallbackOnSuccessAndFailure() {
        val mockAuth = mock<FirebaseAuth>()
        val successTask = mock<Task<AuthResult>>()
        val failureTask = mock<Task<AuthResult>>()

        whenever(successTask.isSuccessful).thenReturn(true)
        whenever(failureTask.isSuccessful).thenReturn(false)

        val failureEx = Exception("SignInFailed")
        whenever(failureTask.exception).thenReturn(failureEx)

        doAnswer { invocation ->
            val listener = invocation.getArgument<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(successTask)
            successTask
        }.whenever(successTask).addOnCompleteListener(any())

        doAnswer { invocation ->
            val listener = invocation.getArgument<OnCompleteListener<AuthResult>>(0)
            listener.onComplete(failureTask)
            failureTask
        }.whenever(failureTask).addOnCompleteListener(any())

        whenever(mockAuth.signInWithEmailAndPassword(anyString(), anyString()))
            .thenReturn(successTask)
            .thenReturn(failureTask)

        val manager = AuthManager(mockAuth)

        val successFlag = AtomicBoolean(false)
        manager.signIn("user@example.com", "pwd") { ok, _ -> successFlag.set(ok) }
        assertTrue(successFlag.get())

        val failureFlag = AtomicBoolean(true)
        var error: String? = null
        manager.signIn("user@example.com", "bad") { ok, err -> failureFlag.set(ok); error = err }
        assertFalse(failureFlag.get())
        assertEquals("SignInFailed", error)
    }
}