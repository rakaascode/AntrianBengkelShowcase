package dev.inteiintel.teduhserviceapp.presentation.main.profile.edit_profile

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dev.inteiintel.teduhserviceapp.presentation.main.profile.ProfileUiState
import dev.inteiintel.teduhserviceapp.presentation.main.profile.ProfileViewModel
import dev.inteiintel.teduhserviceapp.ui.theme.DarkOrange
import dev.inteiintel.teduhserviceapp.ui.theme.DarkSlate
import dev.inteiintel.teduhserviceapp.ui.theme.DimGray
import java.util.*

val PrimBlue = Color(0xFF101C73)
val SnowWhite = Color.White

@Composable
fun EditProfileScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController
) {
    val dataProfile by profileViewModel.getProfile.collectAsState()
    val updateState by profileViewModel.updateState.collectAsState()
    val context = LocalContext.current

    var isEditMode by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var province by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }

    val photoPickerLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            selectedImageUri = it
            profileViewModel.uploadAvatar(context, it)
        }
    }

    // Inisialisasi state hanya saat profile pertama kali didapat / diperbarui
    LaunchedEffect(dataProfile) {
        dataProfile?.let { user ->
            fullName = user.name
            email = user.email ?: ""
            phone = user.no_wa ?: ""
            address = user.alamat ?: ""
            city = user.kota ?: ""
            province = user.provinsi ?: ""
            postalCode = user.kode_pos ?: ""
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    // Handle feedback response update
    LaunchedEffect(updateState) {
        when (val state = updateState) {
            is ProfileUiState.Success -> {
                android.widget.Toast.makeText(context, "Profil berhasil diperbarui!", android.widget.Toast.LENGTH_SHORT).show()
                isEditMode = false
                profileViewModel.resetUpdateState()
            }
            is ProfileUiState.Error -> {
                android.widget.Toast.makeText(context, state.message, android.widget.Toast.LENGTH_LONG).show()
                profileViewModel.resetUpdateState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        Box(
            modifier = Modifier
                .background(SnowWhite)
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Edit Profile",
                    fontWeight = FontWeight.Bold,
                    color = DarkSlate,
                    fontSize = 18.sp
                )
            }

            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = DarkSlate
                )
            }
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = DimGray
        )

        LazyColumn(
            state = rememberLazyListState(),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            contentAlignment = Alignment.BottomEnd
                        ) {

                            val imageUrl = selectedImageUri ?: dataProfile?.avatar_url ?: "https://i.pravatar.cc/300"

                            AsyncImage(
                                model = imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(110.dp)
                                    .clip(CircleShape)
                            )

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(
                                        width = 1.dp,
                                        color = Color(0xFFE5E5E5),
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        photoPickerLauncher.launch("image/*")
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CameraAlt,
                                    contentDescription = null,
                                    tint = PrimBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Foto Profil",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "JPG, JPEG atau PNG. Maks. 2MB.",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            item {

                ProfileSection(
                ) {

                    ProfileTextField(
                        label = "Nama Lengkap",
                        placeHolder = "Masukkan Nama Lengkap",
                        value = fullName,
                        enabled = isEditMode,
                        onValueChange = {
                            fullName = it
                        }
                    )

                    ProfileTextField(
                        label = "Email",
                        placeHolder = "Masukkan Email",
                        value = email,
                        enabled = isEditMode,
                        onValueChange = {
                            email = it
                        }
                    )

                    ProfileTextField(
                        label = "Nomor Telepon",
                        placeHolder = "Masukan No Telp",
                        value = phone,
                        enabled = isEditMode,
                        onValueChange = {
                            phone = it
                        }
                    )
                }
            }

            item {

                ProfileSection(
                ) {

                    ProfileTextField(
                        label = "Alamat",
                        placeHolder = "Masukan Alamat (Opsional)",
                        value = address,
                        enabled = isEditMode,
                        singleLine = false,
                        minLines = 3,
                        onValueChange = {
                            address = it
                        }
                    )

                    ProfileTextField(
                        label = "Kota",
                        placeHolder = "Masukan Nama Kota (Opsional)",
                        value = city,
                        enabled = isEditMode,
                        onValueChange = {
                            city = it
                        }
                    )

                    ProfileTextField(
                        label = "Provinsi",
                        placeHolder = "Masukan Nama Provinsi (Opsional)",
                        value = province,
                        enabled = isEditMode,
                        onValueChange = {
                            province = it
                        }
                    )

                    ProfileTextField(
                        label = "Kode Pos",
                        placeHolder = "Masukan Kode Pos (Opsional)",
                        value = postalCode,
                        enabled = isEditMode,
                        onValueChange = {
                            postalCode = it
                        }
                    )
                }
            }

            item {
                val isLoading = updateState is ProfileUiState.Loading

                Button(
                    onClick = {
                        if (isEditMode) {
                            if (fullName.isBlank()) {
                                android.widget.Toast.makeText(context, "Nama lengkap tidak boleh kosong", android.widget.Toast.LENGTH_SHORT).show()
                            } else {
                                profileViewModel.updateProfile(
                                    name = fullName,
                                    alamat = address,
                                    kota = city,
                                    provinsi = province,
                                    kodePos = postalCode
                                )
                            }
                        } else {
                            isEditMode = true
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimBlue
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (isEditMode) {
                                "Simpan Profile"
                            } else {
                                "Edit Profile"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SnowWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ProfileSection(
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            content()
        }
    }
}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    placeHolder: String,
    enabled: Boolean,
    singleLine: Boolean = true,
    minLines: Int = 1,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {

    Column {

        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.DarkGray,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,

            placeholder = {
                Text(placeHolder)
            },

            enabled = enabled,
            singleLine = singleLine,
            minLines = minLines,
            trailingIcon = trailingIcon,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth(),

            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = Color(0xFFE0E0E0),
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = PrimBlue,
                disabledContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            )
        )
    }
}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewEditProfile() {
//    EditProfileScreen()
//}