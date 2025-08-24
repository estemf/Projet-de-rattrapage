package org.diiage.projet_rattrapage.data.repository

import org.diiage.projet_rattrapage.data.api.DeezerApiService
import org.diiage.projet_rattrapage.data.dto.toDomainModels
import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.model.Track
import org.diiage.projet_rattrapage.domain.repository.MusicRepository
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection


/**
 * Implémentation du repository pour l'accès aux données musicales via l'API Deezer
 * 
 * Cette classe applique plusieurs design patterns :
 * - Repository Pattern : encapsule l'accès aux données
 * - Adapter Pattern : adapte l'API externe vers notre interface
 * - Strategy Pattern : différentes stratégies de gestion d'erreur
 * 
 * Principes SOLID respectés :
 * - Single Responsibility : gère uniquement l'accès aux données musicales
 * - Dependency Inversion : implémente l'interface définie par le domaine
 * - Interface Segregation : méthodes spécialisées par type de données
 * 
 * @property apiService Service d'accès à l'API Deezer
 * 

 * @since 1.0
 */
class MusicRepositoryImpl(
    private val apiService: DeezerApiService
) : MusicRepository {
    
    // ================================
    // IMPLÉMENTATION DES MÉTHODES DE RECHERCHE
    // ================================
    
    /**
     * {@inheritDoc}
     * 
     * Implémentation avec :
     * - Gestion complète des erreurs HTTP
     * - Logging détaillé pour le debugging
     * - Transformation automatique DTO -> Domain
     * - Validation des données reçues
     */
    override suspend fun searchArtists(query: String, limit: Int): Result<List<Artist>> {
        Timber.d("🔍 Recherche d'artistes pour la requête: '$query' (limit: $limit)")
        
        return try {
            val response = apiService.searchArtists(query, limit)
            
            Timber.d("📡 Réponse API reçue - Code: ${response.code()}")
            
            response.handleApiResponse(
                onSuccess = { apiResponse ->
                    val artists = apiResponse.data.toDomainModels()
                    Timber.i("✅ Recherche réussie: ${artists.size} artiste(s) trouvé(s)")
                    artists
                },
                operationName = "recherche d'artistes",
                query = query
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la recherche d'artistes")
            Result.failure(
                IOException("Erreur de connexion lors de la recherche d'artistes", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun searchAlbums(query: String, limit: Int): Result<List<Album>> {
        Timber.d("🔍 Recherche d'albums pour la requête: '$query' (limit: $limit)")
        
        return try {
            val response = apiService.searchAlbums(query, limit)
            
            Timber.d("📡 Réponse API reçue - Code: ${response.code()}")
            
            response.handleApiResponse(
                onSuccess = { apiResponse ->
                    val albums = apiResponse.data.toDomainModels()
                    Timber.i("✅ Recherche d'albums réussie: ${albums.size} album(s) trouvé(s)")
                    albums
                },
                operationName = "recherche d'albums",
                query = query
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la recherche d'albums")
            Result.failure(
                IOException("Erreur de connexion lors de la recherche d'albums", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun searchTracks(query: String, limit: Int): Result<List<Track>> {
        Timber.d("🔍 Recherche de pistes pour la requête: '$query' (limit: $limit)")
        
        return try {
            val response = apiService.searchTracks(query, limit)
            
            Timber.d("📡 Réponse API reçue - Code: ${response.code()}")
            
            response.handleApiResponse(
                onSuccess = { apiResponse ->
                    val tracks = apiResponse.data.toDomainModels()
                    Timber.i("✅ Recherche de pistes réussie: ${tracks.size} piste(s) trouvée(s)")
                    tracks
                },
                operationName = "recherche de pistes",
                query = query
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la recherche de pistes")
            Result.failure(
                IOException("Erreur de connexion lors de la recherche de pistes", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun getArtistById(artistId: Long): Result<Artist> {
        Timber.d("🎤 Récupération de l'artiste ID: $artistId")
        
        return try {
            val response = apiService.getArtistById(artistId)
            
            response.handleSingleItemResponse(
                onSuccess = { artistDto ->
                    val artist = artistDto.toDomainModel()
                    Timber.i("✅ Artiste récupéré: ${artist.name}")
                    artist
                },
                operationName = "récupération d'artiste",
                itemId = artistId.toString()
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la récupération de l'artiste $artistId")
            Result.failure(
                IOException("Erreur de connexion lors de la récupération de l'artiste", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun getArtistAlbums(artistId: Long): Result<List<Album>> {
        Timber.d("💿 Récupération des albums de l'artiste ID: $artistId")
        
        return try {
            val response = apiService.getArtistAlbums(artistId)
            
            response.handleApiResponse(
                onSuccess = { apiResponse ->
                    val albums = apiResponse.data.toDomainModels()
                    Timber.i("✅ Albums récupérés: ${albums.size} album(s)")
                    albums
                },
                operationName = "récupération d'albums",
                query = "artiste $artistId"
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la récupération des albums")
            Result.failure(
                IOException("Erreur de connexion lors de la récupération des albums", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun getArtistTopTracks(artistId: Long): Result<List<Track>> {
        Timber.d("🎵 Récupération des pistes populaires de l'artiste ID: $artistId")
        
        return try {
            val response = apiService.getArtistTopTracks(artistId)
            
            response.handleApiResponse(
                onSuccess = { apiResponse ->
                    val tracks = apiResponse.data.toDomainModels()
                    Timber.i("✅ Pistes populaires récupérées: ${tracks.size} piste(s)")
                    tracks
                },
                operationName = "récupération de pistes populaires",
                query = "artiste $artistId"
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la récupération des pistes populaires")
            Result.failure(
                IOException("Erreur de connexion lors de la récupération des pistes", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun getAlbumById(albumId: Long): Result<Album> {
        Timber.d("💿 Récupération de l'album ID: $albumId")
        
        return try {
            val response = apiService.getAlbumById(albumId)
            
            response.handleSingleItemResponse(
                onSuccess = { albumDto ->
                    val album = albumDto.toDomainModel()
                    Timber.i("✅ Album récupéré: ${album.title}")
                    album
                },
                operationName = "récupération d'album",
                itemId = albumId.toString()
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la récupération de l'album $albumId")
            Result.failure(
                IOException("Erreur de connexion lors de la récupération de l'album", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun getAlbumTracks(albumId: Long): Result<List<Track>> {
        Timber.d("🎵 Récupération des pistes de l'album ID: $albumId")
        
        return try {
            val response = apiService.getAlbumTracks(albumId)
            
            response.handleApiResponse(
                onSuccess = { apiResponse ->
                    val tracks = apiResponse.data.toDomainModels()
                    Timber.i("✅ Pistes de l'album récupérées: ${tracks.size} piste(s)")
                    tracks
                },
                operationName = "récupération de pistes d'album",
                query = "album $albumId"
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la récupération des pistes de l'album")
            Result.failure(
                IOException("Erreur de connexion lors de la récupération des pistes", exception)
            )
        }
    }
    
    /**
     * {@inheritDoc}
     */
    override suspend fun getTrackById(trackId: Long): Result<Track> {
        Timber.d("🎵 Récupération de la piste ID: $trackId")
        
        return try {
            val response = apiService.getTrackById(trackId)
            
            response.handleSingleItemResponse(
                onSuccess = { trackDto ->
                    val track = trackDto.toDomainModel()
                    Timber.i("✅ Piste récupérée: ${track.title}")
                    track
                },
                operationName = "récupération de piste",
                itemId = trackId.toString()
            )
            
        } catch (exception: Exception) {
            Timber.e(exception, "💥 Exception lors de la récupération de la piste $trackId")
            Result.failure(
                IOException("Erreur de connexion lors de la récupération de la piste", exception)
            )
        }
    }
}

// ================================
// FONCTIONS D'EXTENSION POUR LA GESTION DES RÉPONSES
// ================================

/**
 * Extension pour gérer les réponses API de type liste (DeezerApiResponse<T>)
 * 
 * Cette fonction applique le principe DRY en centralisant
 * la gestion des codes de statut HTTP et des erreurs
 * 
 * @param T Type des éléments dans la réponse
 * @param R Type de retour après transformation
 * @param onSuccess Fonction de transformation en cas de succès
 * @param operationName Nom de l'opération pour les logs
 * @param query Requête associée pour les logs
 * @return Result contenant les données transformées ou une erreur
 */
private inline fun <T, R> Response<org.diiage.projet_rattrapage.data.dto.DeezerApiResponse<T>>.handleApiResponse(
    onSuccess: (org.diiage.projet_rattrapage.data.dto.DeezerApiResponse<T>) -> R,
    operationName: String,
    query: String
): Result<R> {
    return when {
        isSuccessful -> {
            val body = body()
            if (body != null) {
                Timber.d("📦 Données reçues: ${body.data.size} élément(s)")
                Result.success(onSuccess(body))
            } else {
                Timber.w("⚠️ Réponse vide pour $operationName")
                Result.failure(
                    IOException("Aucune donnée reçue pour $operationName")
                )
            }
        }
        
        code() == HttpURLConnection.HTTP_NOT_FOUND -> {
            Timber.w("🔍 Aucun résultat trouvé pour $operationName: '$query'")
            Result.failure(
                NoSuchElementException("Aucun résultat trouvé pour '$query'")
            )
        }
        
        code() == HttpURLConnection.HTTP_BAD_REQUEST -> {
            Timber.w("❌ Requête invalide pour $operationName: '$query'")
            Result.failure(
                IllegalArgumentException("Requête invalide pour $operationName")
            )
        }
        
        code() >= HttpURLConnection.HTTP_INTERNAL_ERROR -> {
            Timber.e("🔥 Erreur serveur (${code()}) pour $operationName")
            Result.failure(
                IOException("Erreur serveur (${code()}) pour $operationName")
            )
        }
        
        else -> {
            Timber.e("⚠️ Erreur HTTP ${code()} pour $operationName")
            Result.failure(
                IOException("Erreur HTTP ${code()} pour $operationName")
            )
        }
    }
}

/**
 * Extension pour gérer les réponses API d'élément unique
 * 
 * @param T Type de l'élément dans la réponse
 * @param R Type de retour après transformation
 * @param onSuccess Fonction de transformation en cas de succès
 * @param operationName Nom de l'opération pour les logs
 * @param itemId Identifiant de l'élément pour les logs
 * @return Result contenant l'élément transformé ou une erreur
 */
private inline fun <T, R> Response<T>.handleSingleItemResponse(
    onSuccess: (T) -> R,
    operationName: String,
    itemId: String
): Result<R> {
    return when {
        isSuccessful -> {
            val body = body()
            if (body != null) {
                Timber.d("📦 Élément reçu pour ID: $itemId")
                Result.success(onSuccess(body))
            } else {
                Timber.w("⚠️ Réponse vide pour $operationName ID: $itemId")
                Result.failure(
                    IOException("Aucune donnée reçue pour $operationName")
                )
            }
        }
        
        code() == HttpURLConnection.HTTP_NOT_FOUND -> {
            Timber.w("🔍 Élément non trouvé pour $operationName ID: $itemId")
            Result.failure(
                NoSuchElementException("Élément non trouvé pour ID: $itemId")
            )
        }
        
        else -> {
            Timber.e("⚠️ Erreur HTTP ${code()} pour $operationName ID: $itemId")
            Result.failure(
                IOException("Erreur HTTP ${code()} pour $operationName")
            )
        }
    }
} 