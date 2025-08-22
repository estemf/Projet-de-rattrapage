package org.diiage.projet_rattrapage.data.api

import org.diiage.projet_rattrapage.data.dto.DeezerApiResponse
import org.diiage.projet_rattrapage.data.dto.ArtistDto
import org.diiage.projet_rattrapage.data.dto.AlbumDto
import org.diiage.projet_rattrapage.data.dto.TrackDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface de service pour l'API Deezer
 * 
 * Cette interface définit tous les endpoints disponibles de l'API Deezer
 * en appliquant les principes SOLID :
 * - Interface Segregation : méthodes spécialisées par type de données
 * - Single Responsibility : chaque méthode a un rôle précis
 * - Dependency Inversion : abstraction de l'accès réseau
 * 
 * Documentation API Deezer : https://developers.deezer.com/api
 * 
 * Toutes les méthodes retournent un Response<T> pour permettre :
 * - La gestion fine des codes de statut HTTP
 * - L'accès aux headers de réponse
 * - La gestion appropriée des erreurs réseau
 * 

 * @since 1.0
 */
interface DeezerApiService {
    
    // ================================
    // ENDPOINTS DE RECHERCHE
    // ================================
    
    /**
     * Recherche d'artistes par terme de recherche
     * 
     * Endpoint : GET /search/artist
     * 
     * @param query Terme de recherche
     * @param limit Nombre maximum de résultats (défaut: 25, max: 100)
     * @param index Index de départ pour la pagination (défaut: 0)
     * @return Response contenant la liste des artistes trouvés
     */
    @GET("search/artist")
    suspend fun searchArtists(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("index") index: Int = 0
    ): Response<DeezerApiResponse<ArtistDto>>
    
    /**
     * Recherche d'albums par terme de recherche
     * 
     * Endpoint : GET /search/album
     * 
     * @param query Terme de recherche
     * @param limit Nombre maximum de résultats
     * @param index Index de départ pour la pagination
     * @return Response contenant la liste des albums trouvés
     */
    @GET("search/album")
    suspend fun searchAlbums(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("index") index: Int = 0
    ): Response<DeezerApiResponse<AlbumDto>>
    
    /**
     * Recherche de pistes par terme de recherche
     * 
     * Endpoint : GET /search/track
     * 
     * @param query Terme de recherche
     * @param limit Nombre maximum de résultats
     * @param index Index de départ pour la pagination
     * @return Response contenant la liste des pistes trouvées
     */
    @GET("search/track")
    suspend fun searchTracks(
        @Query("q") query: String,
        @Query("limit") limit: Int = 25,
        @Query("index") index: Int = 0
    ): Response<DeezerApiResponse<TrackDto>>
    
    // ================================
    // ENDPOINTS D'ARTISTES
    // ================================
    
    /**
     * Récupère les détails d'un artiste par son ID
     * 
     * Endpoint : GET /artist/{id}
     * 
     * @param artistId Identifiant de l'artiste
     * @return Response contenant les détails de l'artiste
     */
    @GET("artist/{id}")
    suspend fun getArtistById(
        @Path("id") artistId: Long
    ): Response<ArtistDto>
    
    /**
     * Récupère les albums d'un artiste
     * 
     * Endpoint : GET /artist/{id}/albums
     * 
     * @param artistId Identifiant de l'artiste
     * @param limit Nombre maximum d'albums à récupérer
     * @param index Index de départ pour la pagination
     * @return Response contenant la liste des albums
     */
    @GET("artist/{id}/albums")
    suspend fun getArtistAlbums(
        @Path("id") artistId: Long,
        @Query("limit") limit: Int = 50,
        @Query("index") index: Int = 0
    ): Response<DeezerApiResponse<AlbumDto>>
    
    /**
     * Récupère les pistes les plus populaires d'un artiste
     * 
     * Endpoint : GET /artist/{id}/top
     * 
     * @param artistId Identifiant de l'artiste
     * @param limit Nombre maximum de pistes à récupérer
     * @param index Index de départ pour la pagination
     * @return Response contenant la liste des pistes populaires
     */
    @GET("artist/{id}/top")
    suspend fun getArtistTopTracks(
        @Path("id") artistId: Long,
        @Query("limit") limit: Int = 50,
        @Query("index") index: Int = 0
    ): Response<DeezerApiResponse<TrackDto>>
    
    // ================================
    // ENDPOINTS D'ALBUMS
    // ================================
    
    /**
     * Récupère les détails d'un album par son ID
     * 
     * Endpoint : GET /album/{id}
     * 
     * @param albumId Identifiant de l'album
     * @return Response contenant les détails de l'album
     */
    @GET("album/{id}")
    suspend fun getAlbumById(
        @Path("id") albumId: Long
    ): Response<AlbumDto>
    
    /**
     * Récupère les pistes d'un album
     * 
     * Endpoint : GET /album/{id}/tracks
     * 
     * @param albumId Identifiant de l'album
     * @param limit Nombre maximum de pistes à récupérer
     * @param index Index de départ pour la pagination
     * @return Response contenant la liste des pistes de l'album
     */
    @GET("album/{id}/tracks")
    suspend fun getAlbumTracks(
        @Path("id") albumId: Long,
        @Query("limit") limit: Int = 100,
        @Query("index") index: Int = 0
    ): Response<DeezerApiResponse<TrackDto>>
    
    // ================================
    // ENDPOINTS DE PISTES
    // ================================
    
    /**
     * Récupère les détails d'une piste par son ID
     * 
     * Endpoint : GET /track/{id}
     * 
     * @param trackId Identifiant de la piste
     * @return Response contenant les détails de la piste
     */
    @GET("track/{id}")
    suspend fun getTrackById(
        @Path("id") trackId: Long
    ): Response<TrackDto>
    
    companion object {
        /**
         * URL de base de l'API Deezer
         */
        const val BASE_URL = "https://api.deezer.com/"
        
        /**
         * Limites par défaut pour les différents types de contenu
         */
        const val DEFAULT_SEARCH_LIMIT = 25
        const val DEFAULT_ALBUMS_LIMIT = 50
        const val DEFAULT_TRACKS_LIMIT = 50
        const val MAX_SEARCH_LIMIT = 100
    }
} 