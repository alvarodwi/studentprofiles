package me.varoa.studentprofiles

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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

        lifecycleScope.launch {
            toggleAppTheme(prefs.theme.first())
        }
    }
}
