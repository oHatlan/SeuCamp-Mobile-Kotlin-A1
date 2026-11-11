package com.example.seucamp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seucamp.data.repository.TorneioRepository

class TorneioViewModelFactory(
    private val repository: TorneioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TorneioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TorneioViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecida")
    }
}
