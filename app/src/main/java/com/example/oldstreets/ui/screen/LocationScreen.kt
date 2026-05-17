package com.example.oldstreets.ui.screen

import android.R
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.oldstreets.ui.components.HistoricalPhotoCard
import com.example.oldstreets.ui.components.RetroInputField
import com.example.oldstreets.ui.viewmodel.MainViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(viewModel: MainViewModel, navController: NavController) {

    val cities by viewModel.cities.collectAsStateWithLifecycle()
    val streets by viewModel.streets.collectAsStateWithLifecycle()
    val coordinates by viewModel.coordinates.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    var cityInput by rememberSaveable { mutableStateOf("") }
    var selectedCityFiasId by rememberSaveable { mutableStateOf<String?>(null) }
    var streetInput by rememberSaveable { mutableStateOf("") }
    var selectedStreetFiasId by rememberSaveable { mutableStateOf<String?>(null) }

    var expandedCities by remember { mutableStateOf(false) }
    var streetMenuExpanded by remember { mutableStateOf(false) }

    val photos = viewModel.photos.collectAsLazyPagingItems()

    LaunchedEffect(photos.itemCount) {
        Log.d("UI_DEBUG", "photos.itemCount = ${photos.itemCount}")
        Log.d("UI_DEBUG", "photos.loadState.refresh = ${photos.loadState.refresh}")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Исторические улицы",
            fontSize = 28.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 32.dp, top = 16.dp),
            textAlign = TextAlign.Center
        )


        ExposedDropdownMenuBox(
            expanded = expandedCities && cities.isNotEmpty(),
            onExpandedChange = { expandedCities = it }
        ) {
            OutlinedTextField(
                value = cityInput,
                onValueChange = { input ->
                    Log.d("UI", "cityInput changed to: $input")
                    cityInput = input
                    selectedCityFiasId = null
                    streetInput = ""
                    viewModel.searchCities(input)
                    expandedCities = input.isNotBlank()
                },
                placeholder = {
                    Text(
                        "Начните вводить город", color =
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                },
                label = { Text("Город") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface
                        .copy(alpha = 0.3f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = false) }
            )
            ExposedDropdownMenu(
                expanded = cities.isNotEmpty() && expandedCities,
                onDismissRequest = { expandedCities = false },
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.medium
                )
            ) {
                cities.forEach { city ->
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

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = streets.isNotEmpty() && selectedCityFiasId != null && streetMenuExpanded,
            onExpandedChange = { streetMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = streetInput,
                onValueChange = { input ->
                    streetInput = input
                    selectedStreetFiasId = null
                    selectedCityFiasId?.let { cityId ->
                        viewModel.loadStreets(cityId, input)
                        streetMenuExpanded = input.isNotBlank()
                    }
                },
                label = { Text("Улица") },
                enabled = selectedCityFiasId != null,
                placeholder = {
                    Text(
                        "Выберите улицу", color = MaterialTheme.colorScheme.onSurface
                            .copy(alpha = 0.5f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(false) }
            )
            ExposedDropdownMenu(
                expanded = streets.isNotEmpty() &&
                        selectedCityFiasId != null && streetMenuExpanded,
                onDismissRequest = { streetMenuExpanded = false },
                modifier = Modifier.background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.medium
                )
            ) {
                streets.forEach { street ->
                    DropdownMenuItem(
                        text = { Text(street.name) },
                        onClick = {
                            streetInput = street.name
                            selectedStreetFiasId = street.fiasId
                            streetMenuExpanded = false
                            viewModel.getCoordinates(street.fiasId)
                        }
                    )
                }
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
        }

        error?.let {
            AssistChip(
                onClick = { viewModel.clearError() },
                label = { Text(it) },
                modifier = Modifier.fillMaxWidth(),
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                    labelColor = MaterialTheme.colorScheme.error
                ),
                shape = MaterialTheme.shapes.small
            )
        }
        if (coordinates != null && selectedStreetFiasId != null) {
            Text(
                text = "Фотографии прошлого",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp)
            )

            when (val refreshState = photos.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }

                is LoadState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error.copy(
                                alpha = 0.1f
                            )
                        ),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Ошибка загрузки фото", color = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { photos.retry() }) {
                            }
                            Text("Повторить")
                        }
                    }
                }

                else -> {
                    Log.d("UI_DEBUG", "Вошли в else, photos.itemCount = ${photos.itemCount}")
                    if (photos.itemCount == 0) {
                        Log.d("UI_DEBUG", "Фото не найдены, показываем карточку")
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Text(
                                "Фотографии не найдены\nПопробуйте другой адрес",
                                modifier = Modifier.padding(24.dp),
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                    } else {
                        Log.d("UI_DEBUG", "Начинаем отрисовку сетки, фото: ${photos.itemCount}")
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            items(photos.itemCount) { index ->
                                photos[index]?.let { photo ->
                                    HistoricalPhotoCard(
                                        photo,
                                        onClick = {
                                            viewModel.selectPhoto(photo)
                                            navController.navigate("photo")
                                        })
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
