package com.zaydorstudios.wendycompanionx.interfaces

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaydorstudios.wendycompanionx.MainActivity
import com.zaydorstudios.wendycompanionx.ui.theme.WendyCompanionXTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.parameters
import kotlinx.coroutines.runBlocking

@Composable
fun LoginForm(modifier: Modifier = Modifier) {
    Surface {
        var credentials by remember { mutableStateOf(Credentials()) }
        val context = LocalContext.current

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
        ) {
            Text(text = "Wendy Companion X", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(100.dp))
            EmailField(
                value = credentials.email,
                onChange = { data -> credentials = credentials.copy(email = data) },
                modifier = Modifier.fillMaxWidth(),
            )
            PasswordField(
                value = credentials.password,
                onChange = { data -> credentials = credentials.copy(password = data) },
                submit = { if (!checkCredentials(credentials, context)) credentials = Credentials() },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(10.dp))
            LabeledCheckbox(
                label = "Remember Me",
                onCheckChange = { credentials = credentials.copy(remember = !credentials.remember) },
                isChecked = credentials.remember,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    if (!checkCredentials(credentials, context)) credentials = Credentials()
                },
                enabled = credentials.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Login")
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    runBlocking {
                        pingBackend()
                    }
                },
                enabled = true,
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Sign Up")
            }
        }
    }
}

private suspend fun pingBackend() {
    // adb reverse tcp:3000 tcp:80
    val client = HttpClient(CIO)
    val response: HttpResponse = client.get("http://localhost:3000")

    println(response.bodyAsText())
    client.close()
}

@Composable
fun EmailField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Email",
    placeholder: String = "Enter your Email",
) {
    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = colorScheme.primary,
        )
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions =
            KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = VisualTransformation.None,
    )
}

@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password",
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Key,
            contentDescription = "",
            tint = colorScheme.primary,
        )
    }

    val trailingIcon = @Composable {
        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon(
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = colorScheme.primary,
            )
        }
    }

    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions =
            KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { submit() },
            ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
    )
}

@Composable
fun LabeledCheckbox(
    label: String,
    onCheckChange: () -> Unit,
    isChecked: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        Modifier.clickable(onClick = onCheckChange).padding(4.dp),
    ) {
        Checkbox(checked = isChecked, onCheckedChange = null)
        Spacer(Modifier.size(6.dp))
        Text(label)
    }
}

data class Credentials(
    var email: String = "",
    var password: String = "",
    var remember: Boolean = false,
) {
    fun isNotEmpty(): Boolean = email.isNotEmpty() && password.isNotEmpty()
}

suspend fun checkLoginWithBackend(credentials: Credentials): Boolean {
    val client = HttpClient(CIO)
    val response: HttpResponse =
        client.submitForm(
            "http://localhost:3000/result",
            formParameters =
                parameters {
                    append("email", credentials.email)
                    append("password", credentials.password)
                },
        )

    client.close()
    println(response)
    return response.status.value == 200
}

fun checkCredentials(
    credentials: Credentials,
    context: Context,
): Boolean {
    var isLoginValid: Boolean
    runBlocking {
        isLoginValid = checkLoginWithBackend(credentials)
    }
    if (isLoginValid) {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as Activity).finish()
        return true
    } else {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        return false
    }
}

@Preview
@Composable
private fun LoginFormPreview() {
    WendyCompanionXTheme { LoginForm() }
}

@Preview
@Composable
private fun LoginFormPreviewDark() {
    WendyCompanionXTheme(darkTheme = true) {
        LoginForm()
    }
}
