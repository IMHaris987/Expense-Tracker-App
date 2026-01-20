package com.haris.expensetracker.network

import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    @GET("v6/0065a0ded8fcee065d7c470e/latest/{base}")
    suspend fun getExchangeRates(
        @Path("base") baseCurrency: String
    ): ExchangeRateResponse
}