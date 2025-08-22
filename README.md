# 🚀 Guide de Configuration - Projet Android Deezer Music

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- **Android Studio** (version 2023.1.1 ou plus récente)
- **Java Development Kit (JDK)** version 17 ou plus récente
- **Git** pour cloner le projet

## 🎯 Étapes de Configuration

### **1. Cloner le projet**

```bash
git clone [URL_DU_REPO_GITHUB]
cd Projet_Rattrapage
```

### **2. Configuration du SDK Android**

#### **Option A : Configuration automatique (recommandée)**
1. Ouvrez le projet dans **Android Studio**
2. Android Studio détectera automatiquement le SDK manquant
3. Cliquez sur **"Sync Project with Gradle Files"** dans la barre d'outils
4. Suivez les instructions pour télécharger le SDK

#### **Option B : Configuration manuelle**
1. Créez un fichier `local.properties` à la racine du projet
2. Ajoutez le chemin vers votre SDK Android :

```properties
# Windows
sdk.dir=C\:\\Users\\VotreNom\\AppData\\Local\\Android\\Sdk

# macOS
sdk.dir=/Users/VotreNom/Library/Android/sdk

# Linux
sdk.dir=/home/VotreNom/Android/Sdk
```

**💡 Comment trouver le chemin SDK :**
- Dans Android Studio : **File → Project Structure → SDK Location**
- Ou dans **File → Settings → Appearance & Behavior → System Settings → Android SDK**

### **3. Vérification des dépendances**

Le projet utilise **Gradle** avec un fichier `libs.versions.toml` centralisé. Vérifiez que :

1. **Gradle** se synchronise correctement
2. Toutes les **dépendances** sont téléchargées
3. Aucune **erreur de compilation** n'apparaît

### **4. Configuration de l'API Deezer**

Le projet utilise l'API Deezer publique (pas de clé API requise), mais vous pouvez personnaliser :

1. Ouvrez `app/src/main/java/org/diiage/projet_rattrapage/data/api/DeezerApiService.kt`
2. Vérifiez que l'URL de base est correcte : `https://api.deezer.com/`

## 🏃‍♂️ Lancement du projet

### **1. Ouvrir dans Android Studio**
```bash
# Depuis Android Studio
File → Open → Sélectionnez le dossier Projet_Rattrapage
```

### **2. Synchronisation Gradle**
- Cliquez sur **"Sync Project with Gradle Files"** (icône éléphant)
- Attendez la fin de la synchronisation

### **3. Configuration de l'émulateur**
1. **Tools → Device Manager**
2. Cliquez sur **"Create Device"**
3. Sélectionnez un appareil (ex: Pixel 5)
4. Choisissez une image système Android (API 34 recommandée)
5. Finalisez la création

### **4. Lancement de l'application**
1. Sélectionnez votre émulateur dans la barre d'outils
2. Cliquez sur le bouton **"Run"** (▶️ vert)
3. L'application se compile et se lance automatiquement

## 🔧 Résolution des problèmes courants

### **Problème : "SDK not found"**
```bash
# Solution : Créer local.properties avec le bon chemin
# Vérifiez le chemin dans Android Studio
```

### **Problème : "Gradle sync failed"**
```bash
# Solutions :
# 1. File → Invalidate Caches and Restart
# 2. Vérifiez votre connexion internet
# 3. Vérifiez la version de Java (JDK 17+)
```

### **Problème : "Build failed"**
```bash
# Solutions :
# 1. Clean Project (Build → Clean Project)
# 2. Rebuild Project (Build → Rebuild Project)
# 3. Vérifiez les logs d'erreur dans la console
```

### **Problème : "Emulator not starting"**
```bash
# Solutions :
# 1. Vérifiez que la virtualisation est activée dans le BIOS
# 2. Redémarrez Android Studio
# 3. Créez un nouvel émulateur
```

## 📱 Test de l'application

Une fois lancée, l'application devrait afficher :

1. **Écran de recherche** avec un champ de texte
2. **Possibilité de rechercher des artistes, albums et pistes** via l'API Deezer
3. **Affichage des résultats** avec navigation et détails
4. **Gestion des états** (chargement, erreur, succès)

## 🎯 Fonctionnalités à tester

- ✅ Recherche d'artistes, albums et pistes
- ✅ Affichage des résultats ainsi qu'une page détails
- ✅ Light et Dark mode
- ✅ Feedback haptique (vibration)

## 📚 Ressources utiles

- **Documentation Android** : [developer.android.com](https://developer.android.com)
- **Jetpack Compose** : [developer.android.com/jetpack/compose](https://developer.android.com/jetpack/compose)
- **API Deezer** : [developers.deezer.com](https://developers.deezer.com)

