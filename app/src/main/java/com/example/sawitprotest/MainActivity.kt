package com.example.sawitprotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sawitprotest.base.ui.theme.SawitProTestTheme
import com.example.sawitprotest.feature.weighbridge.navigator.WeightBridgeNavigationHost
import com.example.sawitprotest.feature.weighbridge.viewmodel.ViewModelFactory
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel

class MainActivity : ComponentActivity() {

    lateinit var vm : WeighBridgeViewModel
    private lateinit var navController : NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        vm = ViewModelProvider(this, ViewModelFactory())[WeighBridgeViewModel::class.java]
        super.onCreate(savedInstanceState)
        setContent {
            SawitProTestTheme {
                navController = rememberNavController()
                WeightBridgeNavigationHost(vm, navController)
            }
        }
    }
}
