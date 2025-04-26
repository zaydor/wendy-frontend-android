package com.zaydorstudios.wendycompanionx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zaydorstudios.wendycompanionx.interfaces.LoginForm
import com.zaydorstudios.wendycompanionx.ui.theme.WendyCompanionXTheme

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WendyCompanionXTheme {
                LoginForm()
            }
        }
    }
}
