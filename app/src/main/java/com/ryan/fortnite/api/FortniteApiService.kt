package com.ryan.fortnite.api

import com.ryan.fortnite.data.StatsData
import com.ryan.fortnite.data.StatsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.awaitResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FortniteApiService {
    @GET("/v2/stats/br/v2")
    fun getStats(@Query("name") gamerTag: String): Call<StatsResponse>
}

fun fetchStats(gamerTag: String, onSuccess: (StatsData) -> Unit, onError: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.getStats(gamerTag).awaitResponse()
            if (response.isSuccessful && response.body() != null) {
                response.body()?.data?.let {
                    withContext(Dispatchers.Main) {
                        onSuccess(it)
                    }
                } ?: onError("No data available")
            } else {
                onError("API error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onError(e.message ?: "An error occurred")
            }
        }
    }
}