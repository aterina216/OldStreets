package com.example.oldstreets.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Query
import com.example.oldstreets.domain.model.City
import com.example.oldstreets.domain.model.Street
import com.example.oldstreets.domain.repository.AddressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val adressRepository: AddressRepository) : ViewModel() {

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
}