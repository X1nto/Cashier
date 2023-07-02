package com.xinto.cashier.domain.model

sealed interface Result<out T> {
    @JvmInline
    value class Success<out T>(val data: T) : Result<T>

    object Error : Result<Nothing>
}