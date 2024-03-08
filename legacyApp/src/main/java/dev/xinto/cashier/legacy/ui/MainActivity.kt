package dev.xinto.cashier.legacy.ui

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dev.xinto.cashier.common.ui.navigation.Destination
import dev.xinto.cashier.common.ui.screen.main.MainViewModel
import dev.xinto.cashier.legacy.R
import dev.xinto.cashier.legacy.ui.screen.income.IncomeFragment
import dev.xinto.cashier.legacy.ui.screen.products.ProductsFragment
import dev.xinto.cashier.legacy.ui.screen.registry.RegistryFragment
import dev.xinto.cashier.legacy.ui.view.Tab
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
        val productsTab = findViewById<Tab>(R.id.tab_products)
            .apply {
                setOnClickListener {
                    viewModel.navigate(Destination.Products)
                }
            }

        val registryFragment = RegistryFragment()
        val incomeFragment = IncomeFragment()
        val productsFragment = ProductsFragment()
        supportFragmentManager.commit {
            add(R.id.main_fragment, registryFragment)
            add(R.id.main_fragment, incomeFragment)
            add(R.id.main_fragment, productsFragment)
        }

        viewModel.screen
            .flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED)
            .onEach {
                registryTab.isSelected = it == Destination.Registry
                statusTab.isSelected = it == Destination.Status
                productsTab.isSelected = it == Destination.Products

                supportFragmentManager.commit {
                    //garbage code
                    setReorderingAllowed(true)
                    when (it) {
                        Destination.Registry -> {
                            show(registryFragment)
                            hide(incomeFragment)
                            hide(productsFragment)
                        }

                        Destination.Status -> {
                            show(incomeFragment)
                            hide(registryFragment)
                            hide(productsFragment)
                        }

                        Destination.Products -> {
                            show(productsFragment)
                            hide(incomeFragment)
                            hide(registryFragment)
                        }
                    }
                }
            }.launchIn(lifecycleScope)
    }
}