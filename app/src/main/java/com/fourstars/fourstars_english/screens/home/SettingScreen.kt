package com.fourstars.fourstars_english.screens.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fourstars.fourstars_english.repository.UserRoleViewModel
import com.fourstars.fourstars_english.ui.theme.Feather
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    onLogout: () -> Unit,
    userRoleViewModel: UserRoleViewModel = viewModel()
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser

    // Kiểm tra role khi Composable được tạo
    LaunchedEffect(Unit) {
        user?.uid?.let { userRoleViewModel.checkUserRole(it) }
        Log.e("------ ROLE:", userRoleViewModel.isAdmin.toString())
    }

    val isAdmin = userRoleViewModel.isAdmin

    var showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        ResetPasswordDialog(
            onDismiss = { showDialog.value = false },
            onSendEmail = { email ->
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setting", fontWeight = FontWeight.Bold, fontFamily = Feather) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 24.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Cài đặt",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 18.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 👉 Nút Quản trị nếu là admin
            if (isAdmin) {
                SettingItem(
                    icon = Icons.Default.Settings,
                    text = "Admin dashboard"
                ) {
                    navController.navigate("admin_dashboard")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Đăng xuất
            SettingItem(
                icon = Icons.Default.ExitToApp,
                text = "Logout"
            ) {
                FirebaseAuth.getInstance().signOut()
                onLogout()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Đổi mật khẩu
//            SettingItem(
//                icon = Icons.Default.Lock,
//                text = "Đổi mật khẩu"
//            ) {
//                showDialog.value = true
//            }
        }
    }
}


@Composable
fun SettingItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontSize = 18.sp)
    }
}

@Composable
fun ResetPasswordDialog(
    onDismiss: () -> Unit,
    onSendEmail: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Đặt lại mật khẩu") },
        text = {
            Column {
                Text("Nhập email để đặt lại mật khẩu:")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") }
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSendEmail(email)
                onDismiss()
            }) {
                Text("Gửi")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}