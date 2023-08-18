package me.varoa.studentprofiles.core.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import io.mockk.confirmVerified
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import me.varoa.studentprofiles.core.DummyHelper
import me.varoa.studentprofiles.core.TestDispatcherRule
import me.varoa.studentprofiles.core.data.local.AppDatabase
import me.varoa.studentprofiles.core.data.local.dao.FavoriteDao
import me.varoa.studentprofiles.core.data.local.dao.StudentDao
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.util.asEntity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(AndroidJUnit4::class)
@SmallTest
class AppDatabaseTest {
    @get:Rule
    val instantRule = InstantTaskExecutorRule()

    @get: Rule
    val dispatcherRule = TestDispatcherRule()

    private lateinit var favoriteDao: FavoriteDao
    private lateinit var studentDao: StudentDao
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database =
            Room
                .inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .build()
        favoriteDao = database.favoriteDao
        studentDao = database.studentDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insert_students_and_get_student_id() =
        runTest {
            val dao: StudentDao = spyk(studentDao)
            val totalItem = Random.nextInt(2, 20)
            for (i in 1..totalItem) {
                dao.insert(DummyHelper.generateTestStudent(i).asEntity())
            }
            // verify that dao.insert exactly called n times
            verify(exactly = totalItem) { dao.insert(allAny()) }
            confirmVerified(dao)

            // check first item
            studentDao.getStudent(1).test {
                val data = awaitItem()
                assertEquals(1, data.id)
                assertEquals("Student_1", data.name)
            }

            // check last item
            studentDao.getStudent(totalItem).test {
                val data = awaitItem()
                assertEquals(totalItem, data.id)
                assertEquals("Student_$totalItem", data.name)
            }
        }

    @Test
    fun get_students_after_insert() =
        runTest {
            // insert
            val totalItem = Random.nextInt(50, 100)
            for (i in 1..totalItem) {
                studentDao.insert(DummyHelper.generateTestStudent(i).asEntity())
            }

            // get student
            val query = StudentQuery().generateQuery()
            val actualData = Pager(PagingConfig(pageSize = 20)) { studentDao.getAll(query) }.flow
            val job =
                launch {
                    actualData.collectLatest {
                        studentDiffer.submitData(it)
                    }
                }

            advanceUntilIdle()

            // assert paging data not null
            assertNotNull(studentDiffer.snapshot())
            // assert paging data size is now total item
            assertEquals(totalItem, studentDiffer.snapshot().size)

            job.cancel()
        }

    @Test
    fun add_to_and_remove_from_favorite() =
        runTest {
            // insert
            val totalItem = Random.nextInt(5, 15)
            for (i in 1..totalItem) {
                studentDao.insert(DummyHelper.generateTestStudent(i).asEntity())
            }

            // add to favorite
            val itemFavorite = Random.nextInt(1, totalItem)
            favoriteDao.updateFavorite(itemFavorite, true)
            // check if item isFavorite is true
            studentDao.getStudent(itemFavorite).test {
                assertEquals(true, awaitItem().isFavorite)
            }

            // remove from favorite
            favoriteDao.updateFavorite(itemFavorite, false)
            // check if item isFavorite is false now
            studentDao.getStudent(itemFavorite).test {
                assertEquals(false, awaitItem().isFavorite)
            }
        }

    private val studentDiffer =
        AsyncPagingDataDiffer(
            diffCallback =
                object : DiffUtil.ItemCallback<StudentMinified>() {
                    override fun areItemsTheSame(
                        oldItem: StudentMinified,
                        newItem: StudentMinified,
                    ): Boolean = oldItem.id == newItem.id

                    override fun areContentsTheSame(
                        oldItem: StudentMinified,
                        newItem: StudentMinified,
                    ): Boolean = oldItem == newItem
                },
            updateCallback =
                object : ListUpdateCallback {
                    override fun onInserted(
                        position: Int,
                        count: Int,
                    ) {
                    }

                    override fun onRemoved(
                        position: Int,
                        count: Int,
                    ) {
                    }

                    override fun onMoved(
                        fromPosition: Int,
                        toPosition: Int,
                    ) {
                    }

                    override fun onChanged(
                        position: Int,
                        count: Int,
                        payload: Any?,
                    ) {
                    }
                },
            mainDispatcher = dispatcherRule.dispatcher,
            workerDispatcher = dispatcherRule.dispatcher,
        )
}
