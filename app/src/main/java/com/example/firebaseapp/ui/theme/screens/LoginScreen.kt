import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebaseapp.ui.theme.screens.LoginScreenViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

@Composable
fun LoginScreen(
    padding: PaddingValues,
    viewModel: LoginScreenViewModel = LoginScreenViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    val remoteConfig = Firebase.remoteConfig

    // Variables de estado para los valores de Remote Config
    var welcomeMessage by remember { mutableStateOf(remoteConfig.getString("textConfig")) }
    var themeDateTime by remember { mutableStateOf(remoteConfig.getBoolean("booleanConfig")) }

    // Fetch de Remote Config
    remoteConfig
        .fetchAndActivate()
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                welcomeMessage = remoteConfig.getString("textConfig")
                themeDateTime = remoteConfig.getBoolean("booleanConfig")
            }
        }

    Surface(modifier = Modifier.fillMaxSize()) {
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(if (themeDateTime) Color.DarkGray else Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = welcomeMessage,
                modifier = Modifier.padding(bottom = 24.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = (if (themeDateTime) Color.White else Color.DarkGray),
            )
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp),
                color = (if (themeDateTime) Color.White else Color.DarkGray),
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Correo electrónico") },
                shape = RoundedCornerShape(64.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0XFF3483FA),
                        disabledTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        unfocusedTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedSupportingTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        disabledSupportingTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        unfocusedPlaceholderColor = if (themeDateTime) Color.White else Color.DarkGray,
                        disabledPlaceholderColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedPlaceholderColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedLabelColor = if (themeDateTime) Color.White else Color.DarkGray,
                        unfocusedLabelColor = if (themeDateTime) Color.White else Color.DarkGray,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Contraseña") },
                shape = RoundedCornerShape(64.dp),
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0XFF3483FA),
                        disabledTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        unfocusedTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedSupportingTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        disabledSupportingTextColor = if (themeDateTime) Color.White else Color.DarkGray,
                        unfocusedPlaceholderColor = if (themeDateTime) Color.White else Color.DarkGray,
                        disabledPlaceholderColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedPlaceholderColor = if (themeDateTime) Color.White else Color.DarkGray,
                        focusedLabelColor = if (themeDateTime) Color.White else Color.DarkGray,
                        unfocusedLabelColor = if (themeDateTime) Color.White else Color.DarkGray,
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = {
                    viewModel.login(
                        emailState.value,
                        passwordState.value,
                        {
                            firebaseAnalytics.logEvent("login") {
                                param("email", emailState.value)
                                param("password", passwordState.value)
                            }
                            navController.navigate(route = "editUser")
                        },
                        context = context,
                    )
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF3483FA),
                        contentColor = Color.White,
                    ),
            ) {
                Text("Iniciar sesión")
            }
            Row {
                Text(text = "¿No tenés una cuenta?", color = (if (themeDateTime) Color.White else Color.DarkGray))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "¡Registrate acá!",
                    color = (if (themeDateTime) Color.White else Color.DarkGray),
                    modifier = Modifier.clickable { navController.navigate(route = "signUp") },
                )
            }
            Button(
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0Xeeff3300),
                    ),
                modifier = Modifier.padding(top = 100.dp),
                onClick = {
                    Firebase.crashlytics.log("Boton Para Crash Presionado")
                    throw RuntimeException("This is a test crash")
                },
            ) {
                Text("Botón Bomba")
            }
        }
    }
}
