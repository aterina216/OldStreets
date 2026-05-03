package com.example.oldstreets.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.oldstreets.domain.model.City
import com.example.oldstreets.domain.model.HistoricalPhoto
import com.example.oldstreets.domain.model.Street
import com.example.oldstreets.domain.repository.AddressRepository
import com.example.oldstreets.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val adressRepository: AddressRepository,
    private val photoRepository: PhotoRepository) : ViewModel() {

    private var _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities

    private var _streets = MutableStateFlow<List<Street>>(emptyList())
    val streets: StateFlow<List<Street>> = _streets

    private var _coordinates = MutableStateFlow<Pair<Double, Double>?>(null)
    val coordinates: StateFlow<Pair<Double, Double>?> = _coordinates

    private var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private var _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private var _photos = MutableStateFlow<List<HistoricalPhoto>>(emptyList())
    val photos: StateFlow<List<HistoricalPhoto>> = _photos.asStateFlow()

    private var _isLoadingPhotos = MutableStateFlow(false)
    val isLoadingPhotos: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun searchCities(query: String) {
        Log.d("ViewModel", "searchCities called with query: $query")
        if (query.length < 2) {
            _cities.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            Log.d("ViewModel", "searchCities: $query")
            try {
                val result = adressRepository.suggestSities(query)
                Log.d("ViewModel", "Result size: ${result.size}")
                _cities.value = result
            } catch (e: Exception) {
                Log.e("ViewModel", "Error in searchCities", e)
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStreets(cityFiasId: String, query: String) {
        if (query.length < 2) {
            _streets.value = emptyList()
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = adressRepository.suggestStreets(cityFiasId, query)
                _streets.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCoordinates(streerFiasId: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val coords = adressRepository.getCoordinates(streerFiasId)
                _coordinates.value = coords
                coords?.let { loadHistoricalPhotos(it.first, it.second) }
            }
            catch (e: Exception) {
                _error.value = e.message
            }
            finally {
                _isLoading.value = false
            }
        }
    }
    fun clearError() {
        _error.value = null
    }

    fun loadHistoricalPhotos(lat: Double, lon: Double) {
        viewModelScope.launch {
            _isLoadingPhotos.value = true

            try {
                val result = photoRepository.getHistoricalPhotos(lat, lon)
                _photos.value = result
            }
            catch (e: Exception) {
                Log.e("MainViewModel", "Не удалось загрузить фото", e)
                _photos.value = emptyList()
            }
            finally {
                _isLoadingPhotos.value = false
            }
        }
    }
}