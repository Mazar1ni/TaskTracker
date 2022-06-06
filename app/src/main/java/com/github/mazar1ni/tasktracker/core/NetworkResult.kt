package com.github.mazar1ni.tasktracker.core

import com.github.mazar1ni.tasktracker.core.util.NetworkResultType

data class NetworkResult<T>(val networkResultType: NetworkResultType, val data: T? = null)