package com.example.sawitprotest.data

import android.content.SharedPreferences
import com.example.sawitprotest.data.local.LocalWeightBridgeApiImpl
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.google.common.base.Verify.verify
import com.google.gson.Gson
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

class LocalWeightBridgeApiImplTest {

    @Test
    fun `test setEntryList`() {
        // Given
        val editor = mockk<SharedPreferences.Editor>(relaxed = true)
        val sharedPreferences = mockk<SharedPreferences> {
            every { edit() } returns editor
        }
        val gson = Gson()
        val localWeightBridgeApi = LocalWeightBridgeApiImpl(context = mockk(relaxed = true), sharedPreferences = sharedPreferences, gson = gson)
        val entryList = listOf(BridgeWeightEntryItem())

        // When
        localWeightBridgeApi.setEntryList(entryList)

        // Then
        verify { editor.putString("entryList", gson.toJson(entryList)) }
        verify { editor.apply() }
    }

    @Test
    fun `test getEntryList`() {
        // Given
        val dummy = listOf(
            BridgeWeightEntryItem(id = "testID")
        )
        val sharedPreferences = mockk<SharedPreferences> {
            every { getString("entryList", null) } returns Gson().toJson(dummy)
        }
        val gson = Gson()
        val localWeightBridgeApi = LocalWeightBridgeApiImpl(context = mockk(relaxed = true), sharedPreferences = sharedPreferences, gson = gson)

        // When
        val result = localWeightBridgeApi.getEntryList()

        // Then
        assertEquals(1, result.size)
        assertEquals("testID", result[0].id)
    }
}