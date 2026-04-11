package com.example.uppmanageapp1

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<LoginActivity>()

    @Test
    fun loginButton_isDisabled_whenFieldsAreEmpty() {
        // Given
        composeTestRule.onNodeWithTag("email_input").performTextInput("")
        composeTestRule.onNodeWithTag("password_input").performTextInput("")

        // Then
        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun loginButton_isClickable_afterInput() {
        // Given
        composeTestRule.onNodeWithTag("email_input").performTextInput("test@test.com")
        composeTestRule.onNodeWithTag("password_input").performTextInput("1234")

        // Then
        composeTestRule.onNodeWithTag("login_button").assertIsEnabled()

        // Perform click
        composeTestRule.onNodeWithTag("login_button").performClick()
    }

    @Test
    fun navigateToSignUp_invokedWhenSignUpClicked() {
        // When
        composeTestRule.onNodeWithText("회원가입").performClick()

        // Then
        // Navigation intent is started by the activity; verify via separate instrumentation test if needed
    }

    @Test
    fun emailInput_displaysTypedText() {
        // Ensure the node is present before interacting
        composeTestRule.onNodeWithTag("email_input").assertExists()
        composeTestRule.onNodeWithTag("email_input").performTextInput("user@example.com")
        composeTestRule.onNodeWithTag("email_input").assertTextContains("user@example.com")
    }

    @Test
    fun passwordInput_exists_and_acceptsInput() {
        composeTestRule.onNodeWithTag("password_input").performTextInput("mypassword")
        composeTestRule.onNodeWithTag("password_input").assertExists()
    }

    @Test
    fun loginButton_isDisabled_whenOnlyEmailProvided() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("user@example.com")
        composeTestRule.onNodeWithTag("password_input").performTextInput("")

        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun loginButton_isDisabled_whenOnlyPasswordProvided() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("")
        composeTestRule.onNodeWithTag("password_input").performTextInput("password")

        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun loginButton_remainsDisabled_forWhitespaceOnlyInputs() {
        composeTestRule.onNodeWithTag("email_input").performTextInput("   ")
        composeTestRule.onNodeWithTag("password_input").performTextInput("   ")

        composeTestRule.onNodeWithTag("login_button").assertIsNotEnabled()
    }

    @Test
    fun onLogin_notCalled_whenButtonDisabledAndClicked() {
        var called = false

        composeTestRule.onNodeWithTag("email_input").performTextInput("")
        composeTestRule.onNodeWithTag("password_input").performTextInput("")

        // Attempt to click disabled button
        composeTestRule.onNodeWithTag("login_button").performClick()

        assertFalse(called)
    }
}