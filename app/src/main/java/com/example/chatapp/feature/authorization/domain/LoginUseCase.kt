package com.example.chatapp.feature.authorization.domain

import com.example.chatapp.feature.coinDetail.domain.CoinDetailEntity

interface LoginUseCase {

    suspend operator fun invoke(username: String, password: String): LoginEntity

}