package com.example.sawitprotest.feature.weighbridge.model

enum class SortType(
    val value: String,
    val isAscending: Boolean = true,
    val label: String = ""
) {
    NONE("", label = "None"),
    OLDEST_DATE(value = "weighingDate", isAscending = true, label = "Oldest Date"),
    NEWEST_DATE(value = "weighingDate", isAscending = false, label = "Newest Date"),
    DRIVER_NAME_ASC(value = "driverName", label = "Driver A-z"),
    DRIVER_NAME_DESC(value = "driverName", isAscending = false, label = "Driver Z-a"),
    LICENSE_NUMBER_ASC(value = "licenseNumber", label = "License Number A-z"),
    LICENSE_NUMBER_DESC(value = "licenseNumber", isAscending = false, label = "License Number Z-a"),
}