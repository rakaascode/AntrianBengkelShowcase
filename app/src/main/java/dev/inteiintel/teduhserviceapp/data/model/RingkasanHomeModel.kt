package dev.inteiintel.teduhserviceapp.data.model

import com.google.gson.annotations.SerializedName

data class RingkasanHomeResponse(
    @SerializedName("success")
    val success: Boolean,

    @SerializedName("total_cabang")
    val totalCabang: Int,

    @SerializedName("data")
    val data: List<RingkasanCabangItem>
)

data class RingkasanCabangItem(
    @SerializedName("cabang_id")
    val cabangId: Int,

    @SerializedName("nama_cabang")
    val namaCabang: String,

    @SerializedName("latitude")
    val latitude: Double,

    @SerializedName("longitude")
    val longitude: Double,

    @SerializedName("nomor_dipanggil")
    val nomorDipanggil: Int?,

    @SerializedName("estimasi_jam")
    val estimasiJam: String,

    @SerializedName("sisa_antrian")
    val sisaAntrian: Int
)