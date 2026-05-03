package com.example.oldstreets.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.oldstreets.domain.repository.AddressRepository
import com.example.oldstreets.domain.repository.PhotoRepository

class MainViewmodelFactory(private val repo: AddressRepository,
    private val photoRepository: PhotoRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo, photoRepository) as T
    }
}