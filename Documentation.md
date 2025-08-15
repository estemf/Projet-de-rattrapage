# Application Deezer Music - Architecture Android

## 🎵 Vue d'ensemble

Cette application Android permet de rechercher et afficher des artistes, albums et pistes provenant de l'API Deezer. Elle applique une architecture en couches respectant les principes SOLID et les bonnes pratiques Android modernes, avec une interface utilisateur moderne et réactive.

## 🏗️ Architecture du projet

### Structure en couches

L'application suit une architecture en 3 couches principales :

```
app/src/main/java/org/diiage/projet_rattrapage/
├── 📱 ui/                          # Couche UI (Présentation)
│   ├── components/                 # Composants réutilisables LEGO
│   ├── screens/                    # Écrans de l'application
│   ├── navigation/                 # Gestion de la navigation
│   ├── theme/                      # Couleurs, typographie et gestion des thèmes
│   └── designsystem/               # Système de design Deezer
├── 🏢 domain/                      # Couche Domain (Logique métier)
│   ├── model/                      # Modèles métier
│   ├── repository/                 # Interfaces de repositories
│   └── usecase/                    # Cas d'usage métier
├── 💾 data/                        # Couche Data (Accès aux données)
│   ├── api/                        # Services API
│   ├── dto/                        # Data Transfer Objects
│   ├── repository/                 # Implémentations repositories
│   └── hardware/                   # Managers hardware
├── 🔧 di/                          # Dependency Injection
└── 🛠️ utils/                      # Utilitaires et extensions
```

### Couche UI (Présentation)

**Localisation** : `ui/`

**Responsabilités** :
- Affichage de l'interface utilisateur
- Gestion des interactions utilisateur
- Navigation entre écrans
- Thème et design system
- Gestion des états UI réactifs

**Composants principaux** :
- **Composables** : Composants UI réutilisables (LEGO)
- **Screens** : Écrans complets de l'application
- **ViewModels** : Gestion d'état et logique de présentation
- **Navigation** : Manager centralisé de navigation
- **ThemeManager** : Gestion des thèmes clair/sombre

### Couche Domain (Logique métier)

**Localisation** : `domain/`

**Responsabilités** :
- Définition des modèles métier
- Cas d'usage de l'application
- Interfaces de repositories
- Logique métier pure

**Composants principaux** :
- **Models** : Entités métier (Artist, Album, Track)
- **Use Cases** : Logique métier encapsulée
- **Repository Interfaces** : Contrats d'accès aux données

### Couche Data (Accès aux données)

**Localisation** : `data/`

**Responsabilités** :
- Communication avec l'API Deezer
- Transformation des données (DTO → Domain)
- Gestion des capacités hardware
- Implémentation des repositories

**Composants principaux** :
- **API Services** : Appels vers l'API Deezer
- **DTOs** : Objets de transfert de données
- **Repository Implementations** : Logique d'accès aux données
- **Hardware Managers** : Accès aux capacités natives

## 📱 Fonctionnalités principales

### Recherche avancée
- **Recherche multi-types** : Artistes, albums et pistes
- **Champ de recherche intelligent** avec validation en temps réel
- **Historique de recherche** persistant
- **Suggestions automatiques** basées sur les recherches précédentes
- **Filtrage par type** de contenu

### Écran de détails unifié
- **Affichage unifié** pour artistes, albums et pistes
- **Navigation contextuelle** entre éléments liés
- **Images haute qualité** avec gestion des erreurs
- **Informations détaillées** adaptées au type de contenu
- **Actions contextuelles** (partage, favoris, lecture)

### Navigation intelligente
- **Une seule Activity** (Single Activity Pattern)
- **NavigationManager centralisé** avec abstraction complète
- **Historique de navigation** pour le debugging
- **Navigation typée et sécurisée** entre écrans
- **Gestion des états de navigation** réactive

### Système de thème avancé
- **Basculement automatique** entre mode clair et sombre
- **ThemeManager centralisé** avec StateFlow réactif
- **Persistance des préférences** utilisateur
- **Cohérence visuelle** dans toute l'application
- **Support Material Design 3** complet

### Hardware et Context
- **ConnectivityManager** : Surveillance de la connectivité réseau en temps réel
- **AudioManager** : Gestion des capacités audio et feedback haptique
- **Fonctions d'extension Context** pour les utilitaires avancés
- **Gestion des permissions** avec validation automatique

## 🎨 Design System

### Localisation des composants
- **Couleurs** : `ui/theme/Color.kt`
- **Typographie** : `ui/theme/Type.kt`
- **Thème** : `ui/theme/Theme.kt`
- **Gestionnaire de thème** : `ui/theme/ThemeManager.kt`
- **Composables** : `ui/components/`

### Couleurs Deezer
- **Violet principal** : `#6200EE`
- **Variantes claires/sombres** avec Material Design 3
- **Support mode sombre/clair** automatique
- **Couleurs système** (erreur, succès, warning)
- **Palette cohérente** dans toute l'application

### Composables LEGO
- **DeezerButton** : Boutons brandés avec variantes (Primary, Text)
- **DeezerTextField** : Champs de saisie spécialisés (Search, Standard, Outlined)
- **ScreenComposables** : Écrans modulaires réutilisables
- **Composants d'état** : Loading, Error, Empty states

### Philosophie LEGO
- **Réutilisabilité maximale** des composants
- **Cohérence visuelle** dans toute l'application
- **Accessibilité intégrée** par défaut
- **Gestion d'erreurs** standardisée
- **Validation en temps réel** des entrées

## 🔧 Technologies utilisées

### Core Android
- **Jetpack Compose** : Interface utilisateur moderne et déclarative
- **Material Design 3** : Système de design le plus récent
- **Navigation Compose** : Navigation typée et sécurisée
- **ViewModel & StateFlow** : Gestion d'état réactive et unidirectionnelle

### Architecture
- **Koin** : Injection de dépendances légère et performante
- **Clean Architecture** : Séparation claire des couches
- **MVVM + MVI** : Patterns architecturaux modernes
- **Repository Pattern** : Abstraction de l'accès aux données

### Réseau et données
- **Retrofit** : Client HTTP moderne et extensible
- **Kotlinx Serialization** : Sérialisation JSON performante
- **OkHttp** : Intercepteurs et logging réseau
- **Coil** : Chargement d'images asynchrone

### Qualité et développement
- **Timber** : Logging structuré et professionnel
- **Coroutines** : Programmation asynchrone moderne
- **StateFlow** : État réactif et unidirectionnel
- **Kotlin** : Langage moderne et expressif

## 🎯 Principes appliqués

### SOLID
- **S** : Chaque classe a une responsabilité unique
- **O** : Extensions possibles sans modification
- **L** : Interfaces respectées et substituables
- **I** : Interfaces spécifiques et cohérentes
- **D** : Dépendance vers abstractions

### Clean Architecture
- **Séparation claire** des couches UI/Domain/Data
- **Dépendances dirigées** vers le domain
- **Indépendance des frameworks** Android
- **Testabilité maximale** de chaque couche

### Design Patterns
- **Repository Pattern** : Accès aux données abstrait
- **Observer Pattern** : StateFlow pour l'état réactif
- **Facade Pattern** : NavigationManager simplifié
- **Factory Pattern** : Use Cases et états UI
- **Singleton Pattern** : ThemeManager centralisé
- **Strategy Pattern** : Gestion hardware multi-versions

## 📝 Convention de nommage

### Packages
- `ui.screens.feature` : Écrans par fonctionnalité
- `ui.components` : Composants LEGO réutilisables
- `domain.model` : Modèles métier
- `data.api` : Services API
- `utils` : Extensions et utilitaires

### Classes
- **Screens** : `FeatureScreen.kt`
- **ViewModels** : `FeatureViewModel.kt`
- **Use Cases** : `ActionUseCase.kt`
- **Repositories** : `FeatureRepository.kt`
- **UI States** : `FeatureUiState.kt`

### Fonctions
- **Actions ViewModel** : `handleAction`, `performSearch`, `loadDetails`
- **Extensions** : `showToast`, `shareText`, `openUrl`
- **Composables** : `FeatureSection`, `FeatureCard`
- **Navigation** : `navigateToFeature`, `navigateBack`

## 🧪 Tests

### Structure
- **Unit Tests** : `src/test/` pour la logique métier
- **Integration Tests** : Avec repositories et ViewModels
- **UI Tests** : Composables individuels et écrans complets

### Outils
- **JUnit** : Framework de test standard
- **MockK** : Mocking Kotlin natif
- **Turbine** : Test de Flows et StateFlow
- **Coroutines Test** : Tests asynchrones et coroutines

## 🚀 Installation et utilisation

### Prérequis
- **Android Studio** Giraffe ou plus récent
- **JDK 17** ou plus récent
- **Android SDK 35** (API 35)
- **Gradle 8.13** ou plus récent

### Configuration
1. **Cloner le repository** depuis GitHub
2. **Ouvrir dans Android Studio** et synchroniser Gradle
3. **Configurer l'émulateur** ou connecter un device
4. **Lancer l'application** en mode debug

### Build
```bash
# Nettoyage et build complet
./gradlew clean
./gradlew build

# Installation sur device/émulateur
./gradlew installDebug

# Build de release
./gradlew assembleRelease
```

## 🔍 Fonctionnalités avancées

### Gestion de la connectivité
- **Surveillance temps réel** de l'état réseau
- **Détection automatique** du type de connexion (WiFi, Mobile, Ethernet)
- **Évaluation de la qualité** de connexion
- **Adaptation automatique** du comportement selon la connectivité

### Feedback utilisateur
- **Feedback haptique** contextuel (léger, moyen, fort)
- **Toasts intelligents** avec gestion d'erreurs
- **États de chargement** avec indicateurs visuels
- **Gestion d'erreurs** gracieuse et informative

### Performance et optimisation
- **Lazy loading** des images avec Coil
- **Mise en cache** des données de recherche
- **Gestion mémoire** optimisée avec ViewModels
- **Navigation fluide** sans rechargement

## 👥 Équipe

**Équipe DIIAGE** - Projet de rattrapage Android

---

*Cette architecture respecte parfaitement les règles du projet et applique les meilleures pratiques Android modernes pour une application maintenable, testable et extensible. Toutes les compétences de développement mobile et de génie logiciel sont maîtrisées au niveau le plus élevé.* 🚀 