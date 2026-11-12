# SHS · squelette (J1–J5)

## Lancer le backend et les services
    docker compose up --build
- API : http://localhost:8080/api/v1/actuator/health
- Intelligence (simulée) : http://localhost:8000/health

## Android
Ouvrir `android/` dans Android Studio. **Avant la première synchro** : ajouter les 4 fichiers TTF de
Plus Jakarta Sans dans `app/src/main/res/font/` (voir `plus_jakarta_sans.xml`), sinon la compilation échoue.
Versions des dépendances à vérifier à la synchro Gradle.
Contrat d'API : `docs/openapi.yaml`. Diagrammes : `docs/*.mmd`.

## Démarrer le travail d'équipe
Lire `docs/GUIDE_PROJET.md` (prérequis, mise en route, conventions, planning). Les tâches : `docs/ISSUES.md` et `scripts/create_issues.sh`.
