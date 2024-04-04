package com.example.sawitprotest.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sawitprotest.data.remote.api.firebase.WeighBridgeApiImpl
import com.example.sawitprotest.data.remote.model.BridgeWeightEntryItem
import com.example.sawitprotest.data.remote.model.SearchModel
import com.example.sawitprotest.feature.weighbridge.model.SortType
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeighBridgeApiImplTest {

    val dispatcher = TestCoroutineDispatcher()
    var setupCalled = false

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    @Throws(Exception::class)
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }



    @Test
    fun `test deleteEntry`() = runTest {
        val entry = BridgeWeightEntryItem(
            id = "1",
            driverName = "Bridge One",
            licenseNumber = "LIC001",
            dateTime = 14324234234
        )

        val weighBridgeReference = mockk<DatabaseReference>()
        coEvery { weighBridgeReference.push().key } returns "1"
        coEvery { weighBridgeReference.child(any()) } returns weighBridgeReference
        coEvery { weighBridgeReference.removeValue() } returns mockk<Task<Void>>()

        var  taskWait :  suspend (Task<Void>) -> Any? = { task -> null }
        val weighBridgeApi = WeighBridgeApiImpl(mockk(), weighBridgeReference)
        weighBridgeApi.taskWait = taskWait
        val result = weighBridgeApi.deleteEntry(entry.id)
        assertTrue(result)
    }

    @Test
    fun `test updateEntry`() = runTest {
        val entry = BridgeWeightEntryItem(
            id = "1",
            driverName = "Bridge One",
            licenseNumber = "LIC001",
            dateTime = 14324234234
        )

        val weighBridgeReference = mockk<DatabaseReference>()
        coEvery { weighBridgeReference.push().key } returns "1"
        coEvery { weighBridgeReference.child(any()) } returns weighBridgeReference
        coEvery { weighBridgeReference.setValue(any()) } returns mockk<Task<Void>>()

        var  taskWait :  suspend (Task<Void>) -> Any? = { task -> null }
        val weighBridgeApi = WeighBridgeApiImpl(mockk(), weighBridgeReference)
        weighBridgeApi.taskWait = taskWait
        val result = weighBridgeApi.updateEntry(entry)
        assertTrue(result)
    }

    @Test
    fun `test createEntry fail`() = runTest {
        val entry = BridgeWeightEntryItem(
            id = "1",
            driverName = "Bridge One",
            licenseNumber = "LIC001",
            dateTime = 14324234234
        )

        val weighBridgeReference = mockk<DatabaseReference>()
        coEvery { weighBridgeReference.push().key } returns null
        coEvery { weighBridgeReference.child(any()) } returns weighBridgeReference
        coEvery { weighBridgeReference.setValue(any()) } returns mockk<Task<Void>>()

        var  taskWait :  suspend (Task<Void>) -> Any? = { task -> null }
        val weighBridgeApi = WeighBridgeApiImpl(mockk(), weighBridgeReference)
        weighBridgeApi.taskWait = taskWait
        val result = weighBridgeApi.createEntry(entry)
        assertFalse(result)
    }

    @Test
    fun `test createEntry`() = runTest {
        val entry = BridgeWeightEntryItem(
            id = "1",
            driverName = "Bridge One",
            licenseNumber = "LIC001",
            dateTime = 14324234234
        )

        val weighBridgeReference = mockk<DatabaseReference>()
        coEvery { weighBridgeReference.push().key } returns "1"
        coEvery { weighBridgeReference.child(any()) } returns weighBridgeReference
        coEvery { weighBridgeReference.setValue(any()) } returns mockk<Task<Void>>()

        var  taskWait :  suspend (Task<Void>) -> Any? = { task -> null }
        val weighBridgeApi = WeighBridgeApiImpl(mockk(), weighBridgeReference)
        weighBridgeApi.taskWait = taskWait
        val result = weighBridgeApi.createEntry(entry)
        assertTrue(result)
    }

    @Test
    fun `test readEntries sortEarliestDate`() = runTest {
        // Given
        val dataSnapshot = mockk<DataSnapshot> {
            coEvery { children } returns listOf(
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "1",
                        driverName = "Bridge One",
                        licenseNumber = "LIC001",
                        dateTime = 14324234234
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "2",
                        driverName = "Bridge Two",
                        licenseNumber = "LIC002",
                        dateTime = 14324234278
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "3",
                        driverName = "Bridge Three",
                        licenseNumber = "LIC003",
                        dateTime = 14324234290
                    )
                }
            )
        }

        val weighBridgeApi = WeighBridgeApiImpl(mockk(), mockk())
        val searchModel = SearchModel()
        searchModel.sort.sortType = SortType.NEWEST_DATE

        val mock : suspend (Query) -> DataSnapshot = { query -> dataSnapshot }
        weighBridgeApi.processAwait = mock

        // When
        val result = weighBridgeApi.readEntries(searchModel)
        // Then
        assertEquals(3, result.size)

        assertEquals("1", result[2].id)
        assertEquals("Bridge One", result[2].driverName)
        assertEquals("LIC001", result[2].licenseNumber)

        assertEquals("2", result[1].id)
        assertEquals("Bridge Two", result[1].driverName)
        assertEquals("LIC002", result[1].licenseNumber)

        assertEquals("3", result[0].id)
        assertEquals("Bridge Three", result[0].driverName)
        assertEquals("LIC003", result[0].licenseNumber)
    }

    @Test
    fun `test readEntries sortOlderDate`() = runTest {
        // Given
        val dataSnapshot = mockk<DataSnapshot> {
            coEvery { children } returns listOf(
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "1",
                        driverName = "Bridge One",
                        licenseNumber = "LIC001",
                        dateTime = 14324234234
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "2",
                        driverName = "Bridge Two",
                        licenseNumber = "LIC002",
                        dateTime = 14324234278
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "3",
                        driverName = "Bridge Three",
                        licenseNumber = "LIC003",
                        dateTime = 14324234290
                    )
                }
            )
        }

        val weighBridgeApi = WeighBridgeApiImpl(mockk(), mockk())
        val searchModel = SearchModel()
        searchModel.sort.sortType = SortType.OLDEST_DATE

        val mock : suspend (Query) -> DataSnapshot = { query -> dataSnapshot }
        weighBridgeApi.processAwait = mock

        // When
        val result = weighBridgeApi.readEntries(searchModel)
        // Then
        assertEquals(3, result.size)

        assertEquals("1", result[0].id)
        assertEquals("Bridge One", result[0].driverName)
        assertEquals("LIC001", result[0].licenseNumber)

        assertEquals("2", result[1].id)
        assertEquals("Bridge Two", result[1].driverName)
        assertEquals("LIC002", result[1].licenseNumber)

        assertEquals("3", result[2].id)
        assertEquals("Bridge Three", result[2].driverName)
        assertEquals("LIC003", result[2].licenseNumber)
    }

    @Test
    fun `test readEntries sortDriverNameDesc`() = runTest {
        // Given
        val dataSnapshot = mockk<DataSnapshot> {
            coEvery { children } returns listOf(
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "1",
                        driverName = "Bridge One",
                        licenseNumber = "LIC001",
                        dateTime = 14324234234
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "2",
                        driverName = "Bridge Two",
                        licenseNumber = "LIC002",
                        dateTime = 14324234278
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "3",
                        driverName = "Bridge Three",
                        licenseNumber = "LIC003",
                        dateTime = 14324234290
                    )
                }
            )
        }

        val weighBridgeApi = WeighBridgeApiImpl(mockk(), mockk())
        val searchModel = SearchModel()
        searchModel.sort.sortType = SortType.DRIVER_NAME_DESC

        val mock : suspend (Query) -> DataSnapshot = { query -> dataSnapshot }
        weighBridgeApi.processAwait = mock

        // When
        val result = weighBridgeApi.readEntries(searchModel)
        // Then
        assertEquals(3, result.size)

        assertEquals("1", result[2].id)
        assertEquals("Bridge One", result[2].driverName)
        assertEquals("LIC001", result[2].licenseNumber)

        assertEquals("2", result[0].id)
        assertEquals("Bridge Two", result[0].driverName)
        assertEquals("LIC002", result[0].licenseNumber)

        assertEquals("3", result[1].id)
        assertEquals("Bridge Three", result[1].driverName)
        assertEquals("LIC003", result[1].licenseNumber)
    }

    @Test
    fun `test readEntries sortDriverName`() = runTest {
        // Given
        val dataSnapshot = mockk<DataSnapshot> {
            coEvery { children } returns listOf(
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "1",
                        driverName = "Bridge One",
                        licenseNumber = "LIC001",
                        dateTime = 14324234234
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "2",
                        driverName = "Bridge Two",
                        licenseNumber = "LIC002",
                        dateTime = 14324234278
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "3",
                        driverName = "Bridge Three",
                        licenseNumber = "LIC003",
                        dateTime = 14324234290
                    )
                }
            )
        }

        val weighBridgeApi = WeighBridgeApiImpl(mockk(), mockk())
        val searchModel = SearchModel()
        searchModel.sort.sortType = SortType.DRIVER_NAME_ASC

        val mock : suspend (Query) -> DataSnapshot = { query -> dataSnapshot }
        weighBridgeApi.processAwait = mock

        // When
        val result = weighBridgeApi.readEntries(searchModel)
        // Then
        assertEquals(3, result.size)

        assertEquals("1", result[0].id)
        assertEquals("Bridge One", result[0].driverName)
        assertEquals("LIC001", result[0].licenseNumber)

        assertEquals("2", result[2].id)
        assertEquals("Bridge Two", result[2].driverName)
        assertEquals("LIC002", result[2].licenseNumber)

        assertEquals("3", result[1].id)
        assertEquals("Bridge Three", result[1].driverName)
        assertEquals("LIC003", result[1].licenseNumber)
    }

    @Test
    fun `test readEntries searchDriverOne`() = runTest {
        // Given
        val dataSnapshot = mockk<DataSnapshot> {
            coEvery { children } returns listOf(
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "1",
                        driverName = "Bridge One",
                        licenseNumber = "LIC001",
                        dateTime = 14324234234
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "2",
                        driverName = "Bridge Two",
                        licenseNumber = "LIC002",
                        dateTime = 14324234278
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "3",
                        driverName = "Bridge Three",
                        licenseNumber = "LIC003",
                        dateTime = 14324234290
                    )
                }
            )
        }

        val weighBridgeApi = WeighBridgeApiImpl(mockk(), mockk())
        val searchModel = SearchModel(keyworkd = "One")

        val mock : suspend (Query) -> DataSnapshot = { query -> dataSnapshot }
        weighBridgeApi.processAwait = mock

        // When
        val result = weighBridgeApi.readEntries(searchModel)
        // Then
        assertEquals(1, result.size)

        assertEquals("1", result[0].id)
        assertEquals("Bridge One", result[0].driverName)
        assertEquals("LIC001", result[0].licenseNumber)
    }

    @Test
    fun `test readEntries filterMaxDateMinDate`() = runTest {
        // Given
        val dataSnapshot = mockk<DataSnapshot> {
            coEvery { children } returns listOf(
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "1",
                        driverName = "Bridge One",
                        licenseNumber = "LIC001",
                        dateTime = 14324234234
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "2",
                        driverName = "Bridge Two",
                        licenseNumber = "LIC002",
                        dateTime = 14324234278
                    )
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(
                        id = "3",
                        driverName = "Bridge Three",
                        licenseNumber = "LIC003",
                        dateTime = 14324234290
                    )
                }
            )
        }

        val weighBridgeApi = WeighBridgeApiImpl(mockk(), mockk())
        val searchModel = SearchModel()

        searchModel.filter.minDate = 14324234235
        searchModel.filter.maxDate = 14324234288

        val mock : suspend (Query) -> DataSnapshot = { query -> dataSnapshot }
        weighBridgeApi.processAwait = mock

        // When
        val result = weighBridgeApi.readEntries(searchModel)
        // Then
        assertEquals(1, result.size)

        assertEquals("2", result[0].id)
        assertEquals("Bridge Two", result[0].driverName)
        assertEquals("LIC002", result[0].licenseNumber)

    }

    @Test
    fun `test readEntries`() = runTest {
        // Given
        val dataSnapshot = mockk<DataSnapshot> {
            coEvery { children } returns listOf(
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(id = "1", driverName = "Bridge One", licenseNumber = "LIC001")
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(id = "2", driverName = "Bridge Two", licenseNumber = "LIC002")
                },
                mockk {
                    coEvery { getValue(BridgeWeightEntryItem::class.java) } returns BridgeWeightEntryItem(id = "3", driverName = "Bridge Three", licenseNumber = "LIC003")
                }
            )
        }

        val weighBridgeApi = WeighBridgeApiImpl(mockk(), mockk())
        val searchModel = SearchModel()

        val mock : suspend (Query) -> DataSnapshot = { query -> dataSnapshot }
        weighBridgeApi.processAwait = mock

        // When
        val result = weighBridgeApi.readEntries(searchModel)
        // Then
        assertEquals(3, result.size)

        assertEquals("1", result[0].id)
        assertEquals("Bridge One", result[0].driverName)
        assertEquals("LIC001", result[0].licenseNumber)

        assertEquals("2", result[1].id)
        assertEquals("Bridge Two", result[1].driverName)
        assertEquals("LIC002", result[1].licenseNumber)

        assertEquals("3", result[2].id)
        assertEquals("Bridge Three", result[2].driverName)
        assertEquals("LIC003", result[2].licenseNumber)
    }
}