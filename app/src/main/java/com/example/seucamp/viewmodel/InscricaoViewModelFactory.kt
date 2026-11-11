package com.example.seucamp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.seucamp.data.repository.InscricaoRepository

class InscricaoViewModelFactory(
    private val repository: InscricaoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InscricaoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InscricaoViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconhecida")
    }
}
