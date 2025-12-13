package com.um.programacion2.screens

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.um.programacion2.screens.tabs.EventosTab
import com.um.programacion2.screens.tabs.PerfilTab
import com.um.programacion2.screens.tabs.UserVentasTab

class MainNavigationScreen : Screen {

    @Composable
    override fun Content() {
        TabNavigator(EventosTab) {
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        // Agregar items barra inferior
                        TabNavigationItem(EventosTab)
                        TabNavigationItem(UserVentasTab)
                        TabNavigationItem(PerfilTab)
                    }
                }
            ) { innerPadding ->
                // Renderizar contenido del Tab actual
                Box(modifier = Modifier.padding(innerPadding)) {
                    CurrentTab()
                }
            }
        }
    }
}

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current

        NavigationBarItem(
            selected = tabNavigator.current == tab,
            onClick = { tabNavigator.current = tab },
            icon = {
                tab.options.icon?.let { icon ->
                    Icon(painter = icon, contentDescription = tab.options.title)
                }
            },
            label = {
                Text(text = tab.options.title)
            }
        )
    }
