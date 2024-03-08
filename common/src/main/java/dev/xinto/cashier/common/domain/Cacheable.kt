package dev.xinto.cashier.common.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

sealed interface DomainCacheableState<out T> {
    data object Loading : DomainCacheableState<Nothing>

    @JvmInline
    value class Loaded<out T>(val value: T) : DomainCacheableState<T>
}

class Cacheable<T>(
    private inline val compute: suspend () -> T,
) {

    private val state = MutableStateFlow<DomainCacheableState<T>>(DomainCacheableState.Loading)

    val flow = state.map {
        if (it is DomainCacheableState.Loading) {
            DomainCacheableState.Loaded(compute()).also { computed ->
                state.value = computed
            }
        } else {
            it
        }
    }.flowOn(Dispatchers.IO)

    fun refresh() {
        state.value = DomainCacheableState.Loading
    }

    suspend operator fun invoke() = flow.first()
}