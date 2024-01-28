package dev.xinto.cashier.common.domain.model

sealed interface Result<out T> {
    @JvmInline
    value class Success<out T>(val data: T) : Result<T>

    data object Error : Result<Nothing>
}