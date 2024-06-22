import android.widget.Toast
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

@Composable
fun SignupScreen(
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
                text = "Crear cuenta",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text("Correo electrónico") },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0XFF3483FA),
                    ),
            )

            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text("Contraseña") },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                textStyle = MaterialTheme.typography.bodyMedium,
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0XFF3483FA),
                    ),
            )
            Button(
                onClick = {
                    viewModel.createAccount(
                        emailState.value,
                        passwordState.value,
                        "UnlamAnónimo",
                        @Suppress("ktlint:standard:max-line-length")
                        "https://seeklogo.com/images/U/unlam-universidad-nacional-de-la-matanza-logo-B665E562AA-seeklogo.com.png",
                        {
                            Toast.makeText(context, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show()
                        },
                        context = context,
                    )
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0XFF3483FA), contentColor = Color.White),
            ) {
                Text("Crear cuenta")
            }
        }
    }
}
