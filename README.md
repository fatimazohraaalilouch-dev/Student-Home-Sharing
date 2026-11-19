# SHS · squelette (J1–J5)

## Lancer le backend et les services
    docker compose up --build
- API : http://localhost:8080/api/v1/actuator/health
- Intelligence (simulée) : http://localhost:8000/health

## Migrations de base de données (Flyway)

Les migrations SQL sont stockées dans `backend/src/main/resources/db/migration/`.

### Répartition des versions

- `V1` : extensions PostgreSQL/PostGIS.
- `V2` : schéma initial de la base de données.
- `V3` à `V9` : migrations communes aux trois lots.
- `V10` à `V19` : Lot A.
- `V20` à `V29` : Lot B.
- `V30` à `V39` : Lot C.

### Règles à respecter

- Nommer les fichiers selon le format `V<version>__<description>.sql` (deux underscores).
- Utiliser la prochaine version libre dans la plage attribuée.
- Les modifications communes aux trois lots doivent utiliser la plage commune (`V3` à `V9`).
- Ne jamais modifier une migration déjà appliquée : créer une nouvelle migration pour toute évolution.

## Android
Ouvrir `android/` dans Android Studio. **Avant la première synchro** : ajouter les 4 fichiers TTF de
Plus Jakarta Sans dans `app/src/main/res/font/` (voir `plus_jakarta_sans.xml`), sinon la compilation échoue.
Versions des dépendances à vérifier à la synchro Gradle.
Contrat d'API : `docs/openapi.yaml`. Diagrammes : `docs/*.mmd`.

## Démarrer le travail d'équipe
Lire `docs/GUIDE_PROJET.md` (prérequis, mise en route, conventions, planning). Les tâches : `docs/ISSUES.md` et `scripts/create_issues.sh`.
