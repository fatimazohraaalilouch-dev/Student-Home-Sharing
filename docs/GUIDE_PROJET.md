# Guide du projet Student Home Sharing (SHS)

Application Android de logement étudiant à Al Hoceïma. Équipe de 3 · J1 = 11/11/2026 · J30 = 10/12/2026.
Ce guide suffit pour installer, lancer et commencer à coder dès aujourd'hui. Les tâches sont dans `docs/ISSUES.md` (et créées sur GitHub par `scripts/create_issues.sh`).

## 1. Le produit en bref
- **Deux espaces** dans une même app : Locataire (5 destinations : Explorer, Favoris, Copilot, Messages, Profil) et Propriétaire (4 destinations : Mes annonces, Demandes, Messages, Profil). Un utilisateur peut basculer entre les deux.
- **Parcours locataire** : inscription → profil de recherche (établissement, budget, distance, types, équipements, importance de chaque critère) → résultats classés avec % de compatibilité → détail → demande de réservation → suivi → avis.
- **Parcours propriétaire** : publication en 6 étapes (Informations, Prix, Détails, Localisation, Photos, Récapitulatif) → justificatif d'autorisation de louer → vérification automatique → annonce publiée ou en attente → traitement des demandes.
- **Copilot** : conseiller et accompagnateur du locataire (recherche, comparaison de 3 logements, fiabilité, conseil privé dans la messagerie). Il propose, il n'envoie rien tout seul, et ne s'exécute qu'avec consentement.
- **Décisions déjà prises** : pas de rôle administrateur ; justificatif obligatoire pour le propriétaire ; frais de service conservés mais fixés à 0 ; langues FR/AR/EN avec RTL ; montants en MAD.

## 2. Prérequis (à installer aujourd'hui)

| Outil | Version | Pourquoi |
|---|---|---|
| Git | récent | versionnement |
| Compte GitHub + GitHub CLI (`gh`) | `gh` 2.x | dépôt, issues (script) |
| Docker Desktop (WSL2 sous Windows) | récent, ≥ 8 Go de RAM alloués si possible | base PostGIS, API, service Python |
| JDK | **21** (backend) | Spring Boot 3.3.5 |
| Android Studio | **Ladybug 2024.2.1 ou plus récent** | requis par Android Gradle Plugin 8.7.2 |
| SDK Android | plateforme **35**, image d'émulateur API 34 ou 35 | `compileSdk`/`targetSdk` 35, `minSdk` 26 |
| Python | 3.12 (optionnel en local, Docker suffit) | service d'intelligence |
| Client API | Bruno ou Postman | importer `docs/openapi.yaml` |
| Client SQL | DBeaver ou `psql` | inspecter PostgreSQL (`localhost:5432`, base et utilisateur `shs`, mot de passe de dev `shs_dev`) |
| Extension Mermaid (VS Code) | — | lire `docs/*.mmd` |

Le projet Android compile avec Gradle 8.10.2 (wrapper inclus), Kotlin 2.0.21, Hilt 2.52, Retrofit 2.11, Room 2.6.1, osmdroid 6.1.20. Android Studio utilise son propre JDK 17 pour Gradle : rien à régler.
Matériel conseillé : 16 Go de RAM (émulateur + Docker + IDE en même temps).
Comptes et clés à prévoir plus tard : une clé d'API de LLM (issue S1-14), jamais dans le dépôt.

## 3. Mise en route en 30 minutes

```bash
# 1. Cloner le dépôt existant, y copier le contenu du zip, puis pousser
git clone https://github.com/fatimazohraaalilouch-dev/Student-Home-Sharing.git
cd Student-Home-Sharing
#   copier ici tout le contenu du dossier shs/ du zip (y compris .github et .gitignore)
git add .
git update-index --chmod=+x android/gradlew scripts/create_issues.sh
git commit -m "chore: squelette SHS J1-J5"
git push origin main

# 2. Inviter asmaaaa01ab et zinebs_dev (Settings > Collaborators) ; elles doivent accepter l'invitation
# 3. Créer labels, jalons et issues (les usernames sont déjà dans le script)
gh auth login
./scripts/create_issues.sh

# 4. Lancer le serveur
docker compose up --build
curl http://localhost:8080/api/v1/actuator/health      # {"status":"UP"}
curl http://localhost:8000/health                      # {"status":"ok","simulated":true}
```
Android : ouvrir le dossier `android/` dans Android Studio, laisser Gradle synchroniser, lancer sur un émulateur. L'app parle à l'API via `http://10.0.2.2:8080/api/v1/` (c'est l'adresse de votre machine vue depuis l'émulateur). Les polices Plus Jakarta Sans sont déjà dans `res/font`.

**Important : ce squelette n'a jamais été compilé.** `PROGRESS.md` le dit déjà : c'est la première chose à vérifier (issues S1-02 et S1-03). Si une dépendance ne se résout pas, figez sa version et notez-le dans l'issue.

## 4. Structure du dépôt

```
shs/
├─ android/            App Kotlin, vues XML, ViewBinding, MVVM, Hilt, Retrofit, Room, osmdroid
│  └─ app/src/main/java/ma/shs/app/{ui,data,di}  + res/{values,values-night,font,layout,navigation}
├─ backend/            Spring Boot 3.3 (Java 21), JPA, Flyway, Spring Security
│  └─ src/main/resources/db/migration/   V1 extensions, V2 schéma
├─ intelligence/       FastAPI : classement, risque, justificatif, Copilot (routes simulées pour l'instant)
├─ docs/               openapi.yaml (contrat), 15 diagrammes .mmd, DECISIONS.md, PROGRESS.md, ISSUES.md
├─ scripts/            create_issues.sh
├─ .github/            CI, modèle de PR, modèle d'issue « écart maquette », corps des issues
└─ docker-compose.yml  db (PostGIS 16) + api (:8080) + intelligence (:8000)
```
Modifications faites par rapport au zip reçu : ajout du wrapper Gradle et des 4 polices au bon endroit, des fichiers `.github`, d'un `.gitignore`, et d'un *healthcheck* PostgreSQL dans `docker-compose.yml` (sans lui, l'API peut démarrer avant la base et planter).

## 5. Architecture

- **Android** (docs/02) : Fragments + layouts XML → ViewModel (`StateFlow<UiState>`) → use cases → repositories → Retrofit/OkHttp (intercepteur Bearer + `Authenticator` de refresh) / Room (brouillons de messages) / EncryptedSharedPreferences (jetons). Navigation Component, injection Hilt.
- **API** (docs/03) : filtre JWT → contrôleurs `/api/v1` (auth, me, listings, bookings, conversations, copilot) → services métier → repositories JPA → PostgreSQL + PostGIS. Tâches planifiées (réservation « Terminée », purge des comptes).
- **Service intelligence** (docs/03) : appelé **uniquement par l'API** sur `/internal/*` (rank, trust, proof/verify, copilot/run), jamais par l'app. Il appelle le LLM et rappelle l'API par des fonctions métier autorisées.
- **Données** (docs/04 + `V2__schema.sql`) : `app_user`, `listing`, `booking` (contrainte d'exclusion : deux réservations acceptées ne se chevauchent pas), `owner_proof`, `trust_score`, `conversation`/`message` (`risk_flags` calculés par règles), `copilot_consent`/`copilot_usage`/`copilot_log` (sans contenu des messages).
- **États** : annonce (docs/06), justificatif (docs/07), réservation (docs/08).
- **Séquences clés** : connexion (10), publication + justificatif (11), recherche + classement (12), réservation en conflit (13), Copilot + consentement (14).

### Règles d'or
1. **OpenAPI d'abord** : on ne change pas un nom de route ou de champ sans modifier `docs/openapi.yaml` dans la même PR et prévenir les autres.
2. **Maquette = référence.** Un écran XML est reproduit fidèlement. Si une maquette contredit un document de référence ou une règle, on ouvre une issue « Écart maquette » et **personne ne tranche seul**.
3. **Le LLM ne décide rien d'important** : les signaux d'alerte, le risque et la vérification de justificatif sont calculés par des règles ; le Copilot propose, l'utilisateur décide.
4. **Aucun secret ni donnée personnelle** dans le code, les logs ou le dépôt (clé LLM, mots de passe, vrais justificatifs).

## 6. Conventions de travail

**Branches** : `feature/<code-issue>-<mot-clé>` (ex. `feature/S2-B1-recherche`). Une PR par issue, la plus petite possible, `closes #N` dans la description.
**Commits** : `feat:`, `fix:`, `chore:`, `docs:`, `test:` + message court en français.
**PR** : modèle fourni (définition de terminé). Relue par un autre membre ; CI verte obligatoire ; fusion en *squash*.
**Pour éviter les conflits de fusion** (là où l'équipe va se marcher dessus) :
- Navigation : un graphe par lot, inclus dans `nav_graph.xml` (issue S1-08). Ne modifiez pas le graphe d'un autre lot.
- Textes : un fichier `strings_<fonction>.xml` par fonctionnalité, avec les dossiers `values`, `values-ar`, `values-en`.
- Migrations Flyway : ne **jamais** modifier une migration déjà fusionnée. Plages réservées : V3–V9 communes, **V10–V19 lot A, V20–V29 lot B, V30–V39 lot C**.
- `docs/openapi.yaml` : modifications en PR dédiées et courtes.

**Règles Android (PR template)** : aucune couleur, taille ou police en dur (tout vient de `colors`, `dimens`, `type`, `styles`) ; `start/end` et jamais `left/right` ; textes FR/AR/EN ; aperçu RTL ; mode sombre pour les écrans qui existent en sombre ; ViewBinding ; pas de logique dans les Fragments.
**Règles backend** : erreurs au format `{code, message, details}` ; validation par annotations ; tests d'intégration avec Testcontainers ; montants en MAD, dates en UTC.

## 7. Qui fait quoi

Répartition issue de `DECISIONS.md` ; Fatima = lot B.

| Lot | Responsable | Périmètre |
|---|---|---|
| **A** | asmaaaa01ab | Authentification (backend et écrans), publication d'annonce, photos, justificatif, indice de risque, gestion des annonces |
| **B** | Fatima | Profil locataire, recherche, classement, détail, favoris, carte, avis, signalements, signaux d'alerte de messagerie |
| **C** | zinebs_dev | Socle Android et erreurs API, réservations, messagerie, Copilot (consentement, quota, LLM, écrans), conformité |

Les trois membres réalisent aussi les tâches communes (environnement, traductions et RTL, recette croisée, démo).
Charge estimée (jours-personne) : A ≈ 41, B ≈ 38, C ≈ 43. Voir §9.

## 8. Planning

| Jalon | Dates | Objectif |
|---|---|---|
| **S1 · Fondations** | J1–J7 (→ 17/11) | Tout compile et démarre ; authentification complète ; 23 premiers écrans XML ; profil de recherche branché |
| **S2 · Parcours de base** | J8–J14 (→ 24/11) | Publier une annonce, rechercher et voir un détail, demander une réservation |
| **S3 · Intelligence et écrans avancés** | J15–J21 (→ 01/12) | Classement réel, justificatif + risque, avis, messagerie, Copilot |
| **S4 · Finitions** | J22–J30 (→ 10/12) | Traductions/RTL, sombre, tests, sécurité, recette croisée, **gel du code à J28 (08/12)**, démo |

Rythme : point de 15 minutes chaque jour (fait / bloqué / prévu), une revue de jalon à la fin de chaque semaine. Mettez à jour `docs/PROGRESS.md` en fin de session.

## 9. Réalisme : à lire avant de commencer

Le total des 52 issues représente environ **120 jours-personne** pour 3 personnes, soit ~40 jours chacun en 30 jours calendaires (environ 21 jours ouvrés). Ce sont des estimations de ma part, faites sans voir la liste exacte des 95 écrans (le document d'inventaire n'est pas dans le zip) ni vos disponibilités. À temps plein, c'est serré ; avec des cours ou un stage en parallèle, **tout ne tiendra pas**.
D'où les priorités sur chaque issue :
- **p1** (≈ 77 j au total) : le parcours de démo complet. À faire en premier.
- **p2** (≈ 38 j) : justificatif automatisé, risque, avis, messagerie avancée, Copilot.
- **p3** (≈ 6 j) : à sacrifier en premier (écrans secondaires du Copilot, mode sombre).
Faites un point honnête à la fin de S1 : si le retard dépasse 2 jours, décidez ensemble ce que vous coupez (par exemple un Copilot réduit au conseil privé, ou un justificatif vérifié par règles simples sans OCR).

## 10. Points ouverts à trancher
Fournisseur du LLM (S1-14, avant J10) · conservation des justificatifs et seuil de suspension (S2-D1) · quota Copilot et consentement présélectionné au regard de la loi 09-08 (S2-D2) · couleurs de badges en sombre et ombre (S2-D3) · cibles du graphe de navigation (S1-15) · conformité CNDP à vérifier.

## 11. Commandes utiles
```bash
docker compose up --build                 # tout démarrer
docker compose down -v                    # tout arrêter et vider la base (rejoue Flyway)
docker compose logs -f api                # logs de l'API
cd backend && mvn -B verify               # tests backend (JDK 21)
cd android && ./gradlew assembleDebug testDebugUnitTest
cd intelligence && pip install -r requirements.txt && uvicorn main:app --reload
psql -h localhost -U shs shs              # mot de passe : shs_dev
```
Dépannage fréquent : « Plus Jakarta Sans introuvable » → vérifier les 4 `.ttf` dans `res/font` ; l'émulateur n'atteint pas l'API → utiliser `10.0.2.2`, pas `localhost` ; l'API échoue au démarrage → `docker compose down -v` puis relancer ; Gradle bloqué sur une dépendance → figer la version et le noter dans l'issue.
