package com.zaydorstudios.wendycompanionx

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zaydorstudios.wendycompanionx.ui.theme.WendyCompanionXTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WendyCompanionXTheme {
                WendyCompanionXApp("isaiah")
            }
        }
    }

    @Preview
    @Composable
    private fun Preview() {
        WendyCompanionXTheme {
            WendyCompanionXApp("isaiah")
        }
    }

    @Preview
    @Composable
    private fun PreviewDark() {
        WendyCompanionXTheme(darkTheme = true) {
            WendyCompanionXApp("isaiah dark")
        }
    }

    @Composable
    fun WendyCompanionXApp(
        name: String,
        modifier: Modifier = Modifier,
    ) {
        Surface {
            val context = LocalContext.current
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = "Hello $name",
                )
                Spacer(Modifier.padding(30.dp))
                Button(
                    onClick = {
                        toLoginActivity(context)
                    },
                    shape = RoundedCornerShape(5.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("Log Out") }
            }
        }
    }

    private suspend fun logout() {
        val client = HttpClient(CIO)
        val response: HttpResponse =
            client.get(
                "http://localhost:3000/logout",
            )
        println(response)
        client.close()
    }

    private fun toLoginActivity(context: Context) {
        runBlocking {
            logout()
        }
        context.startActivity(Intent(context, LoginActivity::class.java))
        (context as Activity).finish()
    }
}
