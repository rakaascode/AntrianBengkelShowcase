# Project specific ProGuard / R8 rules

# Keep all data models for Gson serialization/deserialization
-keep class dev.inteiintel.teduhserviceapp.data.model.** { *; }
-keepclassmembers class dev.inteiintel.teduhserviceapp.data.model.** { <fields>; }

# Keep Room entities and DAOs
-keep class dev.inteiintel.teduhserviceapp.data.local.room.** { *; }
-keepclassmembers class dev.inteiintel.teduhserviceapp.data.local.room.** { *; }

# Retrofit & Gson rules
-keepattributes Signature, *Annotation*, InnerClasses, EnclosingMethod
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}
-keep,allowobfuscation,allowshrinking class com.google.gson.** { *; }

# OkHttp & Retrofit interfaces
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Lottie Animation
-keep class com.airbnb.lottie.** { *; }

# Coil
-dontwarn coil.**

# Preserve line numbers for stack traces
-keepattributes SourceFile,LineNumberTable