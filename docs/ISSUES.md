# Issues du projet SHS (52 tâches)

Chaque issue est aussi créée automatiquement par `scripts/create_issues.sh`. Priorités : **p1** indispensable à la démo, **p2** important, **p3** à sacrifier en premier si le temps manque.


## Lot A · annonces et justificatif (propriétaire)

Total : 36.0 j-personne

| Code | Titre | Jalon | Prio | Estim. | Dépend de |
|---|---|---|---|---|---|
| S1-01 | Dépôt GitHub : créer, protéger main, pousser le squelette, CI verte | S1 | p1 | 0.5 j | — |
| S1-05 | Backend : authentification JWT (register, login, refresh, logout, password-reset) | S1 | p1 | 2.5 j | S1-03 |
| S1-09 | Android XML : Splash, Découverte 1–3, Connexion et ses états | S1 | p1 | 2.5 j | S1-08 |
| S1-12 | Android : connexion et inscription branchées à l'API, gestion de session | S1 | p1 | 1.5 j | S1-05, S1-09 |
| S2-A1 | Backend : annonces propriétaire (brouillon, CRUD, étapes de publication) | S2 | p1 | 3 j | S1-05 |
| S2-A2 | Backend : photos (upload, ordre, couverture, empreinte phash), soumission | S2 | p1 | 2.5 j | S2-A1 |
| S2-A3 | Android XML : Publication 1–5 et Récapitulatif | S2 | p1 | 3 j | S1-08 |
| S2-A4 | Android : Mes annonces (liste, vide) et publication branchée à l'API | S2 | p1 | 3 j | S2-A1, S2-A2, S2-A3 |
| S2-D1 | Décision : durée de conservation des justificatifs et seuil de suspension automatique | S2 | p2 | 0.5 j | — |
| S2-D3 | Décision : couleurs des badges en mode sombre et ombre 0/6/20 | S2 | p3 | 0.5 j | — |
| S3-A1 | Service intelligence : vérification du justificatif (OCR, extraction, cohérence) | S3 | p2 | 3 j | S2-A2 |
| S3-A2 | Backend : dépôt du justificatif (chiffrement, hash, statut) et publication | S3 | p2 | 3 j | S3-A1, S2-D1 |
| S3-A3 | Service intelligence + backend : indice de risque (TF-IDF, phash, prix, règles) | S3 | p2 | 3.5 j | S2-A2 |
| S3-A4 | Android XML + câblage : justificatif (5 états) et gestion d'annonce (publiée/en attente/suspendue/modif/suppression) | S3 | p2 | 4 j | S3-A2, S2-A4 |
| S4-03 | Tests : parcours propriétaire de bout en bout | S4 | p1 | 1.5 j | — |
| S4-06 | Build release signé, visuels Play Store, README final | S4 | p2 | 1.5 j | — |

## Lot B · recherche et classement (locataire)

Total : 33.0 j-personne

| Code | Titre | Jalon | Prio | Estim. | Dépend de |
|---|---|---|---|---|---|
| S1-02 | Android : synchro Gradle, build debug, lancement sur émulateur | S1 | p1 | 0.5 j | — |
| S1-06 | Backend : /me, espaces, préférences de notification, établissements, profil locataire | S1 | p1 | 2 j | S1-05 |
| S1-10 | Android XML : Inscription (2), Préférences Rôle/Langue (2), Profil locataire 1–3 (3) | S1 | p1 | 3 j | S1-08 |
| S1-13 | Android : profil de recherche branché à l'API, changement de langue et RTL | S1 | p1 | 1.5 j | S1-06, S1-10 |
| S1-15 | Décision : confirmer les cibles des boutons du graphe de navigation sur les maquettes | S1 | p2 | 0.5 j | — |
| S2-B1 | Backend : recherche d'annonces (filtres stricts PostGIS, pagination, tri) | S2 | p1 | 3 j | S1-06 |
| S2-B2 | Backend : détail d'annonce et favoris | S2 | p1 | 1.5 j | S2-B1 |
| S2-B3 | Android XML : Explorer, Filtres, Résultats, Chargement, Aucun résultat | S2 | p1 | 3.5 j | S1-08 |
| S2-B4 | Android : Détail d'annonce, Favoris (+ vide), recherche branchée | S2 | p1 | 3 j | S2-B1, S2-B2, S2-B3 |
| S3-B1 | Classement multicritère (service intelligence) et relaxation des filtres | S3 | p1 | 3.5 j | S2-B1 |
| S3-B2 | Backend : avis vérifiés et signalements | S3 | p2 | 2 j | S2-C1 |
| S3-B3 | Android : Carte, Alternatives proches, badges de compatibilité et de risque | S3 | p2 | 3 j | S3-B1, S2-B4 |
| S3-B4 | Android XML + câblage : Signalement, Confirmation, Avis (rédaction, publié) | S3 | p2 | 2.5 j | S3-B2 |
| S3-C3 | Backend : signaux d'alerte de messagerie (risk_flags par règles) | S3 | p2 | 1.5 j | S2-C5 |
| S4-04 | Tests et performance : parcours locataire, index de recherche, cache d'images | S4 | p1 | 2 j | — |

## Lot C · réservation, messagerie, Copilot

Total : 38.0 j-personne

| Code | Titre | Jalon | Prio | Estim. | Dépend de |
|---|---|---|---|---|---|
| S1-03 | Docker Compose : db + api + intelligence démarrent, Flyway applique V1–V2 | S1 | p1 | 0.5 j | — |
| S1-07 | Backend : erreurs uniformes, validation, tests avec Testcontainers, données de départ | S1 | p1 | 1.5 j | S1-03 |
| S1-08 | Android : socle (Hilt, graphes de navigation par lot, BaseViewModel/UiState, erreurs API) | S1 | p1 | 2 j | S1-02 |
| S1-11 | Android XML : États (3), Permissions (3), Compte (3) | S1 | p1 | 3 j | S1-08 |
| S1-14 | Décision : fournisseur du LLM externe, clé d'API et budget | S1 | p1 | 0.5 j | — |
| S2-C1 | Backend : réservations (demande, conflit de dates, suivi, annulation) | S2 | p1 | 3 j | S1-05 |
| S2-C2 | Backend : demandes côté propriétaire (accepter/refuser) et job « Terminée » | S2 | p1 | 2 j | S2-C1 |
| S2-C3 | Android XML : Réservation (demande, conflit, envoyée, suivi, annulation) et Mes réservations | S2 | p1 | 3.5 j | S1-08 |
| S2-C4 | Android : réservations branchées + écrans Demandes propriétaire (3) | S2 | p1 | 3 j | S2-C1, S2-C2, S2-C3 |
| S2-C5 | Backend : messagerie (conversations, messages, lecture) | S2 | p2 | 2 j | S1-05 |
| S2-D2 | Décision : quota Copilot et consentement présélectionné (loi 09-08) | S2 | p2 | 0.5 j | — |
| S3-C1 | Backend : consentements Copilot, quota, journal sans contenu | S3 | p2 | 2.5 j | S2-D2 |
| S3-C2 | Service intelligence : Copilot (appels de fonctions, garde-fous, conseil, comparaison, fiabilité) | S3 | p2 | 4.5 j | S1-14, S3-C1 |
| S3-C4 | Android XML + câblage : Messages, Conversation et alerte, Brouillon, Conseil privé, Consentement | S3 | p2 | 3.5 j | S2-C5, S3-C1 |
| S3-C5 | Android XML + câblage : onglet Copilot (accueil, réflexion, résultats, comparer 3, fiabilité, états) | S3 | p3 | 4 j | S3-C2, S3-C4 |
| S4-05 | Sécurité et conformité : suppression de compte, purge, logs, secrets | S4 | p1 | 2 j | — |

## Tâches communes (3 membres)

Total : 4.75 j-personne

| Code | Titre | Jalon | Prio | Estim. | Dépend de |
|---|---|---|---|---|---|
| S1-04 | Chaque membre valide son environnement (checklist personnelle) | S1 | p1 | 0.25 j | — |
| S4-01 | Traductions FR/AR/EN complètes et vérification RTL de tous les écrans | S4 | p1 | 1.5 j | — |
| S4-02 | Mode sombre sur les écrans concernés | S4 | p3 | 0.5 j | S2-D3 |
| S4-07 | Recette croisée : chaque membre teste les écrans d'un autre contre les maquettes | S4 | p1 | 1 j | — |
| S4-08 | Gel du code (J28) et préparation de la démo | S4 | p1 | 1.5 j | — |

---
# Détail des issues


## S1-01 · Dépôt GitHub : créer, protéger main, pousser le squelette, CI verte
**Code** `S1-01` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 0.5 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Avoir un dépôt commun propre dans lequel les 3 membres peuvent travailler par pull requests.

### À faire
- [ ] Créer le dépôt (privé) et inviter les 2 autres membres
- [ ] Pousser le contenu du zip tel quel (premier commit)
- [ ] Protéger `main` : PR obligatoire, 1 relecture, CI verte requise
- [ ] Lancer `scripts/create_issues.sh` (labels, milestones, issues)
- [ ] Vérifier que le workflow `CI` démarre sur la première PR

### Références
- docs/GUIDE_PROJET.md §3 et §6
- .github/workflows/ci.yml

### Critères d'acceptation
- [ ] `main` refuse un push direct
- [ ] Les 3 membres voient les issues et les milestones

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-02 · Android : synchro Gradle, build debug, lancement sur émulateur
**Code** `S1-02` · **Lot B · recherche et classement (locataire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 0.5 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Valider que le squelette Android compile et démarre (le rendu n'a jamais été vérifié).

### À faire
- [ ] Ouvrir `android/` dans Android Studio (Ladybug 2024.2.1 ou plus récent)
- [ ] Synchroniser Gradle ; corriger ou figer les versions de dépendances si une résolution échoue
- [ ] `./gradlew assembleDebug testDebugUnitTest` OK
- [ ] Lancer l'app sur un émulateur API 34/35 : l'écran PlaceholderFragment s'affiche
- [ ] Si erreur : la coller dans l'issue et corriger

### Références
- android/app/build.gradle.kts
- README.md

### Critères d'acceptation
- [ ] Build debug vert en local et en CI
- [ ] L'app se lance sans crash

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-03 · Docker Compose : db + api + intelligence démarrent, Flyway applique V1–V2
**Code** `S1-03` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 0.5 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Valider la pile serveur locale.

### À faire
- [ ] `docker compose up --build`
- [ ] `curl http://localhost:8080/api/v1/actuator/health` → `UP`
- [ ] `curl http://localhost:8000/health` → `ok`
- [ ] Se connecter à PostgreSQL (`localhost:5432`, base/utilisateur `shs`) et vérifier les tables du schéma V2
- [ ] Documenter dans l'issue les écarts rencontrés

### Références
- docker-compose.yml
- backend/src/main/resources/db/migration/

### Critères d'acceptation
- [ ] Les 3 conteneurs sont sains
- [ ] Toutes les tables de V2 existent

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-04 · Chaque membre valide son environnement (checklist personnelle)
**Code** `S1-04` · **Tâches communes (3 membres)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 0.25 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Éviter qu'un membre découvre un problème d'outil au milieu du sprint.

### À faire
- [ ] JDK 21, Docker, Git, Android Studio, Python 3.12 installés (versions : voir guide §2)
- [ ] Dépôt cloné, `docker compose up` OK
- [ ] `./gradlew assembleDebug` OK
- [ ] Postman/Bruno : import de `docs/openapi.yaml`
- [ ] Un client SQL (DBeaver ou psql) connecté à la base

### Références
- docs/GUIDE_PROJET.md §2

### Critères d'acceptation
- [ ] Chaque membre coche sa propre ligne en commentaire (3 commentaires « OK »)

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-05 · Backend : authentification JWT (register, login, refresh, logout, password-reset)
**Code** `S1-05` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 2.5 j · **Priorité** p1
**Dépend de** : S1-03

### Objectif
Remplacer le `httpBasic` du squelette par une vraie authentification par jetons.

### À faire
- [ ] Ajouter une bibliothèque JWT (jjwt ou Nimbus) au `pom.xml`
- [ ] BCrypt pour `password_hash` ; refresh token stocké haché dans `refresh_token`, rotation et révocation
- [ ] Filtre JWT dans `SecurityConfig` ; `/auth/**` et `/actuator/health` restent publics
- [ ] Routes `/auth/register|login|refresh|logout|password-reset` conformes à l'OpenAPI
- [ ] Tests d'intégration (MockMvc) : succès, 401 `INVALID_CREDENTIALS`, refresh expiré → `SESSION_EXPIRED`

### Références
- docs/openapi.yaml (tag auth)
- docs/10-seq-connexion.mmd
- docs/04-modele-donnees-er.mmd

### Critères d'acceptation
- [ ] Le secret JWT vient de `SHS_JWT_SECRET`, jamais du code
- [ ] Un jeton expiré ou révoqué est refusé

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-06 · Backend : /me, espaces, préférences de notification, établissements, profil locataire
**Code** `S1-06` · **Lot B · recherche et classement (locataire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 2 j · **Priorité** p1
**Dépend de** : S1-05

### Objectif
Fournir le socle « profil » nécessaire à la recherche personnalisée.

### À faire
- [ ] `GET/PATCH/DELETE /me` (suppression = `deleted_at`, purge plus tard)
- [ ] `PUT /me/active-space`, `POST /me/spaces`
- [ ] `GET/PUT /me/notification-preferences`
- [ ] `GET /establishments` (+ données de départ, voir S1-07)
- [ ] `GET/PUT /me/tenant-profile` : `budget_min` nullable, importances 0–100

### Références
- docs/openapi.yaml (tags me, tenant)
- DECISIONS.md (budget min)

### Critères d'acceptation
- [ ] Les contraintes CHECK de la base sont respectées côté validation (400 `VALIDATION_ERROR`)

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-07 · Backend : erreurs uniformes, validation, tests avec Testcontainers, données de départ
**Code** `S1-07` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 1.5 j · **Priorité** p1
**Dépend de** : S1-03

### Objectif
Donner aux 3 lots la même base technique pour les contrôleurs et les tests.

### À faire
- [ ] `@RestControllerAdvice` : format `{code, message, details}` avec les codes de `components/schemas/Error`
- [ ] Dépendance Testcontainers (image `postgis/postgis:16-3.4`) + classe de base pour tests d'intégration
- [ ] Migration `V3__seed_establishments.sql` : quelques établissements d'Al Hoceïma
- [ ] Convention de migrations par lot (voir guide §6) documentée dans le README

### Références
- docs/openapi.yaml (schéma Error)
- backend/src/main/resources/db/migration/

### Critères d'acceptation
- [ ] Un test d'intégration exemple passe en CI
- [ ] Un 400 renvoie `VALIDATION_ERROR` avec détails par champ

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-08 · Android : socle (Hilt, graphes de navigation par lot, BaseViewModel/UiState, erreurs API)
**Code** `S1-08` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 2 j · **Priorité** p1
**Dépend de** : S1-02

### Objectif
Éviter les conflits de fusion et donner un même modèle MVVM aux 3 membres.

### À faire
- [ ] Découper la navigation : `nav_graph.xml` racine qui `<include>` un graphe par lot (tenant, owner, copilot, auth)
- [ ] `UiState` générique (Loading / Content / Empty / Error) + `BaseViewModel`
- [ ] Mapping des erreurs Retrofit → codes API (`SESSION_EXPIRED`, `BOOKING_DATES_CONFLICT`…)
- [ ] Fichiers `strings` séparés par lot (`strings_auth.xml`, `strings_search.xml`, …)
- [ ] Exemple complet de bout en bout sur un écran factice

### Références
- docs/02-android-couches.mmd
- docs/05-navigation.mmd

### Critères d'acceptation
- [ ] Deux membres peuvent ajouter des écrans sans toucher aux mêmes fichiers

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-09 · Android XML : Splash, Découverte 1–3, Connexion et ses états
**Code** `S1-09` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 2.5 j · **Priorité** p1
**Dépend de** : S1-08

### Objectif
Reproduire fidèlement ces écrans d'après les maquettes (FR/AR/EN, RTL).

### À faire
- [ ] Splash
- [ ] Découverte 1, 2, 3
- [ ] Connexion + « identifiants invalides » + « session expirée »
- [ ] Comparer chaque écran à la maquette ; tout écart → issue `ecart`

### Références
- Maquettes (planche design system)
- docs/05-navigation.mmd

### Critères d'acceptation
- [ ] Aucune couleur/taille/police en dur ; `start/end` partout
- [ ] Aperçu RTL vérifié

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-10 · Android XML : Inscription (2), Préférences Rôle/Langue (2), Profil locataire 1–3 (3)
**Code** `S1-10` · **Lot B · recherche et classement (locataire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S1-08

### Objectif
Reproduire fidèlement ces 7 écrans.

### À faire
- [ ] Inscription (2 écrans)
- [ ] Préférences : Rôle, Langue
- [ ] Profil locataire 1 Établissement, 2 Préférences, 3 Importance (curseurs 0–100)
- [ ] Écarts → issues `ecart`

### Références
- Maquettes
- docs/05-navigation.mmd

### Critères d'acceptation
- [ ] Mêmes critères que S1-09

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-11 · Android XML : États (3), Permissions (3), Compte (3)
**Code** `S1-11` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S1-08

### Objectif
Reproduire fidèlement les écrans transverses (liste exacte à lire dans le doc d'inventaire, 9 écrans prévus).

### À faire
- [ ] États : sans internet, erreur serveur, session expirée
- [ ] Permissions : caméra, galerie, localisation
- [ ] Compte : déconnexion, suppression, compte supprimé (confirmer la liste sur les maquettes)

### Références
- Maquettes
- docs/05-navigation.mmd (sous-graphe Réglages)

### Critères d'acceptation
- [ ] Mêmes critères que S1-09

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-12 · Android : connexion et inscription branchées à l'API, gestion de session
**Code** `S1-12` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 1.5 j · **Priorité** p1
**Dépend de** : S1-05, S1-09

### Objectif
Parcours complet : inscription → connexion → écran d'accueil, avec renouvellement de jeton.

### À faire
- [ ] LoginViewModel / RegisterViewModel + UiState
- [ ] Stockage des jetons (`TokenStore`) et déconnexion
- [ ] Écran « Session expirée » quand le refresh échoue
- [ ] Splash : session valide → accueil, sinon découverte

### Références
- docs/10-seq-connexion.mmd
- android/.../data/AuthInterceptors.kt

### Critères d'acceptation
- [ ] Un jeton expiré renouvelé sans que l'utilisateur le voie
- [ ] Test unitaire du ViewModel (MockK)

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-13 · Android : profil de recherche branché à l'API, changement de langue et RTL
**Code** `S1-13` · **Lot B · recherche et classement (locataire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 1.5 j · **Priorité** p1
**Dépend de** : S1-06, S1-10

### Objectif
Les 3 écrans de profil enregistrent vraiment, et la langue choisie s'applique.

### À faire
- [ ] Appels `GET/PUT /me/tenant-profile` et `GET /establishments`
- [ ] Choix de la langue (FR/AR/EN) appliqué à l'app (`AppCompatDelegate.setApplicationLocales`)
- [ ] Vérifier le miroir RTL en arabe

### Références
- docs/openapi.yaml
- DECISIONS.md

### Critères d'acceptation
- [ ] Redémarrer l'app conserve le profil et la langue

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S1-14 · Décision : fournisseur du LLM externe, clé d'API et budget
**Code** `S1-14` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 0.5 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Point ouvert qui bloque la semaine 3 ; à trancher avant J10.

### À faire
- [ ] Comparer 2–3 fournisseurs (coût, latence, appel de fonctions, hébergement des données)
- [ ] Choisir un fournisseur et créer une clé de développement (jamais dans le dépôt)
- [ ] Consigner la décision dans `docs/DECISIONS.md`

### Références
- DECISIONS.md (points ouverts)
- docs/14-seq-copilot-consentement.mmd

### Critères d'acceptation
- [ ] Décision écrite, variable d'environnement `SHS_LLM_API_KEY` prévue dans `docker-compose.yml`


## S1-15 · Décision : confirmer les cibles des boutons du graphe de navigation sur les maquettes
**Code** `S1-15` · **Lot B · recherche et classement (locataire)** · **Jalon** S1 · Fondations (J1–J7, → 17/11) · **Estimation** 0.5 j · **Priorité** p2
**Dépend de** : rien

### Objectif
`docs/05-navigation.mmd` est déduit de l'inventaire, pas des maquettes.

### À faire
- [ ] Parcourir les maquettes et noter chaque écart de navigation
- [ ] Corriger `05-navigation.mmd` ; ouvrir des issues `ecart` pour les contradictions de règle métier

### Références
- docs/05-navigation.mmd

### Critères d'acceptation
- [ ] Graphe mis à jour et relu par les 2 autres membres


## S2-A1 · Backend : annonces propriétaire (brouillon, CRUD, étapes de publication)
**Code** `S2-A1` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S1-05

### Objectif
Permettre de créer et compléter une annonce par étapes.

### À faire
- [ ] `POST/GET /me/listings`, `GET/PATCH/DELETE /me/listings/{id}`, `draft_step`
- [ ] Propriétaire uniquement ; 403 `FORBIDDEN` sur l'annonce d'un autre
- [ ] Machine d'états : DRAFT → UNDER_REVIEW → PUBLISHED / ON_HOLD / SUSPENDED
- [ ] Annonce publiée : reste « Publiée » pendant le re-contrôle des champs sensibles

### Références
- docs/06-etat-annonce.mmd
- DECISIONS.md
- docs/openapi.yaml (tag owner)

### Critères d'acceptation
- [ ] Transitions interdites refusées
- [ ] Tests d'intégration des états

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-A2 · Backend : photos (upload, ordre, couverture, empreinte phash), soumission
**Code** `S2-A2` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 2.5 j · **Priorité** p1
**Dépend de** : S2-A1

### Objectif
Gérer les photos d'annonce et la soumission.

### À faire
- [ ] `POST/DELETE /me/listings/{id}/photos`, `PUT .../photos/order`
- [ ] Stockage fichier sur volume Docker ; contrôle du type et de la taille
- [ ] Calcul du phash (`listing_photo.phash`)
- [ ] `POST /me/listings/{id}/submit` → UNDER_REVIEW (champs requis vérifiés)

### Références
- docs/11-seq-publication-justificatif.mmd

### Critères d'acceptation
- [ ] Pas de fichier exécutable accepté
- [ ] Photo de couverture unique

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-A3 · Android XML : Publication 1–5 et Récapitulatif
**Code** `S2-A3` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S1-08

### Objectif
6 écrans : Informations, Prix, Détails, Localisation, Photos, Récapitulatif.

### À faire
- [ ] Reproduire les 6 écrans fidèlement
- [ ] Brouillon conservé entre les étapes
- [ ] Écarts → `ecart`

### Références
- Maquettes
- DECISIONS.md (ordre des étapes)

### Critères d'acceptation
- [ ] Critères communs XML (PR template)

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-A4 · Android : Mes annonces (liste, vide) et publication branchée à l'API
**Code** `S2-A4` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S2-A1, S2-A2, S2-A3

### Objectif
Le propriétaire peut créer, voir et soumettre une annonce.

### À faire
- [ ] Liste « Mes annonces » + état vide
- [ ] Wizard de publication ↔ API (PATCH par étape)
- [ ] Photos : caméra/galerie avec permissions
- [ ] Changement d'espace Locataire ↔ Propriétaire

### Références
- docs/05-navigation.mmd (sous-graphe Propriétaire)

### Critères d'acceptation
- [ ] Parcours complet testé sur émulateur

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-B1 · Backend : recherche d'annonces (filtres stricts PostGIS, pagination, tri)
**Code** `S2-B1` · **Lot B · recherche et classement (locataire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S1-06

### Objectif
`GET /listings/search` sans classement intelligent (le score arrive en S3).

### À faire
- [ ] Filtres : statut PUBLISHED, budget, distance (`ST_DWithin` depuis l'établissement), types, équipements
- [ ] Pagination `page`/`size`, tri prix/date
- [ ] Champ de score provisoire (80, comme le service simulé)
- [ ] Jeu de données de test (`V4`/seed) d'au moins 30 annonces

### Références
- docs/12-seq-recherche-classement.mmd
- docs/openapi.yaml (tag tenant)

### Critères d'acceptation
- [ ] Requête < 300 ms sur le jeu de test
- [ ] Tests d'intégration sur chaque filtre

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-B2 · Backend : détail d'annonce et favoris
**Code** `S2-B2` · **Lot B · recherche et classement (locataire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 1.5 j · **Priorité** p1
**Dépend de** : S2-B1

### Objectif
Consultation et mise en favori.

### À faire
- [ ] `GET /listings/{id}` (photos, équipements, note)
- [ ] `GET /favorites`, `PUT/DELETE /favorites/{listingId}` idempotents

### Références
- docs/openapi.yaml

### Critères d'acceptation
- [ ] Une annonce non publiée n'est pas visible par un locataire

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-B3 · Android XML : Explorer, Filtres, Résultats, Chargement, Aucun résultat
**Code** `S2-B3` · **Lot B · recherche et classement (locataire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3.5 j · **Priorité** p1
**Dépend de** : S1-08

### Objectif
Les écrans de recherche (+ espace locataire à 5 destinations).

### À faire
- [ ] Barre de navigation à 5 destinations (Explorer, Favoris, Copilot, Messages, Profil)
- [ ] Accueil Explorer, Filtres, Résultats, Chargement, Aucun résultat

### Références
- Maquettes
- docs/05-navigation.mmd

### Critères d'acceptation
- [ ] Critères communs XML

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-B4 · Android : Détail d'annonce, Favoris (+ vide), recherche branchée
**Code** `S2-B4` · **Lot B · recherche et classement (locataire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S2-B1, S2-B2, S2-B3

### Objectif
Parcours locataire de bout en bout jusqu'au détail.

### À faire
- [ ] Écran Détail (galerie, prix, équipements)
- [ ] Favoris + état vide
- [ ] Résultats ↔ `/listings/search` avec pagination
- [ ] Écran Chargement et gestion d'erreurs

### Références
- docs/12-seq-recherche-classement.mmd

### Critères d'acceptation
- [ ] Parcours testé sur émulateur

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-C1 · Backend : réservations (demande, conflit de dates, suivi, annulation)
**Code** `S2-C1` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S1-05

### Objectif
Gérer le cycle de vie d'une réservation.

### À faire
- [ ] `POST /listings/{id}/bookings` : référence `SH-AAAA-NNNN` via `booking_ref_seq`, `service_fee` = 0, total estimé
- [ ] Conflit de dates → 409 `BOOKING_DATES_CONFLICT` (contrainte d'exclusion déjà dans le schéma)
- [ ] `GET /me/bookings`, `GET /bookings/{id}`, `POST /bookings/{id}/cancel`
- [ ] Machine d'états REQUESTED / ACCEPTED / REFUSED / CANCELLED / COMPLETED

### Références
- docs/08-etat-reservation.mmd
- docs/13-seq-reservation-conflit.mmd
- DECISIONS.md (référence, frais)

### Critères d'acceptation
- [ ] Test de concurrence sur deux demandes acceptées qui se chevauchent

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-C2 · Backend : demandes côté propriétaire (accepter/refuser) et job « Terminée »
**Code** `S2-C2` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 2 j · **Priorité** p1
**Dépend de** : S2-C1

### Objectif
Le propriétaire traite les demandes ; les séjours passés deviennent COMPLETED.

### À faire
- [ ] `GET /me/booking-requests`, `POST /bookings/{id}/accept|refuse`
- [ ] Accepter un séjour qui chevauche un séjour accepté → erreur claire
- [ ] Tâche planifiée REQUESTED/ACCEPTED → COMPLETED à la date de fin

### Références
- docs/08-etat-reservation.mmd

### Critères d'acceptation
- [ ] Seul le propriétaire de l'annonce peut accepter

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-C3 · Android XML : Réservation (demande, conflit, envoyée, suivi, annulation) et Mes réservations
**Code** `S2-C3` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3.5 j · **Priorité** p1
**Dépend de** : S1-08

### Objectif
6 à 7 écrans côté locataire ; « Mes réservations » s'ouvre depuis Profil.

### À faire
- [ ] Réservation · Demande, Conflit de dates, Demande envoyée, Détail et suivi, Confirmer l'annulation
- [ ] Mes réservations (depuis Profil)

### Références
- Maquettes
- DECISIONS.md (Mes réservations)

### Critères d'acceptation
- [ ] Critères communs XML

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-C4 · Android : réservations branchées + écrans Demandes propriétaire (3)
**Code** `S2-C4` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 3 j · **Priorité** p1
**Dépend de** : S2-C1, S2-C2, S2-C3

### Objectif
Parcours réservation complet des deux côtés.

### À faire
- [ ] Locataire : demande, conflit 409, suivi, annulation
- [ ] Propriétaire : Demandes, Chevauchement, Aucune demande (XML + API)

### Références
- docs/13-seq-reservation-conflit.mmd

### Critères d'acceptation
- [ ] Parcours testé à 2 comptes sur émulateur

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-C5 · Backend : messagerie (conversations, messages, lecture)
**Code** `S2-C5` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 2 j · **Priorité** p2
**Dépend de** : S1-05

### Objectif
Échanges locataire ↔ propriétaire (le Copilot et les alertes s'ajoutent en S3).

### À faire
- [ ] `POST /listings/{id}/conversations` (unique par annonce+locataire), `GET /conversations`
- [ ] `GET/POST /conversations/{id}/messages`, `POST .../read`
- [ ] Pagination et ordre par `sent_at`

### Références
- docs/openapi.yaml (tag messaging)

### Critères d'acceptation
- [ ] Un tiers ne peut pas lire la conversation

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S2-D1 · Décision : durée de conservation des justificatifs et seuil de suspension automatique
**Code** `S2-D1` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 0.5 j · **Priorité** p2
**Dépend de** : rien

### Objectif
Nécessaire pour `retention_until` et la règle ON_HOLD/SUSPENDED.

### À faire
- [ ] Proposer des valeurs (conservation en mois ; seuil de risque)
- [ ] Vérifier la cohérence avec la loi 09-08 (CNDP)
- [ ] Consigner dans `docs/DECISIONS.md`

### Références
- DECISIONS.md

### Critères d'acceptation
- [ ] Valeurs décidées et reportées dans l'issue S3-A2


## S2-D2 · Décision : quota Copilot et consentement présélectionné (loi 09-08)
**Code** `S2-D2` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 0.5 j · **Priorité** p2
**Dépend de** : rien

### Objectif
Avant d'implémenter S3-C1.

### À faire
- [ ] Fixer le quota (requêtes par fenêtre de temps)
- [ ] Trancher : case de consentement cochée par défaut (comme la maquette) ou non
- [ ] Consigner dans `docs/DECISIONS.md`

### Références
- DECISIONS.md
- docs/14-seq-copilot-consentement.mmd

### Critères d'acceptation
- [ ] Décision écrite ; si écart avec la maquette → issue `ecart`


## S2-D3 · Décision : couleurs des badges en mode sombre et ombre 0/6/20
**Code** `S2-D3` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S2 · Parcours de base (J8–J14, → 24/11) · **Estimation** 0.5 j · **Priorité** p3
**Dépend de** : rien

### Objectif
Points ouverts du design system.

### À faire
- [ ] Relever les couleurs sur les 5 écrans sombres
- [ ] Comparer l'ombre à l'élévation 6 dp
- [ ] Mettre à jour `values-night/colors.xml`

### Références
- DECISIONS.md
- android/.../values-night/colors.xml

### Critères d'acceptation
- [ ] Pull request avec captures avant/après


## S3-A1 · Service intelligence : vérification du justificatif (OCR, extraction, cohérence)
**Code** `S3-A1` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 3 j · **Priorité** p2
**Dépend de** : S2-A2

### Objectif
Remplacer la route simulée `/internal/proof/verify`.

### À faire
- [ ] OCR (Tesseract dans l'image Docker) + extraction nom et adresse
- [ ] Score de cohérence avec le propriétaire et l'adresse de l'annonce
- [ ] Statuts VERIFIED / TO_COMPLETE / INCONSISTENT
- [ ] Tests pytest sur 4–5 documents factices

### Références
- docs/11-seq-publication-justificatif.mmd
- docs/07-etat-justificatif.mmd

### Critères d'acceptation
- [ ] Aucun document réel ni donnée personnelle dans le dépôt

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-A2 · Backend : dépôt du justificatif (chiffrement, hash, statut) et publication
**Code** `S3-A2` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 3 j · **Priorité** p2
**Dépend de** : S3-A1, S2-D1

### Objectif
`POST/GET /me/listings/{id}/proof`.

### À faire
- [ ] Upload multipart, chiffrement AES-GCM, `file_hash`, `file_ref` jamais exposé
- [ ] Appel `/internal/proof/verify` puis `/internal/trust`
- [ ] Règle : justificatif non incohérent et risque faible/moyen → PUBLISHED, sinon ON_HOLD
- [ ] `retention_until` renseigné

### Références
- docs/11-seq-publication-justificatif.mmd
- docs/07-etat-justificatif.mmd

### Critères d'acceptation
- [ ] Code `PROOF_REQUIRED` si publication sans justificatif

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-A3 · Service intelligence + backend : indice de risque (TF-IDF, phash, prix, règles)
**Code** `S3-A3` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 3.5 j · **Priorité** p2
**Dépend de** : S2-A2

### Objectif
`/internal/trust` et `GET /listings/{id}/trust`.

### À faire
- [ ] Similarité de description (TF-IDF) et de photos (phash) entre annonces
- [ ] Prix par rapport à la médiane de la zone
- [ ] Règles explicites avec raisons lisibles (`reasons`)
- [ ] Stockage dans `trust_score`

### Références
- docs/03-backend.mmd

### Critères d'acceptation
- [ ] Niveau LOW/MEDIUM/HIGH expliqué, jamais une accusation

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-A4 · Android XML + câblage : justificatif (5 états) et gestion d'annonce (publiée/en attente/suspendue/modif/suppression)
**Code** `S3-A4` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 4 j · **Priorité** p2
**Dépend de** : S3-A2, S2-A4

### Objectif
Écrans de suivi côté propriétaire.

### À faire
- [ ] Justificatif ajouté, Vérification en cours, Vérifié automatiquement, À compléter, Incohérente
- [ ] Gestion : publiée, en attente, suspendue, mode modification, confirmation de suppression
- [ ] Sondage du statut `GET .../proof`

### Références
- docs/07-etat-justificatif.mmd
- docs/06-etat-annonce.mmd

### Critères d'acceptation
- [ ] Critères communs XML

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-B1 · Classement multicritère (service intelligence) et relaxation des filtres
**Code** `S3-B1` · **Lot B · recherche et classement (locataire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 3.5 j · **Priorité** p1
**Dépend de** : S2-B1

### Objectif
Remplacer le score fixe par un vrai pourcentage de compatibilité.

### À faire
- [ ] `/internal/rank` : pondération par les importances du profil (budget, distance, équipements, note)
- [ ] Explications `strengths` / `cautions`
- [ ] Backend : exclure le risque HIGH, facteur 0,9 pour MEDIUM
- [ ] Aucun résultat → assouplir budget de 10 %, puis distance de 50 % (`relaxed=true`)
- [ ] `GET /listings/{id}/match`

### Références
- docs/12-seq-recherche-classement.mmd

### Critères d'acceptation
- [ ] Résultats stables et triés par score ; tests pytest du calcul

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-B2 · Backend : avis vérifiés et signalements
**Code** `S3-B2` · **Lot B · recherche et classement (locataire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 2 j · **Priorité** p2
**Dépend de** : S2-C1

### Objectif
Réputation et modération.

### À faire
- [ ] `POST /bookings/{id}/review` seulement si COMPLETED, 1 avis par réservation
- [ ] `GET /listings/{id}/reviews` + mise à jour de `rating_avg` / `review_count`
- [ ] `POST /listings/{id}/reports` (1 signalement par utilisateur et annonce)

### Références
- docs/openapi.yaml
- DECISIONS.md

### Critères d'acceptation
- [ ] Un avis hors séjour terminé est refusé

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-B3 · Android : Carte, Alternatives proches, badges de compatibilité et de risque
**Code** `S3-B3` · **Lot B · recherche et classement (locataire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 3 j · **Priorité** p2
**Dépend de** : S3-B1, S2-B4

### Objectif
Enrichir la recherche.

### À faire
- [ ] Carte osmdroid, permission localisation
- [ ] Écran Alternatives proches
- [ ] Affichage du pourcentage et du badge dans Résultats et Détail

### Références
- docs/05-navigation.mmd

### Critères d'acceptation
- [ ] Tuiles OSM avec attribution

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-B4 · Android XML + câblage : Signalement, Confirmation, Avis (rédaction, publié)
**Code** `S3-B4` · **Lot B · recherche et classement (locataire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 2.5 j · **Priorité** p2
**Dépend de** : S3-B2

### Objectif
Écrans de feedback.

### À faire
- [ ] Signalement + Confirmation (motifs de l'énumération `ReportReason`, à confronter à l'écran)
- [ ] Avis · Rédaction, Avis publié

### Références
- docs/openapi.yaml (ReportReason)

### Critères d'acceptation
- [ ] Écart de motifs → issue `ecart`

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-C1 · Backend : consentements Copilot, quota, journal sans contenu
**Code** `S3-C1` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 2.5 j · **Priorité** p2
**Dépend de** : S2-D2

### Objectif
Garde-fous obligatoires du Copilot.

### À faire
- [ ] `GET/PUT/DELETE /copilot/consents/{scope}` ; rien enregistré avant « Autoriser »
- [ ] 422 `CONSENT_REQUIRED` si analyse sans consentement
- [ ] `GET /copilot/quota`, 429 `COPILOT_RATE_LIMIT` avec `retryAfterSeconds`
- [ ] `copilot_log` : nom d'outil et statut, jamais le contenu des messages

### Références
- docs/14-seq-copilot-consentement.mmd
- DECISIONS.md

### Critères d'acceptation
- [ ] Révoquer un consentement bloque immédiatement l'analyse

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-C2 · Service intelligence : Copilot (appels de fonctions, garde-fous, conseil, comparaison, fiabilité)
**Code** `S3-C2` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 4.5 j · **Priorité** p2
**Dépend de** : S1-14, S3-C1

### Objectif
`/internal/copilot/run` + routes `/copilot/chat`, `/advice`, `/compare`, `/reliability`.

### À faire
- [ ] Client LLM + liste blanche de fonctions métier appelées par l'API
- [ ] Les messages sont des données non fiables (anti prompt-injection)
- [ ] Réponse : texte, certitude LOW/MEDIUM/HIGH, avertissement « Copilot peut se tromper »
- [ ] Suggestion de réponse modifiable, jamais envoyée automatiquement
- [ ] Mode dégradé si le LLM est indisponible

### Références
- docs/14-seq-copilot-consentement.mmd
- docs/03-backend.mmd

### Critères d'acceptation
- [ ] Clé LLM uniquement par variable d'environnement
- [ ] Tests : injection dans un message ne déclenche aucune fonction non autorisée

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-C3 · Backend : signaux d'alerte de messagerie (risk_flags par règles)
**Code** `S3-C3` · **Lot B · recherche et classement (locataire)** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 1.5 j · **Priorité** p2
**Dépend de** : S2-C5

### Objectif
Alerter sur paiement hors plateforme, urgence, etc.

### À faire
- [ ] Règles explicites (mots-clés, motifs) → `message.risk_flags`
- [ ] Jamais calculé par le LLM
- [ ] Tests unitaires des règles

### Références
- docs/04-modele-donnees-er.mmd

### Critères d'acceptation
- [ ] Un signal est une alerte, pas une conclusion

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-C4 · Android XML + câblage : Messages, Conversation et alerte, Brouillon, Conseil privé, Consentement
**Code** `S3-C4` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 3.5 j · **Priorité** p2
**Dépend de** : S2-C5, S3-C1

### Objectif
Messagerie et Copilot dans la conversation.

### À faire
- [ ] Messages (liste, vide), Conversation et alerte, Brouillon non envoyé (Room)
- [ ] Conseil privé, Analyse simplifiée, Consentement (Autoriser / Pas maintenant)
- [ ] « Utiliser ce brouillon » remplit le champ sans envoyer

### Références
- docs/14-seq-copilot-consentement.mmd

### Critères d'acceptation
- [ ] Critères communs XML

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S3-C5 · Android XML + câblage : onglet Copilot (accueil, réflexion, résultats, comparer 3, fiabilité, états)
**Code** `S3-C5` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S3 · Intelligence et écrans avancés (J15–J21, → 01/12) · **Estimation** 4 j · **Priorité** p3
**Dépend de** : S3-C2, S3-C4

### Objectif
Écrans de l'onglet central.

### À faire
- [ ] Accueil, Réflexion, Conversation et résultats
- [ ] Comparer 3 logements, Fiabilité, Aucun logement
- [ ] Limite atteinte, Hors ligne

### Références
- docs/05-navigation.mmd (sous-graphe Copilot)

### Critères d'acceptation
- [ ] Critères communs XML

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S4-01 · Traductions FR/AR/EN complètes et vérification RTL de tous les écrans
**Code** `S4-01` · **Tâches communes (3 membres)** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 1.5 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Chaque membre vérifie les écrans de son lot, en arabe (RTL) et en anglais.

### À faire
- [ ] A : écrans auth + propriétaire
- [ ] B : écrans locataire (recherche, profil)
- [ ] C : réservation, messagerie, Copilot, transverses
- [ ] Aucun texte en dur ; libellés arabes des équipements (`amenity.label_ar`) renseignés

### Références
- Maquettes arabes

### Critères d'acceptation
- [ ] Captures FR/AR/EN jointes à la PR

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S4-02 · Mode sombre sur les écrans concernés
**Code** `S4-02` · **Tâches communes (3 membres)** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 0.5 j · **Priorité** p3
**Dépend de** : S2-D3

### Objectif
Appliquer les couleurs sombres aux 5 écrans prévus.

### À faire
- [ ] Vérifier les ressources `values-night`
- [ ] Captures avant/après

### Références
- android/.../values-night/colors.xml

### Critères d'acceptation
- [ ] Pas de texte illisible

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S4-03 · Tests : parcours propriétaire de bout en bout
**Code** `S4-03` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 1.5 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Garantir le scénario : inscription → publication → justificatif → réservation reçue.

### À faire
- [ ] Tests d'intégration backend du parcours
- [ ] Un test instrumenté ou une checklist manuelle documentée

### Références
- docs/09-cas-utilisation.mmd

### Critères d'acceptation
- [ ] Scénario rejouable par un autre membre

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S4-04 · Tests et performance : parcours locataire, index de recherche, cache d'images
**Code** `S4-04` · **Lot B · recherche et classement (locataire)** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 2 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Rendre la recherche fluide et fiable.

### À faire
- [ ] `EXPLAIN ANALYZE` des requêtes de recherche, ajustement des index
- [ ] Cache Coil, pagination fluide
- [ ] Tests du parcours locataire

### Références
- docs/12-seq-recherche-classement.mmd

### Critères d'acceptation
- [ ] Aucune requête de recherche > 300 ms sur le jeu de démonstration

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S4-05 · Sécurité et conformité : suppression de compte, purge, logs, secrets
**Code** `S4-05` · **Lot C · réservation, messagerie, Copilot** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 2 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Respecter la loi 09-08 au niveau de la mise en œuvre.

### À faire
- [ ] `DELETE /me` + tâche de purge des comptes
- [ ] Aucune donnée personnelle ni secret dans les logs
- [ ] Variables d'environnement pour tous les secrets
- [ ] Tests de contrôle d'accès (403 sur ressources d'autrui)

### Références
- DECISIONS.md
- docs/03-backend.mmd

### Critères d'acceptation
- [ ] Revue croisée de sécurité par un autre membre


## S4-06 · Build release signé, visuels Play Store, README final
**Code** `S4-06` · **Lot A · annonces et justificatif (propriétaire)** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 1.5 j · **Priorité** p2
**Dépend de** : rien

### Objectif
Livrables d'emballage.

### À faire
- [ ] Keystore (hors dépôt), build release
- [ ] Visuels du store (déjà fournis dans le dossier de design)
- [ ] README final : installation, lancement, comptes de démonstration

### Références
- README.md

### Critères d'acceptation
- [ ] Un tiers installe l'APK et lance le parcours

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S4-07 · Recette croisée : chaque membre teste les écrans d'un autre contre les maquettes
**Code** `S4-07` · **Tâches communes (3 membres)** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 1 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Détecter les écarts avant la livraison.

### À faire
- [ ] A teste le lot B, B teste le lot C, C teste le lot A
- [ ] Ouvrir une issue `ecart` ou `bug` pour chaque différence

### Références
- .github/ISSUE_TEMPLATE/ecart-maquette.md

### Critères d'acceptation
- [ ] Plus aucun écart bloquant ouvert

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs


## S4-08 · Gel du code (J28) et préparation de la démo
**Code** `S4-08` · **Tâches communes (3 membres)** · **Jalon** S4 · Finitions et livraison (J22–J30, → 10/12) · **Estimation** 1.5 j · **Priorité** p1
**Dépend de** : rien

### Objectif
Jeu de données de démonstration, scénario, vidéo de secours.

### À faire
- [ ] Gel de `main` à J28 (08/12), correctifs uniquement
- [ ] Données de démonstration (comptes locataire et propriétaire)
- [ ] Scénario de démo + enregistrement vidéo

### Critères d'acceptation
- [ ] Démo rejouée 2 fois sans erreur

### Définition de terminé
- [ ] Code relu par un autre membre
- [ ] Testé (unitaire ou manuel, décrit dans la PR)
- [ ] Écran XML : comparé à la maquette, textes FR/AR/EN, RTL vérifié, mode sombre si l'écran existe
- [ ] Aucune couleur, taille ou police en dur ; `start/end` (jamais `left/right`)
- [ ] Route d'API conforme à `docs/openapi.yaml` (ou contrat mis à jour)
- [ ] Aucune donnée personnelle ni secret dans le code ou les logs
