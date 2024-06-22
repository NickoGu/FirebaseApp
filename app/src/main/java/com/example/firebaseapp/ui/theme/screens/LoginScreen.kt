import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.firebaseapp.ui.theme.screens.LoginScreenViewModel
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

@Composable
fun LoginScreen(
    padding: PaddingValues,
    viewModel: LoginScreenViewModel = LoginScreenViewModel(),
    navController: NavController,
) {
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        val emailState = remember { mutableStateOf("") }
        val passwordState = remember { mutableStateOf("") }
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Correo electrónico") },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0XFF3483FA),
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Contraseña") },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0XFF3483FA),
                    ),
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = {
                    viewModel.login(
                        emailState.value,
                        passwordState.value,
                        { navController.navigate(route = "homeScreen") },
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
                Text(text = "¿No tenés una cuenta?")
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "¡Registrate acá!",
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
