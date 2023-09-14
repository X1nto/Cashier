package com.xinto.cashier.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.xinto.cashier.ui.screen.registry.RegistryFragment
import com.xinto.cashier.ui.view.Tab
import com.xinto.cashier.R
import com.xinto.cashier.ui.navigation.Destination
import com.xinto.cashier.ui.screen.income.IncomeFragment
import com.xinto.cashier.ui.screen.main.MainViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val registryTab = findViewById<Tab>(R.id.tab_registry)
            .apply {
                setOnClickListener {
                    viewModel.navigate(Destination.Registry)
                }
            }
        val statusTab = findViewById<Tab>(R.id.tab_status)
            .apply {
                setOnClickListener {
                    viewModel.navigate(Destination.Status)
                }
            }

        val registryFragment = RegistryFragment()
        val incomeFragment = IncomeFragment()
        supportFragmentManager.commit {
            add(R.id.main_fragment, registryFragment)
            add(R.id.main_fragment, incomeFragment)
        }

        viewModel.screen
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    when (it) {
                        is Destination.Registry -> {
                            registryTab.isSelected = true
                            statusTab.isSelected = false
                            show(registryFragment)
                            hide(incomeFragment)
                        }
                        else -> {
                            statusTab.isSelected = true
                            registryTab.isSelected = false
                            show(incomeFragment)
                            hide(registryFragment)
                        }
                    }

                }
            }.launchIn(lifecycleScope)
    }
}