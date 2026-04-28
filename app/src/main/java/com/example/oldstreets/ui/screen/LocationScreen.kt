package com.example.oldstreets.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.oldstreets.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(viewModel: MainViewModel) {

    val cities by viewModel.cities.collectAsStateWithLifecycle()
    val streets by viewModel.streets.collectAsStateWithLifecycle()
    val coordinates by viewModel.coordinates.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var cityInput by remember { mutableStateOf("") }
    var selectedCityFiasId by remember { mutableStateOf<String?>(null) }
    var streetInput by remember { mutableStateOf("") }
    var selectedStreetFiasId by remember { mutableStateOf<String?>(null) }

    var expandedCities by remember { mutableStateOf(false) }
    var streetMenuExpanded by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp, top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ExposedDropdownMenuBox(
            expanded = cities.isNotEmpty() && cityInput.isNotBlank(),
            onExpandedChange = { expandedCities = !expandedCities}
        ) {
            OutlinedTextField(
                value = cityInput,
                onValueChange = {
                    input ->
                    Log.d("UI", "cityInput changed to: $input")
                    cityInput = input
                    selectedCityFiasId = null
                    streetInput = ""
                    viewModel.searchCities(input)
                    expandedCities = input.isNotBlank()
                },
                label = { Text("Город") },
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false)}
            )
            ExposedDropdownMenu(
                expanded = cities.isNotEmpty() && expandedCities,
                onDismissRequest = {}
            ) {
                cities.forEach {
                    city ->
                    DropdownMenuItem(
                        text = { Text(city.name) },
                        onClick = {
                            cityInput = city.name
                            selectedCityFiasId = city.fasId
                            expandedCities = false
                            streetInput = ""
                            selectedStreetFiasId = null
                            viewModel.loadStreets(city.fasId, "")
                        }
                    )
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = streets.isNotEmpty() && streetInput.isNotBlank() &&
            selectedCityFiasId != null && streetMenuExpanded,
            onExpandedChange = {streetMenuExpanded = it}
        ) {
            OutlinedTextField(
                value = streetInput,
                onValueChange = {
                    input ->
                    streetInput = input
                    selectedStreetFiasId = null
                    selectedCityFiasId?.let {
                        cityId ->
                        viewModel.loadStreets(cityId, input)
                        streetMenuExpanded = input.isNotBlank()
                    }
                },
                label = { Text("Улица")},
                enabled = selectedCityFiasId != null,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(false) }
            )
            ExposedDropdownMenu(
                expanded = streets.isNotEmpty() && streetInput.isNotBlank() &&
                selectedCityFiasId != null && streetMenuExpanded,
                onDismissRequest = {}
            ) {
                streets.forEach {
                    street ->
                    DropdownMenuItem(
                        text = { Text(street.name)},
                        onClick = {
                            streetInput = street.name
                            selectedStreetFiasId = street.fiasId
                            streetMenuExpanded =false
                            viewModel.getCoordinates(street.fiasId)
                        }
                    )
                }
            }
        }
        if(isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        error?.let {
            AssistChip(
                onClick = {viewModel.clearError()},
                label = {Text(it)},
                modifier = Modifier.fillMaxWidth()
            )
        }
        if(coordinates != null && selectedCityFiasId != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Выбрано: $cityInput, $streetInput")
                    Text("Координаты: ${coordinates!!.first}, ${coordinates!!.second}")
                }
            }
        }
    }
}