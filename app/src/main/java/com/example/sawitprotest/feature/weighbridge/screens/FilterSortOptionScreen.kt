@file:OptIn(ExperimentalLayoutApi::class, ExperimentalLayoutApi::class,
    ExperimentalMaterial3Api::class
)

package com.example.sawitprotest.feature.weighbridge.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.sawitprotest.base.ui.theme.primaryColor
import com.example.sawitprotest.data.remote.model.SearchModel
import com.example.sawitprotest.feature.weighbridge.model.SortType
import com.example.sawitprotest.feature.weighbridge.navigator.FILTER_SORT_SCREEN
import com.example.sawitprotest.feature.weighbridge.navigator.WEIGHT_BRIDGE_DETAIL_SCREEN
import com.example.sawitprotest.feature.weighbridge.viewmodel.WeighBridgeViewModel

fun NavGraphBuilder.FilterSortScreen(
    viewModel: WeighBridgeViewModel,
    onFinish: () -> Unit,
) {

    composable(route = FILTER_SORT_SCREEN) {
        val context = LocalContext.current
        FilterAndSort(
            viewModel.queryReqWeightBridge.filter.maxDate,
            viewModel.queryReqWeightBridge.filter.minDate,
            viewModel.queryReqWeightBridge.sort.sortType,
            viewModel.queryReqWeightBridge.keyworkd,
            {
                viewModel.queryReqWeightBridge.filter.maxDate = it
            },
            {
                viewModel.queryReqWeightBridge.filter.minDate = it
            },
            {
                viewModel.queryReqWeightBridge.sort.sortType = it
            },
            {viewModel.queryReqWeightBridge.keyworkd = it},
            {
                viewModel.queryReqWeightBridge.clearSearch()
                onFinish()
            },
            {onFinish()}
        )
    }
}

@Composable
@Preview(showBackground = true)
fun FilterAndSort(
    currentMaxDate : Long = Long.MAX_VALUE,
    currentMinDate : Long = 0,
    currentSort : SortType = SortType.NONE,
    currentquery : String = "",
    onMaxDate : (Long) -> Unit = {},
    onMinDate : (Long) -> Unit = {},
    onSort : (SortType) -> Unit = {},
    onNewKeyword : (String) -> Unit = {},
    onResetAll  : ()->Unit = {},
    onSearch: ()->Unit = {},
) {

    var query by remember { mutableStateOf(currentquery) }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                modifier = Modifier
                    .size(48.dp).padding(start = 0.dp)
                    .clickable {
                        onResetAll()
                    }
            )
            Text(
                text = " Search Weigh Bridge Entry",
                fontSize = 22.sp,
                fontWeight = FontWeight.W500
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            textStyle = TextStyle(color = Color(0xFF000000)),
            value = query,
            onValueChange = { query = it ; onNewKeyword(it)},
            label = { Text("Search") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color(0xFFBBBCC5),
                disabledLabelColor = Color(0xFFBBBCC5),
                disabledTextColor = Color(0xFF000000),
                disabledBorderColor = Color(0xFF000000),
            )
        )
        Text(text = "Filter", modifier = Modifier.padding(16.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {

            DatePicker(label = "Min Date", currentMinDate){
                onMinDate(it)
            }
            Spacer(modifier = Modifier.width(16.dp))
            DatePicker(label = "Max Date", currentMaxDate, Long.MAX_VALUE){
                onMaxDate(it)
            }
        }

        Text(text = "Sort", modifier = Modifier.padding(16.dp))
        SortOptions(currentSort){
            onSort(it)
        }

        Button(onClick = { onSearch()}, modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 32.dp, bottom = 16.dp)
            .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text(text = "SEARCH")
        }
        Button(
            onClick = { onResetAll()}, modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 16.dp,)
            .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text(text = "RESET ALL")
        }
    }
}

@Composable
fun DatePicker(
    label : String = "Date",
    current : Long = 0,
    bound : Long = 0,
    onPick : (Long) -> Unit,
) {
    var date by remember { mutableStateOf(current) }
    val context = LocalContext.current
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                context.pickDateTime {
                    onPick(it)
                    date = it
                }
            },
        enabled = false,
        textStyle = TextStyle(color = Color(0xFF000000)),
        value = if(date == 0L || date == Long.MAX_VALUE) "Not Applied" else
            date.toFormattedDateString(),
        onValueChange = { },
        label = { Text(label) },
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
                    date = bound
                }
            )
        }
    )
}

@Composable
fun SortOptions(
    currentSort: SortType,
    onSort: (SortType) -> Unit
) {
    val sortOptions = SortType.values()
    var selectedOption by remember { mutableStateOf(currentSort) }

    FlowRow(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        maxItemsInEachRow = 2,
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly
    ) {
        sortOptions.forEach { option ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { selectedOption = option; onSort(option) },
                label = {
                    Text(option.label)
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = primaryColor.copy(alpha = 0.2f),
                ),
                selected = selectedOption == option,
                trailingIcon = {
                    if (selectedOption == option) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    } else {
                        null
                    }
                }
            )
        }
    }

}