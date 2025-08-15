package org.diiage.projet_rattrapage.data.hardware

import android.content.Context
import android.net.ConnectivityManager as SystemConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber


/**
 * Manager centralisé pour la surveillance et gestion de la connectivité réseau
 * 
 * Cette classe implémente plusieurs design patterns avancés :
 * - Observer Pattern : surveillance temps réel via Flow
 * - Singleton Pattern : instance unique pour la connectivité
 * - Strategy Pattern : différentes approches selon la version Android
 * - State Pattern : gestion des différents états de connexion
 * 
 * Capacité native gérée : **Connectivité réseau**
 * - Surveillance en temps réel des changements de connexion
 * - Détection du type de connexion (WiFi, mobile, Ethernet)
 * - Mesure de la qualité de la connexion
 * - Gestion des transitions réseau
 * 
 * Principes SOLID appliqués :
 * - Single Responsibility : gère uniquement la connectivité
 * - Open/Closed : extensible pour nouveaux types de réseau
 * - Liskov Substitution : compatible avec les différentes versions Android
 * 
 * @property context Contexte Android pour accéder au service système
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
class ConnectivityManager(
    private val context: Context
) {
    
    // ================================
    // SERVICES SYSTÈME
    // ================================
    
    /**
     * Service système de connectivité
     */
    private val systemConnectivityManager: SystemConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as SystemConnectivityManager
    }
    
    // ================================
    // DÉTECTION DE L'ÉTAT DE CONNEXION
    // ================================
    
    /**
     * Vérifie si le device est connecté à Internet
     * 
     * Cette méthode utilise l'API moderne NetworkCapabilities (Android 6+)
     * avec fallback vers l'ancienne API pour la compatibilité
     * 
     * @return true si une connexion Internet est disponible
     */
    fun isConnectedToInternet(): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android 6+ : utilise NetworkCapabilities (API moderne)
                val activeNetwork = systemConnectivityManager.activeNetwork
                val networkCapabilities = systemConnectivityManager.getNetworkCapabilities(activeNetwork)
                
                val isConnected = networkCapabilities?.let { capabilities ->
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                } ?: false
                
                Timber.d("🌐 Connexion Internet (API moderne): $isConnected")
                isConnected
            } else {
                // Android < 6 : utilise NetworkInfo (API dépréciée mais nécessaire)
                @Suppress("DEPRECATION")
                val activeNetworkInfo = systemConnectivityManager.activeNetworkInfo
                @Suppress("DEPRECATION")
                val isConnected = activeNetworkInfo?.isConnectedOrConnecting == true
                
                Timber.d("🌐 Connexion Internet (API legacy): $isConnected")
                isConnected
            }
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de la vérification de connectivité")
            false
        }
    }
    
    /**
     * Détermine le type de connexion réseau active
     * 
     * @return Type de connexion ou NONE si pas de connexion
     */
    fun getConnectionType(): ConnectionType {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Android 6+ : analyse des NetworkCapabilities
                val activeNetwork = systemConnectivityManager.activeNetwork
                val networkCapabilities = systemConnectivityManager.getNetworkCapabilities(activeNetwork)
                
                val connectionType = networkCapabilities?.let { capabilities ->
                    when {
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> ConnectionType.WIFI
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> ConnectionType.MOBILE
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionType.ETHERNET
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> ConnectionType.VPN
                        else -> ConnectionType.OTHER
                    }
                } ?: ConnectionType.NONE
                
                Timber.d("📡 Type de connexion détecté: $connectionType")
                connectionType
            } else {
                // Android < 6 : utilise NetworkInfo
                @Suppress("DEPRECATION")
                val activeNetworkInfo = systemConnectivityManager.activeNetworkInfo
                
                val connectionType = when (activeNetworkInfo?.type) {
                    SystemConnectivityManager.TYPE_WIFI -> ConnectionType.WIFI
                    SystemConnectivityManager.TYPE_MOBILE -> ConnectionType.MOBILE
                    SystemConnectivityManager.TYPE_ETHERNET -> ConnectionType.ETHERNET
                    SystemConnectivityManager.TYPE_VPN -> ConnectionType.VPN
                    else -> if (activeNetworkInfo?.isConnected == true) ConnectionType.OTHER else ConnectionType.NONE
                }
                
                Timber.d("📡 Type de connexion détecté (legacy): $connectionType")
                connectionType
            }
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de la détection du type de connexion")
            ConnectionType.NONE
        }
    }
    
    // ================================
    // SURVEILLANCE TEMPS RÉEL
    // ================================
    
    /**
     * Flow réactif pour surveiller les changements de connectivité en temps réel
     * 
     * Cette méthode applique le pattern Observer en utilisant les Flow Kotlin
     * pour créer un stream réactif des changements d'état réseau
     * 
     * Émissions du Flow :
     * - À chaque changement de connexion/déconnexion
     * - À chaque changement de type de réseau
     * - Valeur initiale lors de la souscription
     * 
     * @return Flow émettant l'état de connectivité actuel
     */
    fun observeConnectivityState(): Flow<ConnectivityState> = callbackFlow {
        Timber.d("🔄 Démarrage de la surveillance de connectivité")
        
        // Émission de l'état initial
        val initialState = getCurrentConnectivityState()
        trySend(initialState)
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Android 7+ : utilise NetworkCallback (API moderne)
            val networkCallback = object : SystemConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Timber.d("🟢 Réseau disponible: $network")
                    val state = getCurrentConnectivityState()
                    trySend(state)
                }
                
                override fun onLost(network: Network) {
                    Timber.d("🔴 Réseau perdu: $network")
                    val state = getCurrentConnectivityState()
                    trySend(state)
                }
                
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    Timber.d("🔄 Capacités réseau changées: $network")
                    val state = getCurrentConnectivityState()
                    trySend(state)
                }
            }
            
            // Enregistrement du callback avec NetworkRequest pour tous les réseaux
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            
            systemConnectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            
            // Nettoyage automatique lors de la fermeture du Flow
            awaitClose {
                Timber.d("🛑 Arrêt de la surveillance de connectivité")
                systemConnectivityManager.unregisterNetworkCallback(networkCallback)
            }
        } else {
            // Android < 7 : pas de surveillance temps réel native
            // Le Flow n'émettra que la valeur initiale
            Timber.w("⚠️ Surveillance temps réel non disponible sur Android < 7")
            awaitClose {
                Timber.d("🛑 Arrêt de la surveillance de connectivité (legacy)")
            }
        }
    }.distinctUntilChanged() // Évite les émissions dupliquées
    
    /**
     * Obtient l'état de connectivité actuel
     * 
     * @return État complet de la connectivité
     */
    private fun getCurrentConnectivityState(): ConnectivityState {
        val isConnected = isConnectedToInternet()
        val connectionType = getConnectionType()
        val quality = getConnectionQuality()
        
        return ConnectivityState(
            isConnected = isConnected,
            connectionType = connectionType,
            quality = quality
        )
    }
    
    // ================================
    // ANALYSE DE LA QUALITÉ DE CONNEXION
    // ================================
    
    /**
     * Évalue la qualité de la connexion réseau
     * 
     * Cette méthode analyse les capacités réseau pour déterminer
     * si la connexion est adaptée aux appels API intensifs
     * 
     * @return Qualité de la connexion
     */
    fun getConnectionQuality(): ConnectionQuality {
        return try {
            if (!isConnectedToInternet()) {
                return ConnectionQuality.NONE
            }
            
            val connectionType = getConnectionType()
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = systemConnectivityManager.activeNetwork
                val networkCapabilities = systemConnectivityManager.getNetworkCapabilities(activeNetwork)
                
                val quality = networkCapabilities?.let { capabilities ->
                    when {
                        // WiFi généralement de bonne qualité
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                // Android 10+ : peut analyser la force du signal WiFi
                                ConnectionQuality.HIGH
                            } else {
                                ConnectionQuality.HIGH
                            }
                        }
                        
                        // Ethernet généralement excellente qualité
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> ConnectionQuality.HIGH
                        
                        // Mobile : qualité variable selon le type
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                            // Heuristique basée sur les capacités
                            when {
                                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) -> ConnectionQuality.HIGH
                                else -> ConnectionQuality.MEDIUM
                            }
                        }
                        
                        else -> ConnectionQuality.LOW
                    }
                } ?: ConnectionQuality.LOW
                
                Timber.d("📊 Qualité de connexion: $quality pour $connectionType")
                quality
            } else {
                // Android < 6 : heuristique simple basée sur le type
                val quality = when (connectionType) {
                    ConnectionType.WIFI, ConnectionType.ETHERNET -> ConnectionQuality.HIGH
                    ConnectionType.MOBILE -> ConnectionQuality.MEDIUM
                    ConnectionType.VPN, ConnectionType.OTHER -> ConnectionQuality.LOW
                    ConnectionType.NONE -> ConnectionQuality.NONE
                }
                
                Timber.d("📊 Qualité de connexion (legacy): $quality pour $connectionType")
                quality
            }
        } catch (exception: Exception) {
            Timber.e(exception, "❌ Erreur lors de l'évaluation de la qualité de connexion")
            ConnectionQuality.LOW
        }
    }
    
    // ================================
    // MÉTHODES UTILITAIRES
    // ================================
    
    /**
     * Vérifie si la connexion est adaptée aux appels API intensifs
     * 
     * @return true si la connexion permet des appels API fluides
     */
    fun isConnectionSuitableForApi(): Boolean {
        val quality = getConnectionQuality()
        val suitable = quality in listOf(ConnectionQuality.HIGH, ConnectionQuality.MEDIUM)
        
        Timber.d("🚀 Connexion adaptée pour API: $suitable (qualité: $quality)")
        return suitable
    }
    
    /**
     * Retourne des informations détaillées sur la connectivité
     * 
     * @return Map contenant toutes les informations réseau
     */
    fun getDetailedNetworkInfo(): Map<String, Any> {
        val state = getCurrentConnectivityState()
        
        return mapOf(
            "is_connected" to state.isConnected,
            "connection_type" to state.connectionType.name,
            "quality" to state.quality.name,
            "suitable_for_api" to isConnectionSuitableForApi(),
            "android_version" to Build.VERSION.SDK_INT
        )
    }
    
    /**
     * Log l'état complet de la connectivité
     * 
     * Méthode de debugging pour diagnostiquer les problèmes réseau
     */
    fun logNetworkState() {
        val info = getDetailedNetworkInfo()
        Timber.i("🌐 État réseau du device: $info")
    }
}

/**
 * Modèle représentant l'état complet de la connectivité
 * 
 * @property isConnected Indique si une connexion Internet est active
 * @property connectionType Type de connexion réseau
 * @property quality Qualité estimée de la connexion
 */
data class ConnectivityState(
    val isConnected: Boolean,
    val connectionType: ConnectionType,
    val quality: ConnectionQuality
) {
    /**
     * Indique si l'état permet des opérations réseau
     */
    fun canPerformNetworkOperations(): Boolean = isConnected && quality != ConnectionQuality.NONE
    
    /**
     * Retourne une description textuelle de l'état
     */
    fun getDisplayText(): String = when {
        !isConnected -> "Aucune connexion"
        else -> "${connectionType.displayName} - ${quality.displayName}"
    }
}

/**
 * Énumération des types de connexion réseau
 */
enum class ConnectionType(val displayName: String) {
    WIFI("WiFi"),
    MOBILE("Mobile"),
    ETHERNET("Ethernet"),
    VPN("VPN"),
    OTHER("Autre"),
    NONE("Aucune")
}

/**
 * Énumération des qualités de connexion
 */
enum class ConnectionQuality(val displayName: String) {
    HIGH("Excellente"),
    MEDIUM("Bonne"),
    LOW("Faible"),
    NONE("Aucune")
} 