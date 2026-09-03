package com.narang.instantmechanic.data

import com.narang.instantmechanic.domain.Mechanic
import com.narang.instantmechanic.domain.MechanicRepository
import com.narang.instantmechanic.domain.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class MechanicRepositoryImpl @Inject constructor(
    private val api: MechanicApi
) : MechanicRepository {

    override suspend fun getMechanics(): Result<List<Mechanic>> {
        return try {
            Timber.d("Fetching mechanics from REST api")
            val dtos = api.getMechanics()
            val domain = dtos.map { it.toDomain() }
            Timber.d("Fetched %d mechanics", domain.size)
            Result.Success(domain)
        } catch (e: IOException) {
            Timber.e(e, "Network error fetching mechanics")
            Result.Error("No internet. Check your connection.", e)
        } catch (e: HttpException) {
            Timber.e(e, "Server error %d", e.code())
            val msg = when (e.code()) {
                401 -> "Unauthorized. Please login again."
                404 -> "Mechanics not found."
                in 500..599 -> "Server error. Try again later."
                else -> "Something went wrong: ${e.message()}"
            }
            Result.Error(msg, e)
        } catch (e: Exception) {
            Timber.e(e, "Unknown error")
            Result.Error("Unexpected error: ${e.message}", e)
        }
    }

    override suspend fun getMechanicById(id: Int): Result<Mechanic> {
        return try {
            val dto = api.getMechanicById(id)
            Result.Success(dto.toDomain())
        } catch (e: IOException) {
            Result.Error("No internet.", e)
        } catch (e: HttpException) {
            Result.Error("Server error: ${e.code()}", e)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error", e)
        }
    }

    override fun getMechanicsFlow(): Flow<Result<List<Mechanic>>> = flow {
        emit(Result.Loading)
        emit(getMechanics())
    }
}

/**
 * Fake impl for demo / previews / when Firebase Function not deployed yet.
 * Shows you how modularity helps - swap this in instantly.
 */
class FakeMechanicRepository @Inject constructor() : MechanicRepository {
    private val demo = listOf(
        Mechanic(1, "Swift Fix Auto", "4.9", "Brooklyn, NY • 1.2 km", "Open"),
        Mechanic(2, "Metro Motor Works", "4.8", "Queens, NY • 2.7 km", "Open"),
        Mechanic(3, "Reliable Auto Care", "4.7", "Manhattan, NY • 3.5 km", "Closes 6 PM"),
    )

    override suspend fun getMechanics(): Result<List<Mechanic>> {
        kotlinx.coroutines.delay(800) // simulate network
        return Result.Success(demo)
    }

    override suspend fun getMechanicById(id: Int): Result<Mechanic> {
        val m = demo.find { it.id == id } ?: return Result.Error("Not found")
        return Result.Success(m)
    }

    override fun getMechanicsFlow(): Flow<Result<List<Mechanic>>> = flow {
        emit(Result.Loading)
        kotlinx.coroutines.delay(800)
        emit(Result.Success(demo))
    }
}
