package com.example.sawitprotest.data
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.data.remote.model.SearchModel
import com.example.sawitprotest.feature.weighbridge.model.SortType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchModelTest {

    private lateinit var searchModel: SearchModel
    private lateinit var bridgeWeightEntryItem: BridgeWeightEntryItem

    @Before
    fun setUp() {
        searchModel = SearchModel()
        bridgeWeightEntryItem = BridgeWeightEntryItem(inboundWeight = 50.0, outboundWeight = 100.0)
    }

    @Test
    fun `test SearchModel default values`() {
        assertEquals("", searchModel.keyworkd)
        assertEquals(Long.MAX_VALUE, searchModel.filter.maxDate)
        assertEquals(0, searchModel.filter.minDate)
        assertEquals("", searchModel.filter.driverNameContain)
        assertEquals("", searchModel.filter.licenseNumberContain)
        assertEquals(SortType.NONE, searchModel.sort.sortType)
    }

    @Test
    fun `test BridgeWeightEntryItem default values`() {
        assertEquals("", bridgeWeightEntryItem.id)
        assertEquals(0L, bridgeWeightEntryItem.dateTime)
        assertEquals("", bridgeWeightEntryItem.licenseNumber)
        assertEquals("", bridgeWeightEntryItem.driverName)
        assertEquals(50.0, bridgeWeightEntryItem.inboundWeight, 0.0)
        assertEquals(100.0, bridgeWeightEntryItem.outboundWeight,   0.0)
    }

    @Test
    fun `test BridgeWeightEntryItem netWeight`() {
        assertEquals(50.0, bridgeWeightEntryItem.netWeight,   0.0)
    }

}