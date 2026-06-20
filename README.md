<div align="center">

<img src="app/src/main/res/drawable/ic_launcher_foreground.png" alt="Teduh Service App" width="120"/>

# 🔧 Teduh Service App

**Aplikasi antrian bengkel Yamaha berbasis Android**  
Scan STNK → Pilih Cabang → Ambil Antrian — tanpa perlu antri di tempat.

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-2025.01-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Min SDK](https://img.shields.io/badge/Min_SDK-24_(Android_7)-blue)](https://developer.android.com/about/versions/nougat)
[![Version](https://img.shields.io/badge/Versi-1.3_(versionCode_4)-orange)](#)

</div>

---

## 📋 Daftar Isi

- [Tentang Aplikasi](#-tentang-aplikasi)
- [Fitur Utama](#-fitur-utama)
- [Screenshot](#-screenshot)
- [Arsitektur](#-arsitektur)
- [Tech Stack](#-tech-stack)
- [Struktur Proyek](#-struktur-proyek)
- [Alur Navigasi](#-alur-navigasi)
- [Setup & Instalasi](#-setup--instalasi)
- [Konfigurasi](#-konfigurasi)
- [API Reference](#-api-reference)
- [Lisensi](#-lisensi)

---

## 🚀 Tentang Aplikasi

**Teduh Service App** adalah aplikasi Android yang memungkinkan pelanggan bengkel Yamaha untuk:

- Mengambil nomor antrian servis dari mana saja
- Memindai STNK menggunakan kamera untuk mengisi data kendaraan secara otomatis (OCR)
- Melihat estimasi waktu antrian secara real-time per cabang
- Mendapatkan notifikasi broadcast dari bengkel
- Menyimpan data kendaraan untuk penggunaan ulang

---

## ✨ Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| 📸 **Scan STNK (OCR)** | Scan foto STNK dengan kamera atau galeri → data kendaraan terisi otomatis |
| 📋 **Ambil Antrian** | Alur multi-langkah: isi data → pilih estimasi → pilih cabang → konfirmasi |
| 💾 **Data Tersimpan** | Simpan data kendaraan secara lokal (Room DB) untuk dipakai ulang |
| 🏪 **Detail Cabang** | Info lengkap cabang bengkel termasuk estimasi antrian real-time |
| 🔔 **Notifikasi** | Broadcast pengumuman dari bengkel |
| 🕐 **Pengingat Servis** | Reminder servis via WhatsApp |
| 🔐 **Google Sign-In** | Login aman via Google OAuth 2.0 (Credential Manager API) |
| 🌍 **Lokasi Terdekat** | Deteksi cabang terdekat berdasarkan GPS pengguna |

---

## 📐 Arsitektur

Proyek ini mengikuti pola **MVVM + Clean Architecture** dengan single-Activity berbasis **Jetpack Compose Navigation**.

```
┌─────────────────────────────────────────────────────┐
│                   PRESENTATION                       │
│  Screen (Composable) ◄──► ViewModel                 │
└───────────────┬─────────────────────────────────────┘
                │
┌───────────────▼─────────────────────────────────────┐
│                     DATA                             │
│  Repository ◄──► ApiServices (Retrofit)              │
│  Repository ◄──► Room DAO (Local DB)                 │
│  TokenManager ◄──► DataStore (Preferences)           │
└───────────────┬─────────────────────────────────────┘
                │
┌───────────────▼─────────────────────────────────────┐
│                  DEPENDENCY INJECTION                │
│               Hilt (AppModule)                       │
└─────────────────────────────────────────────────────┘
```

### Alur Data Login

```
AuthLoginScreen
    │── onLoginClick()
    │       └── NetworkUtils.checkNetwork()
    │               └── GoogleAuthUtils.signInWithGoogle()   ← Credential Manager
    │                       └── AuthRepository.loginGoogle() ← POST /auth/google
    │                               └── TokenManager.saveTokens() ← DataStore
    └── getCurrentRefreshToken() [Flow] ── navigate(Screen.Main)
```

### Alur Scan STNK → Ambil Antrian

```
HomeScreen ──► TambahDataScreen ──► ScanStnkScreen (Kamera)
                    │                       │
                    │               ViewModelOCR.processOCR()
                    │                       │
                    │               BitmapCropUtils.cropBitmap()
                    │                       │
                    │               ML Kit TextRecognition
                    │                       │
                    │◄──── stnkResult (savedStateHandle) ◄───┘
                    │
                    ▼
              PilihCabang ──► KonfirmasiAntrean ──► POST /antrian ──► BerhasilAmbil
```

---

## 🛠 Tech Stack

### Core
| Library | Versi | Kegunaan |
|---------|-------|----------|
| **Jetpack Compose** | BOM 2025.01 | UI Framework |
| **Kotlin** | 1.9+ | Bahasa utama |
| **Hilt** | 2.57.1 | Dependency Injection |
| **Navigation Compose** | 2.9.7 | Single-Activity navigation |
| **Coroutines + Flow** | Jetpack | Async & reaktif state |

### Networking
| Library | Versi | Kegunaan |
|---------|-------|----------|
| **Retrofit 2** | 2.9.0 | HTTP client REST API |
| **Gson Converter** | 2.9.0 | JSON serialization |
| **OkHttp** | 4.12.0 | HTTP engine + interceptor |

### Data Persistence
| Library | Versi | Kegunaan |
|---------|-------|----------|
| **Room** | 2.8.4 | Local database (antrian tersimpan) |
| **DataStore Preferences** | 1.2.1 | Penyimpanan JWT token |

### Auth & Services
| Library | Versi | Kegunaan |
|---------|-------|----------|
| **Credential Manager** | 1.6.0 | Google Sign-In modern |
| **Google Identity** | 1.1.1 | ID Token extraction |
| **Play Services Auth** | 21.5.1 | Google OAuth support |
| **Play Services Location** | 21.3.0 | GPS lokasi pengguna |

### AI / OCR
| Library | Versi | Kegunaan |
|---------|-------|----------|
| **ML Kit Text Recognition** | 16.0.1 | OCR membaca teks STNK |
| **CameraX** | 1.3.0 | Kamera live preview + capture |

### UI
| Library | Versi | Kegunaan |
|---------|-------|----------|
| **Material 3** | 1.2.0 | Design system |
| **Coil Compose** | 2.5.0 | Async image loading |
| **Lottie Compose** | 6.4.0 | Animasi Lottie |
| **Accompanist Permissions** | 0.34.0 | Permission handling |

### Testing
| Library | Versi | Kegunaan |
|---------|-------|----------|
| **MockK** | 1.13.10 | Mocking framework Kotlin |
| **Coroutines Test** | 1.8.1 | Unit test suspend function |
| **Retrofit Mock** | 2.9.0 | Mock API response |

---

## 📁 Struktur Proyek

```
app/src/main/java/dev/inteiintel/teduhserviceapp/
│
├── MyApp.kt                          # @HiltAndroidApp — entry point
├── MainActivity.kt                   # Single Activity — host Compose
│
├── di/
│   └── AppModule.kt                  # Hilt module — semua @Provides @Singleton
│
├── data/
│   ├── local/
│   │   ├── TokenManager.kt           # DataStore — simpan/baca JWT
│   │   └── room/
│   │       ├── AppDatabase.kt        # Room database
│   │       ├── AntrianDao.kt         # DAO CRUD antrian lokal
│   │       └── SavedAntrianEntity.kt # Entity tabel saved_antrian
│   ├── mapper/
│   │   └── DataMapperSaved.kt        # SavedAntrianEntity → CreateAntrianRequest
│   ├── model/                        # Request/Response data class
│   │   └── ui/                       # UI state models (StnkResult, UiState, dll.)
│   ├── remote/
│   │   ├── ApiClient.kt              # Retrofit factory + OkHttp setup
│   │   ├── ApiServices.kt            # Interface semua endpoint REST
│   │   └── AuthInterceptor.kt        # OkHttp interceptor JWT injection
│   └── repository/                   # Repository pattern per domain
│
├── presentation/
│   ├── auth/
│   │   ├── AuthLoginScreen.kt        # Onboarding + Google Sign-In UI
│   │   └── AuthViewModel.kt          # Logika auth, token, onboarding data
│   ├── components/
│   │   ├── HorizontalPager.kt        # Komponen pager (dev/percobaan)
│   │   └── HorizontalPagerViewModel.kt
│   ├── main/
│   │   ├── MainScreen.kt             # Shell utama + BottomBar
│   │   ├── components/
│   │   │   ├── ScanStnkScreen.kt     # Kamera OCR STNK
│   │   │   ├── TopAppBar.kt          # AppBar kustom reusable
│   │   │   └── ViewModelOCR.kt       # ML Kit OCR + STNK parser
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   ├── HomeViewModel.kt
│   │   │   ├── ambil_antrean/        # Alur ambil antrian (TambahData, dll.)
│   │   │   ├── detail_cabang/        # Detail & daftar cabang
│   │   │   ├── history/              # Riwayat antrian
│   │   │   └── reminder/             # Pengingat servis
│   │   ├── queues/                   # Antrian aktif + detail
│   │   ├── notifications/            # Broadcast + detail notifikasi
│   │   └── profile/                  # Profil + edit profil
│   └── splash/
│       └── SplashScreenViewModel.kt  # Cek login → hide splash
│
└── utils/
    ├── BitmapCropUtils.kt            # CropArea + cropBitmap()
    ├── CredentialsManager.kt         # GoogleAuthUtils.signInWithGoogle()
    ├── FindNearestCabangUtils.kt     # FindUtils.calculateDistanceKm()
    ├── FormatDateUtils.kt            # ISO 8601 → "dd MMM yyyy HH:mm"
    ├── LocationUtils.kt              # GPS user — strategi 2 tahap
    ├── NetworkStatus.kt              # Sealed class: Available|NoConnection|NoInternet
    ├── NetworkUtils.kt               # isNetworkAvailable + hasInternetCOnnection
    └── navigation/
        ├── Screen.kt                 # Sealed class semua route navigasi
        └── NavGraph.kt               # AppNavGraph — root NavHost
```

---

## 🧭 Alur Navigasi

```
                    ┌──────────┐
               ┌───► AuthScreen │ (Login Google + Onboarding)
               │    └────┬─────┘
               │         │ login sukses
               │    ┌────▼──────────────────────────────────┐
               │    │          MainScreen (Bottom Nav)        │
               │    │  Home │ Queues │ Notifications │ Profile │
               │    └───┬───────────────────────────────────-┘
               │        │
               │   ┌────▼──────────────────────────────────────────────────┐
               │   │                    Home Flow                           │
               │   │  DaftarCabang → DetailCabang                           │
               │   │  Riwayat                                               │
               │   │  Pengingat                                             │
               │   │                                                        │
               │   │  AmbilAntrean                                         │
               │   │    ├── TambahDataSTNK ←──────── ScanStnk (OCR)        │
               │   │    │       ↓                                           │
               │   │    ├── DataTersimpan → PilihEstimasi ─┐               │
               │   │    │                                   ↓               │
               │   │    └──────────────────────── PilihCabang               │
               │   │                                        ↓               │
               │   │                              KonfirmasiAntrean         │
               │   │                                        ↓               │
               │   │                              BerhasilAmbilAntrean      │
               │   └───────────────────────────────────────────────────────┘
               │
               │   ┌───────────────┐   ┌──────────────────┐
               │   │ DetailAntrean │   │ DetailNotifikasi  │
               │   └───────────────┘   └──────────────────┘
               │
               └───── logout (hapus token) ◄── MainScreen auto-redirect
```

---

## ⚙️ Setup & Instalasi

### Prasyarat

- **Android Studio** Hedgehog atau lebih baru
- **JDK 11**
- **Android SDK** — minimum API 24, target API 35

### Langkah Instalasi

```bash
# 1. Clone repositori
git clone https://github.com/miawwmiaww/AntrianBengkelShowcase.git
cd AntrianBengkelShowcase

# 2. Buka di Android Studio
# File → Open → pilih folder proyek

# 3. Sync Gradle
# Android Studio akan otomatis menjalankan Gradle sync

# 4. Build & Run
# Tekan Shift+F10 atau klik tombol ▶ Run
```

---

## 🔧 Konfigurasi

### Google OAuth Client ID

File: `presentation/auth/AuthViewModel.kt`

```kotlin
// Ganti dengan Web Client ID dari Google Cloud Console Anda
private val OAUTH_CLIENT_ID =
    "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
```

> ⚠️ **Catatan Keamanan:** Untuk production, pindahkan nilai ini ke `local.properties` atau gunakan Secrets Manager. Jangan commit Client ID ke repositori publik.

### API Base URL

File: `data/remote/ApiClient.kt`

```kotlin
.baseUrl("https://rakaascode.site/api/")
```

---

## 🌐 API Reference

Base URL: `https://rakaascode.site/api/`

Semua request terautentikasi menggunakan header:
```
Authorization: Bearer <JWT_TOKEN>
```

| Method | Endpoint | Deskripsi |
|--------|----------|-----------|
| `POST` | `auth/google` | Login dengan Google ID Token |
| `GET` | `users/profile` | Profil pengguna |
| `POST` | `users/kontak` | Update nomor WhatsApp reminder |
| `GET` | `cabang` | Daftar semua cabang bengkel |
| `GET` | `cabang/{branchId}` | Detail satu cabang |
| `GET` | `cabang/antrian/ringkasan` | Ringkasan estimasi antrian per cabang |
| `POST` | `antrian` | Buat antrian baru |
| `GET` | `antrian/me` | Antrian aktif pengguna |
| `DELETE` | `antrian/{id}/batal` | Batalkan antrian |
| `GET` | `broadcast` | Semua notifikasi |
| `GET` | `broadcast/{id}` | Detail notifikasi |

---

## 🏗 Build Info

| Properti | Nilai |
|----------|-------|
| Application ID | `dev.inteiintel.teduhserviceapp` |
| Versi | `1.3` (versionCode: 4) |
| Min SDK | 24 (Android 7.0 Nougat) |
| Target SDK | 35 (Android 15) |
| Compile SDK | 36 |
| Kotlin JVM Target | 11 |

---

## 📝 Lisensi

```
© 2024–2026 IntelIntei Dev Team. All rights reserved.
```

Proyek ini dikembangkan sebagai showcase/portofolio.  
Lihat repositori di: [miawwmiaww/AntrianBengkelShowcase](https://github.com/miawwmiaww/AntrianBengkelShowcase)
