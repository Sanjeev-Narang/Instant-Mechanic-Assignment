package com.narang.instantmechanic.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * REST contract
 * Behind the scenes this can point to:
 * 1) Firebase Cloud Function: https://us-central1-<project>.cloudfunctions.net/api/
 * 2) Direct Firestore REST: https://firestore.googleapis.com/v1/...
 * 3) Any mock server for local testing
 *
 * Keep it clean - no Firebase leakage here.
 */
interface MechanicApi {

    @GET("mechanics")
    suspend fun getMechanics(
        @Query("lat") lat: Double? = null,
        @Query("lng") lng: Double? = null,
        @Query("service") service: String? = null
    ): List<MechanicDto>

    @GET("mechanics/{id}")
    suspend fun getMechanicById(
        @Path("id") id: Int
    ): MechanicDto

    // Example for future: create service request
    // @POST("requests")
    // suspend fun createRequest(@Body request: CreateRequestDto): RequestDto
}
