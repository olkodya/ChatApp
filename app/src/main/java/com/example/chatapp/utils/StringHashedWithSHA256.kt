package com.example.chatapp.utils

import java.security.MessageDigest

@OptIn(ExperimentalStdlibApi::class)
fun String.hashedWithSha256() =
    MessageDigest.getInstance("SHA-256")
        .digest(toByteArray())
        .toHexString()