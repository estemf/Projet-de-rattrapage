package org.diiage.projet_rattrapage

import android.app.Application
import org.diiage.projet_rattrapage.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Classe Application principale de l'application Deezer Music
 * 
 * Cette classe initialise :
 * - Koin pour l'injection de dépendances
 * - Timber pour le logging centralisé
 * - Configuration globale de l'application
 * 
 * @author Équipe DIIAGE
 * @since 1.0
 */
class DeezerMusicApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // ================================
        // INITIALISATION DU LOGGING
        // ================================
        
        if (BuildConfig.DEBUG) {
            // Configuration Timber pour le développement avec logs détaillés
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    // Format personnalisé : NomClasse.méthode:ligne
                    return "(${element.fileName}:${element.lineNumber})#${element.methodName}"
                }
            })
            
            Timber.d("🎵 Application Deezer Music initialisée en mode DEBUG")
        } else {
            // En production, logs uniquement pour les erreurs critiques
            Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    if (priority >= android.util.Log.ERROR) {
                        // Ici on pourrait intégrer Crashlytics ou autre
                        android.util.Log.println(priority, tag, message)
                    }
                }
            })
        }
        
        // ================================
        // INITIALISATION DE KOIN
        // ================================
        
        startKoin {
            androidLogger()
            androidContext(this@DeezerMusicApplication)
            modules(
                networkModule,
                repositoryModule,
                useCaseModule,
                hardwareModule,
                navigationModule,
                viewModelModule
            )
        }
        
        Timber.i("🚀 Application Deezer Music démarrée avec succès (Koin)")
    }
} 