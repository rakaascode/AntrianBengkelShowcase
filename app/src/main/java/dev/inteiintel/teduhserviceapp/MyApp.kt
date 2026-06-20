package dev.inteiintel.teduhserviceapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Entry point aplikasi Teduh Service yang menginisialisasi Hilt Dependency Injection.
 *
 * Dideklarasikan di `AndroidManifest.xml` sebagai `android:name=".MyApp"`.
 * Seluruh dependency graph Hilt berpusat di sini.
 *
 * @see dev.inteiintel.teduhserviceapp.di.AppModule
 */
@HiltAndroidApp
class MyApp : Application()