package org.diiage.projet_rattrapage.data.hardware

import android.content.Context
import android.media.AudioManager as SystemAudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import timber.log.Timber


/**
 * Manager centralisé pour la gestion des capacités audio et haptiques du device
 * 
 * Cette classe applique plusieurs design patterns :
 * - Singleton Pattern : une seule instance pour gérer l'audio
 * - Facade Pattern : simplifie l'accès aux APIs système complexes
 * - Strategy Pattern : différentes stratégies selon la version Android
 * 
 * Capacités natives gérées :
 * 1. **Audio** : contrôle du volume, mode audio, focus audio
 * 2. **Vibration** : feedback haptique pour les interactions
 * 
 * Principes SOLID appliqués :
 * - Single Responsibility : gère uniquement les fonctionnalités audio/haptiques
 * - Open/Closed : extensible pour nouvelles fonctionnalités audio
 * - Dependency Inversion : utilise l'injection de dépendances
 * 
 * @property context Contexte Android pour accéder aux services système
 * 

 * @since 1.0
 */
class AudioManager(
    private val context: Context
) {
    
    // ================================
    // SERVICES SYSTÈME
    // ================================
    
    /**
     * Service système pour la gestion audio
     */
    private val systemAudioManager: SystemAudioManager by lazy {
        context.getSystemService(Context.AUDIO_SERVICE) as SystemAudioManager
    }
    
    /**
     * Service vibration selon la version Android
     * Applique le pattern Strategy pour gérer les différences d'API
     */
    private val vibrator: Vibrator by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12+ : utilise VibratorManager
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            // Android < 12 : utilise Vibrator directement
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
    
    // ================================
    // GESTION DU VOLUME AUDIO
    // ================================
    
    /**
     * Récupère le volume actuel de la musique
     * 
     * @return Volume actuel entre 0 et getMaxMusicVolume()
     */
    fun getCurrentMusicVolume(): Int {
        val volume = systemAudioManager.getStreamVolume(SystemAudioManager.STREAM_MUSIC)
        Timber.d("🔊 Volume musique actuel: $volume")
        return volume
    }
    
    /**
     * Récupère le volume maximum possible pour la musique
     * 
     * @return Volume maximum
     */
    fun getMaxMusicVolume(): Int {
        val maxVolume = systemAudioManager.getStreamMaxVolume(SystemAudioManager.STREAM_MUSIC)
        Timber.d("🔊 Volume musique maximum: $maxVolume")
        return maxVolume
    }
    

    

    
    // ================================
    // GESTION DES MODES AUDIO
    // ================================
    

    

    
    // ================================
    // FEEDBACK HAPTIQUE (VIBRATION)
    // ================================
    
    /**
     * Produit un feedback haptique court pour les interactions légères
     * 
     * Utilisé pour :
     * - Clics sur les boutons
     * - Sélection d'éléments
     * - Confirmations rapides
     */
    fun performLightHapticFeedback() {
        performHapticFeedback(HapticType.LIGHT)
    }
    
    /**
     * Produit un feedback haptique moyen pour les interactions importantes
     * 
     * Utilisé pour :
     * - Début/fin de lecture
     * - Changement d'écran
     * - Actions importantes
     */
    fun performMediumHapticFeedback() {
        performHapticFeedback(HapticType.MEDIUM)
    }
    
    /**
     * Produit un feedback haptique fort pour les erreurs ou alertes
     * 
     * Utilisé pour :
     * - Erreurs de connexion
     * - Alertes importantes
     * - Échecs d'opération
     */
    fun performHeavyHapticFeedback() {
        performHapticFeedback(HapticType.HEAVY)
    }
    
    /**
     * Exécute le feedback haptique selon le type demandé
     * 
     * Cette méthode applique le pattern Strategy en utilisant
     * différentes APIs selon la version Android disponible
     * 
     * @param type Type de feedback haptique souhaité
     */
    private fun performHapticFeedback(type: HapticType) {
        if (!vibrator.hasVibrator()) {
            Timber.w("⚠️ Aucun vibrateur disponible sur ce device")
            return
        }
        
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Android 8+ : utilise VibrationEffect (API moderne)
                val effect = when (type) {
                    HapticType.LIGHT -> VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                    HapticType.MEDIUM -> VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                    HapticType.HEAVY -> VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
                }
                
                vibrator.vibrate(effect)
                Timber.d("📳 Feedback haptique $type exécuté (VibrationEffect)")
            } else {
                // Android < 8 : utilise vibrate() simple
                val duration = when (type) {
                    HapticType.LIGHT -> 50L
                    HapticType.MEDIUM -> 100L
                    HapticType.HEAVY -> 200L
                }
                
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
                Timber.d("📳 Feedback haptique $type exécuté (vibrate simple)")
            }
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors du feedback haptique")
        }
    }
    
    // ================================
    // MÉTHODES UTILITAIRES
    // ================================
    

    

}

/**
 * Énumération des types de feedback haptique
 * 
 * Applique le principe d'encapsulation en définissant
 * clairement les intensités de vibration disponibles
 */
enum class HapticType {
    LIGHT,   // Feedback léger (50ms)
    MEDIUM,  // Feedback moyen (100ms)  
    HEAVY    // Feedback fort (200ms)
} 