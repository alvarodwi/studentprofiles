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
import me.varoa.studentprofiles.core.data.interactor.FavoriteListInteractor
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.repository.FavoriteRepository
import me.varoa.studentprofiles.core.domain.usecase.FavoriteListUseCase
import me.varoa.studentprofiles.core.minifiedStudentDiffer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FavoriteListUseCaseTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @MockK
    lateinit var favorite: FavoriteRepository

    private lateinit var useCase: FavoriteListUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `query favorite items`() =
        runTest {
            val expected = mutableListOf<StudentMinified>()
            for (i in 1..Random.nextInt(1, 10)) {
                expected.add(DummyHelper.generateTestStudentMinified(i))
            }

            coEvery { favorite.getFavorites() } returns
                flow {
                    emit(TestPagingSource.snapshot(expected))
                }
            useCase = FavoriteListInteractor(favorite)

            val actual = useCase.getFavorites()
            coVerify { useCase.getFavorites() }

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
        }
}
