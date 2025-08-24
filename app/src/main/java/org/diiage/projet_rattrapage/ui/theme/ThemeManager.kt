package org.diiage.projet_rattrapage.ui.theme

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gestionnaire centralisé de thème pour l'application Deezer Music
 * 
 * Cette classe applique le pattern Singleton pour gérer l'état du thème
 * de manière centralisée dans toute l'application.
 * 
 * Design patterns appliqués :
 * - Singleton Pattern : une seule instance pour gérer l'état global
 * - Observer Pattern : StateFlow pour la réactivité de l'UI
 * - State Management Pattern : gestion centralisée de l'état du thème
 * - Reactive Programming Pattern : programmation réactive avec StateFlow
 * 
 * Responsabilités :
 * - Gestion de l'état dark/light mode
 * - Persistance des préférences utilisateur  
 * - Réactivité via StateFlow
 * - API simple pour basculer entre les thèmes
 * 
 * @since 1.0
 * @see StateFlow
 * @see Projet_RattrapageTheme
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
     * Retourne une description textuelle du mode actuel
     * 
     * @return "Mode sombre" ou "Mode clair"
     */
    fun getCurrentThemeDescription(): String {
        return if (_isDarkTheme.value) "Mode sombre" else "Mode clair"
    }
}