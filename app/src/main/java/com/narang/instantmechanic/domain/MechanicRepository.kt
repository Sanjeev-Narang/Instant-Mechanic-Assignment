package com.narang.instantmechanic.domain

import kotlinx.coroutines.flow.Flow

interface MechanicRepository {
    suspend fun getMechanics(): Result<List<Mechanic>>
    suspend fun getMechanicById(id: Int): Result<Mechanic>
    fun getMechanicsFlow(): Flow<Result<List<Mechanic>>>
}
