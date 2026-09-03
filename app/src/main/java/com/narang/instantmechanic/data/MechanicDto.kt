package com.narang.instantmechanic.data

import com.narang.instantmechanic.domain.Mechanic
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MechanicDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String?,
    @Json(name = "rating") val rating: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "status") val status: String?,
    // extra fields your Firebase Function might return - keep nullable for safety
    @Json(name = "phone") val phone: String? = null,
    @Json(name = "imageUrl") val imageUrl: String? = null,
)

fun MechanicDto.toDomain(): Mechanic = Mechanic(
    id = id,
    name = name ?: "Unknown",
    rating = rating ?: "—",
    location = location ?: "",
    status = status ?: "Unknown"
)

// For wrapping list responses - some backends return { data: [...] } or plain [...]
@JsonClass(generateAdapter = true)
data class MechanicsResponse(
    @Json(name = "mechanics") val mechanics: List<MechanicDto>? = null,
    @Json(name = "data") val data: List<MechanicDto>? = null
)
