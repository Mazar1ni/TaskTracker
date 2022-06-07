package com.github.mazar1ni.tasktracker.core.util

import java.math.BigInteger
import java.security.MessageDigest

object Utils {

    fun String.generateMD5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

}