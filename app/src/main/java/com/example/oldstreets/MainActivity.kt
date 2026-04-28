package com.example.oldstreets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.oldstreets.ui.screen.LocationScreen
import com.example.oldstreets.ui.theme.OldStreetsTheme
import com.example.oldstreets.ui.viewmodel.MainViewModel
import com.example.oldstreets.ui.viewmodel.MainViewmodelFactory
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewmodelFactory: MainViewmodelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as OldStreetApp).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewmodelFactory)[MainViewModel::class]

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            OldStreetsTheme {
                LocationScreen(viewModel)
            }
        }
    }
}

