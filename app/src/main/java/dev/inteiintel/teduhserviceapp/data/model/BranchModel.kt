package dev.inteiintel.teduhserviceapp.data.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class BranchResponse(
    val success: Boolean,
    val message: String,
    val data: List<Branch>
)

data class DetailBranchResponse(
    val success: Boolean,
    val message: String,
    val data: Branch
)

@Parcelize
data class Branch(

    val id: Int,
    val nama: String,
    val alamat: String,
    val kota: String,
    val no_telp: String,

    val latitude: Double?,
    val longitude: Double?,

    @SerializedName("created_at")
    val createdAt: String?,

    @SerializedName("updated_at")
    val updatedAt: String?
): Parcelable

data class BranchWithDistance(
    val branch: Branch,
    val distanceKm: Double
)