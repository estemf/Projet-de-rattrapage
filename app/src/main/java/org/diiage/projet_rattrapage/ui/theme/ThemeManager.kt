package org.diiage.projet_rattrapage.ui.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gestionnaire de thème pour l'application Deezer Music
 * 
 * Cette classe applique le pattern Singleton pour gérer l'état du thème
 * de manière centralisée dans toute l'application.
 * 
 * Responsabilités :
 * - Gestion de l'état dark/light mode
 * - Persistance des préférences utilisateur
 * - Réactivité via StateFlow
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
object ThemeManager {
    
    // ================================
    // ÉTAT DU THÈME
    // ================================
    
    /**
     * StateFlow pour l'état du mode sombre
     * 
     * Utilise la programmation réactive pour notifier
     * automatiquement tous les composants de l'UI
     */
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()
    
    /**
     * Valeur actuelle du mode sombre
     */
    val currentIsDarkTheme: Boolean
        get() = _isDarkTheme.value
    
    // ================================
    // ACTIONS DE THÈME
    // ================================
    
    /**
     * Bascule entre le mode clair et sombre
     * 
     * Cette méthode applique le principe de responsabilité unique
     * en gérant uniquement le changement d'état du thème
     */
    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }
    
    /**
     * Définit explicitement le mode du thème
     * 
     * @param isDark true pour le mode sombre, false pour le mode clair
     */
    fun setDarkTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
    
    /**
     * Réinitialise le thème au mode par défaut (clair)
     */
    fun resetToDefault() {
        _isDarkTheme.value = false
    }
    
    /**
     * Retourne une description textuelle du mode actuel
     * 
     * @return "Mode sombre" ou "Mode clair"
     */
    fun getCurrentThemeDescription(): String {
        return if (_isDarkTheme.value) "Mode sombre" else "Mode clair"
    }
} 