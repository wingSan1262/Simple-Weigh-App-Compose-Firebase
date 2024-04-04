package com.example.sawitprotest.feature.weighbridge.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sawitprotest.base.extensions.toFormattedDateString
import com.example.sawitprotest.base.ui.theme.primaryColor
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem


@Preview(showBackground = true)
@Composable
fun BridgeWeightEntryCard(
    item: BridgeWeightEntryItem =
        BridgeWeightEntryItem(
            id = "KDHWKMS2314",
            dateTime = 1234123,
            licenseNumber = "1234",
            driverName = "John Doe",
            inboundWeight = 100.0,
            outboundWeight = 90.0,
        ),
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .clickable { onEdit() }
        ,
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F7)),
        ) {
            Row(

                modifier = Modifier
                    .background(primaryColor)
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .width(220.dp),
                    color = Color.White,
                    text = item.dateTime.toFormattedDateString()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier.clickable {
                            onEdit()
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Filled.Delete, contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable {
                                onDelete()
                            }
                            .padding(end = 16.dp)
                    )
                }
            }

            EntryRow(label = "", value = item.id, isBold = true)
            Column{
                Row(
                    Modifier.fillMaxWidth(),
                ){
                    EntryRow(
                        modifier = Modifier.weight(1f),
                        label = "Driver Name", value = item.driverName, isBold = true)
                    EntryRow(
                        modifier = Modifier.weight(1f),
                        label = "Net Weight", value = "${item.netWeight} Kg", isBold = true)
                }

                Row(
                    Modifier.fillMaxWidth(),
                ){
                    EntryRow(
                        modifier = Modifier.weight(1f),
                        label = "License Number", value = item.licenseNumber, isBold = true)
                    WeightInfo(
                        modifier = Modifier.weight(1f),
                        inBound = item.inboundWeight,
                        outBound = item.outboundWeight
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun WeightInfo(
    modifier: Modifier, inBound: Double, outBound: Double) {
    Surface(
        modifier = modifier.padding(start = 8.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.width(64.dp),
                    text = "Outbound",
                    fontSize = 12.sp
                )
                Text(
                    text = "$outBound Kg",
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.height(2.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier.width(64.dp),
                    text = "Inbound",
                    fontSize = 12.sp
                )
                Text(
                    text = "$inBound Kg",
                    fontSize = 14.sp,
                )
            }
        }
    }
}

@Composable
fun EntryRow(
    label: String, value: String, isBold: Boolean = false,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.padding(start = 8.dp),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
        ) {
            if (label.isNotEmpty())
                Text(
                    text = label,
                    color = Color(0xff848484),
                    fontSize = 14.sp
                )
            Spacer(modifier = Modifier.height(4.dp))
            if (value.isNotEmpty())
                Text(
                    text = value,
                    fontSize = 18.sp,
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
                )
        }
    }
}