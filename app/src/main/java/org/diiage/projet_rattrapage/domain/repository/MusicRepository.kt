package org.diiage.projet_rattrapage.domain.repository

import org.diiage.projet_rattrapage.domain.model.Artist
import org.diiage.projet_rattrapage.domain.model.Album
import org.diiage.projet_rattrapage.domain.model.Track

/**
 * Interface du repository pour l'accès aux données musicales
 * 
 * Cette interface applique le principe d'inversion de dépendance (SOLID) :
 * - La couche Domain définit le contrat
 * - La couche Data implémente le contrat  
 * - Permet de tester facilement avec des mocks
 * - Découple l'accès aux données de leur source
 * 
 * Pattern Repository appliqué pour :
 * - Centraliser l'accès aux données
 * - Abstraire la source de données (API, base locale, cache)
 * - Faciliter les tests unitaires
 * - Respecter la séparation des responsabilités
 * 

 * @since 1.0
 */
interface MusicRepository {
    
    /**
     * Recherche des artistes selon une requête
     * 
     * Cette méthode utilise le type Result pour une gestion d'erreur fonctionnelle :
     * - Success : contient la liste des artistes trouvés
     * - Failure : contient l'exception en cas d'erreur
     * 
     * @param query Terme de recherche pour les artistes
     * @param limit Nombre maximum de résultats
     * @return Result contenant la liste des artistes ou une erreur
     */
    suspend fun searchArtists(query: String, limit: Int = 25): Result<List<Artist>>
    
    /**
     * Recherche des albums selon une requête
     * 
     * @param query Terme de recherche pour les albums
     * @param limit Nombre maximum de résultats
     * @return Result contenant la liste des albums ou une erreur
     */
    suspend fun searchAlbums(query: String, limit: Int = 25): Result<List<Album>>
    
    /**
     * Recherche des pistes selon une requête
     * 
     * @param query Terme de recherche pour les pistes
     * @param limit Nombre maximum de résultats
     * @return Result contenant la liste des pistes ou une erreur
     */
    suspend fun searchTracks(query: String, limit: Int = 25): Result<List<Track>>
    
    /**
     * Récupère les détails d'un artiste par son identifiant
     * 
     * @param artistId Identifiant de l'artiste
     * @return Result contenant l'artiste ou une erreur
     */
    suspend fun getArtistById(artistId: Long): Result<Artist>
    
    /**
     * Récupère les albums d'un artiste
     * 
     * @param artistId Identifiant de l'artiste
     * @return Result contenant la liste des albums ou une erreur
     */
    suspend fun getArtistAlbums(artistId: Long): Result<List<Album>>
    
    /**
     * Récupère les pistes principales d'un artiste
     * 
     * @param artistId Identifiant de l'artiste
     * @return Result contenant la liste des pistes ou une erreur
     */
    suspend fun getArtistTopTracks(artistId: Long): Result<List<Track>>
    
    /**
     * Récupère les détails d'un album par son identifiant
     * 
     * @param albumId Identifiant de l'album
     * @return Result contenant l'album ou une erreur
     */
    suspend fun getAlbumById(albumId: Long): Result<Album>
    
    /**
     * Récupère les pistes d'un album
     * 
     * @param albumId Identifiant de l'album
     * @return Result contenant la liste des pistes ou une erreur
     */
    suspend fun getAlbumTracks(albumId: Long): Result<List<Track>>
    
    /**
     * Récupère les détails d'une piste par son identifiant
     * 
     * @param trackId Identifiant de la piste
     * @return Result contenant la piste ou une erreur
     */
    suspend fun getTrackById(trackId: Long): Result<Track>
} 