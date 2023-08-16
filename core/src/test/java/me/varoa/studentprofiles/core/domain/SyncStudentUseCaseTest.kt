package me.varoa.studentprofiles.core.domain

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import me.varoa.studentprofiles.core.DummyHelper
import me.varoa.studentprofiles.core.TestDispatcherRule
import me.varoa.studentprofiles.core.data.interactor.SyncStudentInteractor
import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.data.prefs.DataStoreManager
import me.varoa.studentprofiles.core.domain.model.SyncInterval
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.domain.usecase.SyncStudentUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class SyncStudentUseCaseTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @MockK
    lateinit var student: StudentRepository

    @MockK
    lateinit var prefs: DataStoreManager

    private lateinit var useCase: SyncStudentUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `insert students`() =
        runTest {
            val actual = mutableListOf<StudentEntity>()
            val expectedTotal = Random.nextInt(50, 150)

            // capturing to avoid ClassCastException https://mockk.io/#capturing
            val entitySlot = slot<StudentEntity>()
            coEvery { student.insertStudent(capture(entitySlot)) } answers {
                actual.add(entitySlot.captured)
            }

            useCase = SyncStudentInteractor(student, prefs)
            for (i in 1..expectedTotal) {
                useCase.insertStudent(DummyHelper.generateTestStudentEntity(i))
            }
            coVerify(exactly = expectedTotal) { student.insertStudent(any()) }

            // assert the list is now sized as expected
            assertEquals(expectedTotal, actual.size)
        }

    @Test
    fun `delete all students`() =
        runTest {
            val actual = mutableListOf<StudentEntity>()
            val total = Random.nextInt(50, 150)
            for (i in 1..total) {
                actual.add(DummyHelper.generateTestStudentEntity(i))
            }

            coEvery { student.deleteAllStudent() } answers {
                actual.clear()
            }

            useCase = SyncStudentInteractor(student, prefs)
            // check if total data is sized the same before deletion
            assertEquals(total, actual.size)
            useCase.deleteAllStudent()
            coVerify { student.deleteAllStudent() }
            // check if total data is now 0
            assertEquals(0, actual.size)
        }

    @Test
    fun `simulate fetch sync interval from prefs`() =
        runTest {
            var currentSyncInterval = SyncInterval.WEEKLY.name
            coEvery { prefs.syncInterval } returns
                flow {
                    emit(currentSyncInterval)
                }

            useCase = SyncStudentInteractor(student, prefs)
            val interval1 = useCase.getSyncInterval()
            coVerify { prefs.syncInterval }
            // check if interval is the same
            assertEquals(currentSyncInterval, interval1.name)

            // change the interval to biweekly
            currentSyncInterval = SyncInterval.BIWEEKLY.name
            val interval2 = useCase.getSyncInterval()
            coVerify { prefs.syncInterval }
            assertEquals(currentSyncInterval, interval2.name)
        }
}
