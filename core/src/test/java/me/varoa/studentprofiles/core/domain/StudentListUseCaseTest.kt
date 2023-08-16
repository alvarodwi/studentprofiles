package me.varoa.studentprofiles.core.domain

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import me.varoa.studentprofiles.core.DummyHelper
import me.varoa.studentprofiles.core.TestDispatcherRule
import me.varoa.studentprofiles.core.TestPagingSource
import me.varoa.studentprofiles.core.buildDiffer
import me.varoa.studentprofiles.core.data.interactor.StudentListInteractor
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.domain.usecase.StudentListUseCase
import me.varoa.studentprofiles.core.minifiedStudentDiffer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class StudentListUseCaseTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @MockK
    lateinit var student: StudentRepository

    private lateinit var useCase: StudentListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `query student items`() =
        runTest {
            val expected = mutableListOf<StudentMinified>()
            for (i in 1..Random.nextInt(50, 100)) {
                expected.add(DummyHelper.generateTestStudentMinified(i))
            }

            coEvery { student.getStudents(allAny()) } returns
                flow {
                    emit(TestPagingSource.snapshot(expected))
                }
            useCase = StudentListInteractor(student)

            val actual = useCase.getStudents(StudentQuery())
            coVerify { useCase.getStudents(allAny()) }

            val differ = buildDiffer(minifiedStudentDiffer, testDispatcherRule.dispatcher)
            actual.test {
                differ.submitData(awaitItem())
                awaitComplete()
            }

            advanceUntilIdle()

            // assert paging data not null
            assertNotNull(differ.snapshot())
            // assert paging data size is same
            assertEquals(expected.size, differ.snapshot().size)
            // assert first data is the same
            assertEquals(expected[0], differ.snapshot()[0])
            // assert random data within the list is the same
            val rand = Random.nextInt(1, expected.size)
            assertEquals(expected[rand], differ.snapshot()[rand])
        }
}
