package me.varoa.studentprofiles.core.domain

import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import me.varoa.studentprofiles.core.DummyHelper
import me.varoa.studentprofiles.core.TestDispatcherRule
import me.varoa.studentprofiles.core.data.interactor.StudentDetailInteractor
import me.varoa.studentprofiles.core.domain.repository.FavoriteRepository
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.domain.usecase.StudentDetailUseCase
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class StudentDetailUseCaseTest {
    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @MockK
    lateinit var student: StudentRepository

    @MockK
    lateinit var favorite: FavoriteRepository

    private lateinit var useCase: StudentDetailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `get student detail`() =
        runTest {
            val expected = DummyHelper.generateTestStudent(1)
            coEvery { student.getStudent(1) } returns
                flow {
                    emit(expected)
                }

            useCase = StudentDetailInteractor(student, favorite)
            val actual = useCase.getStudent(1)
            coVerify { student.getStudent(1) }

            actual.test {
                val data = awaitItem()
                assertEquals(expected.id, data.id)
                assertEquals(expected.name, data.name)
                awaitComplete()
            }
        }

    @Test
    fun `add and remove favorite`() =
        runTest {
            var item = DummyHelper.generateTestStudent(1)
            coEvery { favorite.addToFavorite(1) } answers {
                item = item.copy(isFavorite = true)
            }
            coEvery { favorite.removeFromFavorite(1) } answers {
                item = item.copy(isFavorite = false)
            }
            coEvery { student.getStudent(1) } returns flow { emit(item) }

            useCase = StudentDetailInteractor(student, favorite)

            // test add favorite
            useCase.addToFavorite(1)
            coVerify { favorite.addToFavorite(1) }
            val actual = useCase.getStudent(1)
            actual.test {
                val data = awaitItem()
                assertEquals(true, data.isFavorite)
                awaitComplete()
            }

            // test remove favorite
            useCase.removeFromFavorite(1)
            coVerify { favorite.removeFromFavorite(1) }
            actual.test {
                val data = awaitItem()
                assertEquals(false, data.isFavorite)
                awaitComplete()
            }
        }
}
