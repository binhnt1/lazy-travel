package com.lazytravel

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.window.ComposeUIViewController
import com.lazytravel.data.models.*
import com.lazytravel.data.remote.PocketBaseClient
import com.lazytravel.ui.screens.HomeNoAuthScreen
import com.lazytravel.ui.screens.auth.SignInScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import platform.UIKit.UIViewController

enum class Screen {
    HOME_NO_AUTH,
    SIGN_IN,
    HOME_AUTH
}

@OptIn(DelicateCoroutinesApi::class)
fun MainViewController(): UIViewController {
    PocketBaseClient.initialize()

    GlobalScope.launch {
        UseCase().setup()
        Feature().setup()
        Stat().setup()

        User().setup()
        Place().setup()
        Post().setup()
        PostLike().setup()
        PostMedia().setup()
        PostShare().setup()
        PostComment().setup()
    }

    return ComposeUIViewController(
        configure = {
            enforceStrictPlistSanityCheck = false
        }
    ) {
        MaterialTheme {
            var currentScreen by remember { mutableStateOf(Screen.HOME_NO_AUTH) }

            when (currentScreen) {
                Screen.HOME_NO_AUTH -> {
                    HomeNoAuthScreen(
                        onNavigateToSignIn = {
                            currentScreen = Screen.SIGN_IN
                        }
                    )
                }

                Screen.SIGN_IN -> {
                    SignInScreen(
                        onNavigateBack = {
                            currentScreen = Screen.HOME_NO_AUTH
                        },
                        onSignInSuccess = {
                            currentScreen = Screen.HOME_AUTH
                        }
                    )
                }

                Screen.HOME_AUTH -> {
                    Text("Home Auth Screen - Coming soon")
                }
            }
        }
    }
}
