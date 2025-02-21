package com.example.chatapp.di.model

import java.io.IOException

class NetworkException(cause: Throwable? = null) : IOException("Network not stable", cause)
