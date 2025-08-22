package org.diiage.projet_rattrapage.data.hardware

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

/**
 * Manager centralisé pour la lecture des extraits audio Deezer
 * 
 * Cette classe applique plusieurs design patterns :
 * - Singleton Pattern : une seule instance pour gérer la lecture audio
 * - State Pattern : gestion des différents états de lecture
 * - Observer Pattern : surveillance des changements d'état via StateFlow
 * - Strategy Pattern : différentes stratégies selon la version Android
 * 
 * Capacité native gérée : **Lecture audio** via MediaPlayer
 * - Lecture des aperçus de 30 secondes de l'API Deezer
 * - Contrôle de la lecture (play, pause, stop)
 * - Gestion des erreurs et des ressources
 * - Surveillance de l'état de lecture en temps réel
 * 
 * Principes SOLID appliqués :
 * - Single Responsibility : gère uniquement la lecture audio
 * - Open/Closed : extensible pour nouveaux formats audio
 * - Dependency Inversion : utilise l'injection de dépendances
 * - Interface Segregation : méthodes spécialisées par fonctionnalité
 * 
 * @property context Contexte Android pour accéder aux services système
 * @property audioManager Manager audio pour la gestion du volume et des permissions
 * 

 * @since 1.0
 */
class AudioPlayer(
    private val context: Context,
    private val audioManager: AudioManager
) {
    
    // ================================
    // ÉTATS DE LECTURE
    // ================================
    
    /**
     * État actuel de la lecture audio
     */
    private val _playbackState = MutableStateFlow<PlaybackState>(PlaybackState.IDLE)
    val playbackState: StateFlow<PlaybackState> = _playbackState.asStateFlow()
    
    /**
     * URL de l'extrait actuellement en cours de lecture
     */
    private val _currentPreviewUrl = MutableStateFlow<String?>(null)
    val currentPreviewUrl: StateFlow<String?> = _currentPreviewUrl.asStateFlow()
    
    /**
     * Progression de la lecture (0.0 à 1.0)
     */
    private val _playbackProgress = MutableStateFlow(0f)
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()
    
    // ================================
    // COMPOSANTS DE LECTURE
    // ================================
    
    /**
     * Lecteur média Android pour la lecture des extraits
     */
    private var mediaPlayer: MediaPlayer? = null
    
    /**
     * URL de l'extrait en cours de lecture
     */
    private var currentUrl: String? = null
    
    /**
     * Timer pour la mise à jour de la progression
     */
    private var progressUpdateJob: kotlinx.coroutines.Job? = null
    
    // ================================
    // GESTION DU CYCLE DE VIE
    // ================================
    
    /**
     * Initialise le lecteur audio avec les callbacks appropriés
     */
    init {
        Timber.d("🎵 Initialisation du lecteur audio")
        setupMediaPlayerCallbacks()
    }
    
    /**
     * Configure les callbacks du MediaPlayer pour la gestion des événements
     */
    private fun setupMediaPlayerCallbacks() {
        // Les callbacks seront configurés lors de la création du MediaPlayer
    }
    
    // ================================
    // CONTRÔLES DE LECTURE
    // ================================
    
    /**
     * Lance la lecture d'un extrait audio (version synchrone)
     * 
     * Cette méthode applique le pattern Strategy en gérant
     * différemment la lecture selon l'état actuel
     * 
     * @param previewUrl URL de l'extrait à lire
     * @return true si la lecture a été lancée avec succès
     */
    fun playPreview(previewUrl: String): Boolean {
        return try {
            // Validation de l'URL
            if (!isValidPreviewUrl(previewUrl)) {
                Timber.w("⚠️ URL d'extrait invalide: $previewUrl")
                return false
            }
            
            // Arrêt de la lecture en cours si nécessaire
            if (isPlaying()) {
                stopPlayback()
            }
            
            // Création d'un nouveau MediaPlayer
            createNewMediaPlayer()
            
            // Configuration et démarrage de la lecture
            mediaPlayer?.let { player ->
                player.setDataSource(previewUrl)
                player.prepareAsync()
                
                currentUrl = previewUrl
                _currentPreviewUrl.value = previewUrl
                _playbackState.value = PlaybackState.PREPARING
                
                Timber.i("🎵 Démarrage de la lecture de l'extrait: $previewUrl")
                true
            } ?: false
            
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors du lancement de la lecture")
            _playbackState.value = PlaybackState.ERROR
            false
        }
    }
    
    /**
     * Met en pause la lecture en cours
     * 
     * @return true si la pause a été appliquée
     */
    fun pausePlayback(): Boolean {
        return try {
            if (isPlaying()) {
                mediaPlayer?.pause()
                _playbackState.value = PlaybackState.PAUSED
                Timber.d("⏸️ Lecture mise en pause")
                true
            } else {
                false
            }
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de la mise en pause")
            false
        }
    }
    
    /**
     * Reprend la lecture en cours
     * 
     * @return true si la reprise a été appliquée
     */
    fun resumePlayback(): Boolean {
        return try {
            if (_playbackState.value == PlaybackState.PAUSED) {
                mediaPlayer?.start()
                _playbackState.value = PlaybackState.PLAYING
                Timber.d("▶️ Lecture reprise")
                true
            } else {
                false
            }
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de la reprise de lecture")
            false
        }
    }
    
    /**
     * Arrête complètement la lecture
     * 
     * @return true si l'arrêt a été appliqué
     */
    fun stopPlayback(): Boolean {
        return try {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
                mediaPlayer = null
            }
            
            currentUrl = null
            _currentPreviewUrl.value = null
            _playbackState.value = PlaybackState.IDLE
            _playbackProgress.value = 0f
            
            // Arrêt du job de progression
            progressUpdateJob?.cancel()
            progressUpdateJob = null
            
            Timber.d("⏹️ Lecture arrêtée")
            true
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de l'arrêt de la lecture")
            false
        }
    }
    
    // ================================
    // INFORMATIONS DE LECTURE
    // ================================
    
    /**
     * Vérifie si une lecture est en cours
     * 
     * @return true si le lecteur est en train de jouer
     */
    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }
    
    /**
     * Vérifie si l'extrait spécifié est en cours de lecture
     * 
     * @param previewUrl URL de l'extrait à vérifier
     * @return true si cet extrait est en cours de lecture
     */
    fun isPlayingPreview(previewUrl: String): Boolean {
        return currentUrl == previewUrl && isPlaying()
    }
    
    /**
     * Récupère la durée totale de l'extrait en cours
     * 
     * @return Durée en millisecondes ou 0 si non disponible
     */
    fun getCurrentDuration(): Int {
        return mediaPlayer?.duration ?: 0
    }
    
    /**
     * Récupère la position actuelle de la lecture
     * 
     * @return Position en millisecondes ou 0 si non disponible
     */
    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
    
    // ================================
    // MÉTHODES PRIVÉES
    // ================================
    
    /**
     * Crée un nouveau MediaPlayer avec les callbacks appropriés
     */
    private fun createNewMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setOnPreparedListener { player ->
                _playbackState.value = PlaybackState.PLAYING
                player.start()
                startProgressTracking()
                Timber.d("🎵 MediaPlayer prêt, lecture démarrée")
            }
            
            setOnCompletionListener { player ->
                _playbackState.value = PlaybackState.COMPLETED
                _playbackProgress.value = 1f
                stopProgressTracking()
                Timber.d("🎵 Lecture terminée")
            }
            
            setOnErrorListener { player, what, extra ->
                _playbackState.value = PlaybackState.ERROR
                Timber.e("❌ Erreur MediaPlayer: what=$what, extra=$extra")
                true
            }
            
            setOnSeekCompleteListener { player ->
                Timber.d("🎵 Seek terminé vers la position: ${player.currentPosition}")
            }
        }
    }
    
    /**
     * Démarre le suivi de la progression de la lecture
     */
    private fun startProgressTracking() {
        progressUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            while (isPlaying()) {
                val position = getCurrentPosition()
                val duration = getCurrentDuration()
                
                if (duration > 0) {
                    val progress = (position.toFloat() / duration).coerceIn(0f, 1f)
                    _playbackProgress.value = progress
                }
                
                delay(100) // Mise à jour toutes les 100ms
            }
        }
    }
    
    /**
     * Arrête le suivi de la progression
     */
    private fun stopProgressTracking() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }
    
    /**
     * Valide une URL d'extrait audio
     * 
     * @param url URL à valider
     * @return true si l'URL est valide
     */
    private fun isValidPreviewUrl(url: String): Boolean {
        return url.isNotBlank() && 
               url.startsWith("http") && 
               (url.contains(".mp3") || url.contains("preview"))
    }
    
    /**
     * Libère les ressources du lecteur audio
     */
    fun release() {
        stopPlayback()
        Timber.d("🎵 Lecteur audio libéré")
    }
}

/**
 * Énumération des états de lecture audio
 * 
 * Applique le principe d'encapsulation en définissant
 * clairement les états possibles du lecteur
 */
enum class PlaybackState {
    IDLE,       // Aucune lecture en cours
    PREPARING,  // Préparation de la lecture
    PLAYING,    // Lecture en cours
    PAUSED,     // Lecture en pause
    COMPLETED,  // Lecture terminée
    ERROR       // Erreur de lecture
}
