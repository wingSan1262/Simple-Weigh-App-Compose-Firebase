@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.sawitprotest.feature.weighbridge.screens

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.sawitprotest.base.extensions.asState
import com.example.sawitprotest.base.extensions.collectEventState
import com.example.sawitprotest.base.extensions.pickDateTime
import com.example.sawitprotest.base.extensions.showToast
import com.example.sawitprotest.base.extensions.toFormattedDateString
import com.example.sawitprotest.base.ui.compose.lazyColumnControl
import com.example.sawitprotest.base.ui.theme.primaryColor
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.feature.weighbridge.component.BridgeWeightEntryCard
import com.example.sawitprotest.feature.weighbridge.component.EmptyIllustration
import com.example.sawitprotest.feature.weighbridge.navigator.WEIGHT_BRIDGE_DETAIL_SCREEN
import com.example.sawitprotest.feature.weighbridge.navigator.WEIGHT_BRIDGE_LIST_SCREEN
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


fun NavGraphBuilder.BridfeWeightItemScreen(
    viewModel: WeighBridgeViewModel,
    onFinish: () -> Unit,
) {

    composable(route = WEIGHT_BRIDGE_DETAIL_SCREEN) {
        val context = LocalContext.current
        val (isSuccessCreate, isErrorCreate, isLoadingCreate, _, _, errorCreate) =
            viewModel.createWeightBridgeData.collectEventState().asState()

        val (isSuccessUpdate, isErrorUpdate, isLoadingUpdate, _, _, errorUpdate) =
            viewModel.updateWeightBridgeData.collectEventState().asState()

        LaunchedEffect(key1 = isSuccessCreate, key2 = isSuccessUpdate, block = {
            if (isSuccessCreate) {
                context.showToast("Create Success")
                onFinish()
            }
            if (isSuccessUpdate) {
                context.showToast("Update Success")
                onFinish()
            }
        })

        LaunchedEffect(key1 = isErrorCreate, key2 = isErrorUpdate, block = {
            if (isErrorCreate) context.showToast(errorCreate?.message.toString())
            if (isErrorUpdate) context.showToast(errorUpdate?.message.toString())
        })


        BridgeWeightEntryEditComponent(
            viewModel.selectedItem,
            {
                if (viewModel.isEdit) {
                    viewModel.updateWeightBridge(it)
                } else {
                    viewModel.createWeightBridge(it)
                }
            },
            {
                onFinish()
            },
            isEdit = viewModel.isEdit,
            isLoading = isLoadingCreate || isLoadingUpdate,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun BridgeWeightEntryEditComponent(
    chosenItem: BridgeWeightEntryItem = BridgeWeightEntryItem(),
    onDone: (BridgeWeightEntryItem) -> Unit = {},
    onCancel: () -> Unit = {},
    isEdit: Boolean = false,
    isLoading: Boolean = false
) {
    var id by remember { mutableStateOf(chosenItem.id) }
    var dateTime by remember { mutableStateOf(chosenItem.dateTime) }
    var licenseNumber by remember { mutableStateOf(chosenItem.licenseNumber) }
    var driverName by remember { mutableStateOf(chosenItem.driverName) }
    var inboundWeight by remember { mutableStateOf(chosenItem.inboundWeight.toString()) }
    var outboundWeight by remember { mutableStateOf(chosenItem.outboundWeight.toString()) }
    var netWeight by remember { mutableStateOf(chosenItem.netWeight) }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    fun onDone() {
        onDone(
            BridgeWeightEntryItem(
                id = id,
                dateTime = dateTime,
                licenseNumber = licenseNumber,
                driverName = driverName,
                inboundWeight = inboundWeight.toDouble(),
                outboundWeight = outboundWeight.toDouble(),
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .size(48.dp)
                    .clickable {
                        onCancel()
                    }
            )
            Text(
                text = (if (isEdit) "Edit" else "Create") + " Weigh Bridge Entry",
                fontSize = 22.sp,
                fontWeight = FontWeight.W500
            )
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = id,
            enabled = false,
            textStyle = TextStyle(color = Color(0xFF000000)),
            onValueChange = { newValue -> id = newValue },
            label = { Text("System Generated ID") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color(0xFFBBBCC5),
                disabledLabelColor = Color(0xFFBBBCC5),
                disabledTextColor = Color(0xFFBBBCC5),
                disabledBorderColor = Color(0xFFBBBCC5),
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable {
                    context.pickDateTime {
                        dateTime = it
                    }
                },
            enabled = false,
            textStyle = TextStyle(color = Color(0xFF000000)),
            value = if(dateTime == 0L || dateTime == Long.MAX_VALUE) "Not Applied" else
                dateTime.toFormattedDateString(),
            onValueChange = { },
            label = { Text("Date Time") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color(0xFFBBBCC5),
                disabledLabelColor = Color(0xFFBBBCC5),
                disabledTextColor = Color(0xFF000000),
                disabledBorderColor = Color(0xFF000000),
            ),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Pick Date",
                    modifier = Modifier.size(24.dp).clickable {
                        dateTime = 0L
                    }
                )
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = licenseNumber,
            textStyle = TextStyle(color = Color(0xFF000000)),
            onValueChange = { newValue -> licenseNumber = newValue },
            label = { Text("License Number") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = driverName,
            textStyle = TextStyle(color = Color(0xFF000000)),
            onValueChange = { newValue -> driverName = newValue },
            label = { Text("Driver Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = inboundWeight,
            textStyle = TextStyle(color = Color(0xFF000000)),
            onValueChange = { newValue ->
                inboundWeight = newValue.replace(Regex("[^0-9.]"), "")
                netWeight =
                    (outboundWeight.toDoubleOrNull() ?: 0.0) - (inboundWeight.toDoubleOrNull()
                        ?: 0.0)
            },
            label = { Text("Inbound Weight") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            value = outboundWeight,
            textStyle = TextStyle(color = Color(0xFF000000)),
            onValueChange = { newValue ->
                outboundWeight = newValue.replace(Regex("[^0-9.]"), "")
                netWeight =
                    (outboundWeight.toDoubleOrNull() ?: 0.0) - (inboundWeight.toDoubleOrNull()
                        ?: 0.0)
            },
            label = { Text("Outbound Weight") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Net Weight: ${netWeight} Kg"
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            onClick = {
                val error = validateBridgeWeightEntryItem(
                    dateTime = dateTime,
                    licenseNumber = licenseNumber,
                    driverName = driverName,
                    inboundWeight = inboundWeight,
                    outboundWeight = outboundWeight
                )
                if (error != null) {
                    context.showToast(error)
                    return@Button
                }
                onDone()
            },
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            enabled = !isLoading,
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(32.dp)
                        .padding(end = 8.dp)
                )
            }
            Text(if (isEdit) "Save" else "Create")
        }
    }
}

fun validateBridgeWeightEntryItem(
    dateTime: Long,
    licenseNumber: String,
    driverName: String,
    inboundWeight: String,
    outboundWeight: String
): String? {
    if (dateTime <= 0) {
        return "Date Time is not valid"
    }
    if (licenseNumber.isBlank()) {
        return "License Number cannot be empty"
    }
    if (driverName.isBlank()) {
        return "Driver Name cannot be empty"
    }
    if (inboundWeight.isBlank() || inboundWeight.toDoubleOrNull() == null) {
        return "Inbound Weight must be a valid number"
    }
    if (outboundWeight.isBlank() || outboundWeight.toDoubleOrNull() == null) {
        return "Outbound Weight must be a valid number"
    }
    return null
}