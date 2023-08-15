package me.varoa.studentprofiles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import me.varoa.studentprofiles.core.data.prefs.DataStoreManager
import me.varoa.studentprofiles.ext.toggleAppTheme
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val prefs by inject<DataStoreManager>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // setting up nav component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_main)

        lifecycleScope.launch {
            toggleAppTheme(prefs.theme.first())

            if (isFirstTimeSync()) {
                graph.setStartDestination(R.id.syncFragment)
            } else {
                graph.setStartDestination(R.id.nav_home)
            }
            // bind navGraph to fragment
            val navController = navHostFragment.navController
            navController.setGraph(graph, intent.extras)
        }
    }

    private suspend fun isFirstTimeSync() = prefs.firstTimeSync.first()
}
