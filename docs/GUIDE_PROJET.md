# Guide complet du projet Student Home Sharing (SHS)

Application Android de logement étudiant à Al Hoceïma. Équipe de 3 : **Asmae** (lot A, `asmaaaa01ab`), **Fatima** (lot B, `fatimazohraaalilouch-dev`), **Zineb** (lot C, `zinebs-dev`). J1 = 11/11/2026, J30 = 10/12/2026.
Ce guide et les 52 issues (`docs/ISSUES.md`, créées sur GitHub) suffisent pour installer, lancer et réaliser le projet sans blocage. Lisez-le en entier une fois, puis utilisez les recettes (§8) et la table des passages de relais (§11).

## 0. À lire en premier : ce qui a été vérifié et corrigé

Une relecture complète du squelette a trouvé des défauts. Ils sont corrigés dans les fichiers fournis avec ce guide : **vérifiez que votre dépôt les contient** (sinon `git pull`, ou copiez les fichiers).

| Problème trouvé | Conséquence | Correction |
|---|---|---|
| `V2__schema.sql` **absent** du premier zip | la base n'avait aucune table | fichier ajouté dans `backend/src/main/resources/db/migration/` |
| V2 échouait : l'index d'exclusion des réservations exige l'extension `btree_gist` | migration en erreur au démarrage | `CREATE EXTENSION IF NOT EXISTS btree_gist;` en tête de V2 |
| pas de `.gitattributes` | sous Windows, `gradlew` peut avoir des fins de ligne CRLF et casser la CI Linux | `.gitattributes` ajouté (`eol=lf`) |
| `gradlew` sans droit d'exécution (Git sous Windows) | `CI / android` rouge en 12 s | `git update-index --chmod=+x android/gradlew` (issue S1-02) |
| `BearerInterceptor` n'envoie aucun jeton sur `/auth/*`, `/auth/logout` compris | impossible d'identifier l'utilisateur à la déconnexion | à corriger dans l'issue S1-12 |
| schéma `ListingDetail` vide dans `openapi.yaml` | détail d'annonce sans contrat | à compléter dans la PR d'ouverture de S2-A1 (18/11) |

**Ce qui a été testé** : V1 + V2 appliqués sur PostgreSQL 16 + PostGIS 3.4 (21 tables créées, le chevauchement de deux réservations acceptées est bien refusé) ; la CI du dépôt passe pour `backend` et `intelligence`. **Ce qui n'a jamais été testé** : la compilation Android, le démarrage complet via Docker, toutes les routes de l'API (il n'y a presque aucun code métier encore). Un échec à ces étapes est normal et sera corrigé dans S1-02 et S1-03.

## 1. Le produit en bref
- **Deux espaces** dans une même app : Locataire (5 destinations : Explorer, Favoris, Copilot, Messages, Profil) et Propriétaire (4 : Mes annonces, Demandes, Messages, Profil). Un utilisateur peut basculer de l'un à l'autre.
- **Parcours locataire** : inscription → profil de recherche (établissement, budget, distance, types, équipements, importance de chaque critère) → résultats classés avec % de compatibilité → détail → demande de réservation → suivi → avis.
- **Parcours propriétaire** : publication en 6 étapes (Informations, Prix, Détails, Localisation, Photos, Récapitulatif) → soumission → justificatif → vérification automatique → annonce publiée ou en attente → traitement des demandes.
- **Copilot** : conseiller du locataire (recherche, comparaison de 3 logements, fiabilité, conseil privé dans la messagerie). Il propose, n'envoie jamais rien tout seul, et n'analyse une conversation qu'avec consentement.
- **Décisions du projet** (`docs/DECISIONS.md`) : pas de rôle administrateur ; justificatif obligatoire ; frais de service conservés mais fixés à 0 ; langues FR/AR/EN avec RTL ; montants en MAD ; types d'annonce `ROOM|STUDIO|APARTMENT|SHARED_ROOM` (« Colocation » = `SHARED_ROOM`) ; 6 équipements ; référence de réservation `SH-AAAA-NNNN`.

## 2. Prérequis

| Outil | Version | Pourquoi |
|---|---|---|
| Git | 2.40 ou plus récent | versionnement (Git Bash sous Windows) |
| Compte GitHub + GitHub CLI `gh` | `gh` 2.x | dépôt, issues, pull requests |
| Docker Desktop (WSL2 sous Windows) | récent, 8 Go de RAM conseillés | base PostGIS, API, service Python, tests Testcontainers |
| JDK | **21** | backend Spring Boot 3.3.5 hors Docker |
| Android Studio | **Ladybug 2024.2.1 ou plus récent** | requis par le plugin Android 8.7.2 |
| SDK Android | plateforme **35** + Build-Tools ; émulateur API 35 | `compileSdk`/`targetSdk` 35, `minSdk` 26 |
| Python | 3.12 (facultatif en local) | service d'intelligence et ses tests |
| Bruno ou Postman | récent | tester l'API, importer `docs/openapi.yaml` |
| DBeaver ou `psql` | — | inspecter la base (`localhost:5432`, base et utilisateur `shs`, mot de passe de dev `shs_dev`) |
| Mermaid (extension VS Code ou mermaid.live) | — | lire `docs/*.mmd` |

Versions du projet : Gradle 8.10.2 (wrapper), Kotlin 2.0.21, KSP 2.0.21-1.0.28, Hilt 2.52, Retrofit 2.11, OkHttp 4.12, Room 2.6.1, Coil 2.7, osmdroid 6.1.20. Android Studio utilise son propre JDK 17 pour Gradle.

**Vérification de poste** (Git Bash) :
```
git --version ; java -version ; docker --version ; docker compose version ; gh auth status
```
Matériel : 16 Go de RAM conseillés (émulateur + Docker + IDE simultanés).

## 3. Mise en route

Le dépôt existe : `https://github.com/fatimazohraaalilouch-dev/Student-Home-Sharing`. Branche commune : **`main`** (protégée, voir S1-01). Les branches `m1-branch`, `m2-branch`, `m3-branch` sont les branches personnelles historiques : **le code se fait désormais dans des branches `feature/...`** (§6).

```
# 1. Cloner (une seule fois)
git clone https://github.com/fatimazohraaalilouch-dev/Student-Home-Sharing.git
cd Student-Home-Sharing
git switch main
# 2. Lancer le serveur
docker compose up --build
curl http://localhost:8080/api/v1/actuator/health      # {"status":"UP"}
curl http://localhost:8000/health                      # {"status":"ok","simulated":true}
```
**Android** : ouvrir le dossier `android/` dans Android Studio, laisser Gradle synchroniser, lancer sur un émulateur. L'app parle à l'API par `http://10.0.2.2:8080/api/v1/` (adresse de votre machine vue de l'émulateur). Détails et dépannage : issue S1-02.

**Mise à jour des issues GitHub** (une fois, par Fatima) : les 52 issues existent avec un texte court ; pour y mettre les versions détaillées de ce guide :
```
git pull
bash scripts/update_issues.sh
```
Le script remplace uniquement le corps des issues (titres, assignés, labels inchangés) et peut être relancé sans risque.

## 4. Structure du dépôt
```
.
├─ android/            Kotlin, vues XML, ViewBinding, MVVM, Hilt, Retrofit, Room, osmdroid
│  └─ app/src/main/{java/ma/shs/app/{ui,data,di}, res/{values,values-ar,values-en,values-night,font,layout,navigation,drawable}}
├─ backend/            Spring Boot 3.3 (Java 21) : JPA, Flyway, Spring Security
│  └─ src/main/{java/ma/shs/api, resources/{application.yml, db/migration}}
├─ intelligence/       FastAPI : classement, risque, justificatif, Copilot
├─ docs/               openapi.yaml (contrat), 15 diagrammes .mmd, DECISIONS.md, PROGRESS.md, ISSUES.md, ce guide
├─ scripts/            create_issues.sh, update_issues.sh
├─ .github/            CI, modèle de PR, modèle d'issue « écart maquette », corps des issues
└─ docker-compose.yml  db (PostGIS 16) + api (:8080) + intelligence (:8000)
```

## 5. Architecture
- **Android** (docs/02) : Fragment + layout XML → ViewModel (`StateFlow<UiState>`) → Repository → Retrofit/OkHttp (intercepteur Bearer + `Authenticator` de renouvellement) / Room (brouillons de messages) / EncryptedSharedPreferences (jetons). Navigation Component, injection Hilt.
- **API** (docs/03) : filtre JWT → contrôleurs `/api/v1` → services métier → repositories → PostgreSQL + PostGIS. Tâches planifiées (réservation « Terminée », purge). Seule l'API parle à la base.
- **Service intelligence** : appelé **uniquement par l'API** sur `/internal/*` (rank, trust, proof/verify, copilot/run) ; jamais par l'app, jamais exposé publiquement.
- **Données** (`V2__schema.sql`, docs/04) : 21 tables. Points clés : `app_user`, `listing` (+ `location geography`), `booking` (contrainte d'exclusion anti-chevauchement), `owner_proof` (fichier chiffré, jamais exposé), `trust_score`, `message.risk_flags` (calculés par règles), `copilot_consent/usage/log` (sans contenu des messages).
- **États** : annonce (docs/06), justificatif (docs/07), réservation (docs/08). **Séquences** : connexion (10), publication + justificatif (11), recherche + classement (12), réservation en conflit (13), Copilot + consentement (14).

### Règles d'or
1. **OpenAPI d'abord** : le contrat est `docs/openapi.yaml`. Changer une route ou un champ = modifier ce fichier dans la même PR **et** prévenir l'équipe.
2. **La maquette fait foi.** Un écran XML est reproduit fidèlement. Si une maquette contredit un document ou une règle, on ouvre une issue « Écart maquette » ; **personne ne tranche seul**.
3. **Le LLM ne décide rien d'important** : signaux d'alerte, risque, vérification du justificatif sont calculés par des règles ; le Copilot propose, l'utilisateur décide.
4. **Aucun secret ni donnée personnelle** dans le code, les logs ou le dépôt.
5. **Une issue = une branche = une PR**, la plus petite possible.

## 6. Travailler avec Git, pas à pas

**Au quotidien** (chaque début de journée) :
```
git switch main
git pull
```
**Pour chaque issue** (exemple S2-B1) :
```
git switch -c feature/S2-B1-recherche      # toujours depuis main à jour
# ... coder, tester ...
git status                                  # voir ce qui a changé
git add backend/src/main/java/...           # ou git add . après avoir lu git status
git commit -m "feat: recherche d'annonces avec filtres PostGIS"
git push -u origin feature/S2-B1-recherche
```
Sur GitHub : **Compare & pull request** → base `main` → description : `closes #<numéro de l'issue>` + cases du modèle cochées + « Test effectué ». Demander une relecture à un autre membre. CI verte. **Squash and merge**, puis *Delete branch*.
Après la fusion : `git switch main && git pull` et supprimez la branche locale : `git branch -D feature/S2-B1-recherche`.

**Messages de commit** : `feat:` (nouveauté), `fix:` (correction), `docs:`, `test:`, `chore:` + phrase courte en français.

**Relire une PR** : lire la liste des fichiers modifiés ; vérifier la case « Définition de terminé » ; lancer la branche en local si le changement est visible (`git fetch && git switch feature/...`) ; commenter précisément (ligne + suggestion). Approuver seulement si vous seriez à l'aise de maintenir ce code.

**Conflits de fusion** (deux personnes ont modifié la même ligne) :
```
git switch feature/ma-branche
git fetch
git merge origin/main            # Git signale les fichiers en conflit
# ouvrir chaque fichier : garder la bonne version entre <<<<<<< et >>>>>>>, supprimer les marqueurs
git add <fichiers résolus>
git commit
git push
```
Au moindre doute sur un conflit dans un fichier d'un autre lot : demandez à son auteur, ne devinez pas.

**Annuler une erreur** : fichier modifié par erreur → `git restore <fichier>` ; dernier commit pas encore poussé à refaire → `git reset --soft HEAD~1` ; **jamais** `git push --force` sur `main`.

**Éviter les conflits** (zones sensibles) :
- Navigation Android : un graphe par lot (`nav_auth`, `nav_tenant`, `nav_owner`, `nav_copilot`, inclus dans `nav_graph.xml`) ; ne modifiez pas celui d'un autre.
- Textes : un fichier `strings_<fonction>.xml` par fonctionnalité, dans `values/` (français par défaut), `values-ar/`, `values-en/`.
- Migrations Flyway : **ne jamais modifier** une migration déjà fusionnée ; plages réservées : **V3–V9 communes, V10–V19 lot A, V20–V29 lot B, V30–V39 lot C**.
- `docs/openapi.yaml` : changements dans des PR courtes et dédiées.

**Si la CI est rouge** : ouvrir le contrôle en échec sur la PR, lire la dernière erreur de l'étape rouge, corriger, repousser. `backend` rouge : lancer `cd backend && mvn -B verify` en local. `android` rouge : `cd android && ./gradlew assembleDebug testDebugUnitTest`. `intelligence` rouge : `cd intelligence && pip install -r requirements.txt && python -c "import main"`.

## 7. Conventions

**Nommage**
- Java : paquets `ma.shs.api.<domaine>` (`auth`, `user`, `tenant`, `listing`, `booking`, `messaging`, `copilot`, `common`) ; classes `XxxController`, `XxxService`, `XxxRepository`, DTO en `record`.
- Kotlin : paquets `ma.shs.app.ui.<fonction>`, `ma.shs.app.data.<fonction>` ; `XxxFragment`, `XxxViewModel`, `XxxRepository`, `XxxApi`.
- XML : `fragment_<ecran>.xml`, `item_<nom>.xml`, `view_<nom>.xml` ; identifiants de vues en `snake_case` préfixés (`btn_`, `et_`, `tv_`, `iv_`, `rv_`).
- JSON : **camelCase**, comme l'OpenAPI. Dates ISO 8601 en UTC, montants en MAD.

**Android** : aucune couleur, taille ni police en dur ; `start/end` (jamais `left/right`) ; textes FR/AR/EN ; aperçu RTL ; zones tactiles ≥ `@dimen/touch_target_min` ; `contentDescription` sur les images ; ViewBinding ; **aucune logique dans les Fragments** (ViewModel + Repository).
**Backend** : erreurs `{code, message, details}` (`ApiException`) ; validation par annotations ; accès : l'utilisateur ne voit que ses ressources ; transactions pour toute règle métier multiple ; tests d'intégration avec Testcontainers.
**Sécurité** : jamais de mot de passe, jeton, e-mail, contenu de message ou texte de justificatif dans les logs ; secrets par variables d'environnement.

## 8. Recettes

### 8.1 Un endpoint Spring Boot complet
Exemple : `PUT /favorites/{listingId}` (idempotent). Même schéma pour toute route.

```java
// favorite/Favorite.java : l'entité reflète EXACTEMENT la table (ddl-auto=validate)
@Entity @Table(name = "favorite") @IdClass(FavoriteId.class)
public class Favorite {
  @Id @Column(name = "user_id") private UUID userId;
  @Id @Column(name = "listing_id") private UUID listingId;
  @Column(name = "created_at", insertable = false, updatable = false) private Instant createdAt;
}

// favorite/FavoriteService.java
@Service @RequiredArgsConstructor  // ou constructeur explicite
public class FavoriteService {
  private final NamedParameterJdbcTemplate jdbc;
  @Transactional
  public void add(UUID userId, UUID listingId) {
    jdbc.update("INSERT INTO favorite(user_id, listing_id) VALUES (:u, :l) ON CONFLICT DO NOTHING",
                Map.of("u", userId, "l", listingId));
  }
}

// favorite/FavoriteController.java
@RestController @RequestMapping("/favorites")        // le préfixe /api/v1 vient de application.yml
public class FavoriteController {
  @PutMapping("/{listingId}") @ResponseStatus(HttpStatus.NO_CONTENT)
  public void add(@PathVariable UUID listingId) { service.add(CurrentUser.id(), listingId); }
}
```
Règles :
- **Utilisateur connecté** : `CurrentUser.id()` lit l'`Authentication` (UUID placé par le filtre JWT, S1-05).
- **Erreurs** : `throw new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", "Annonce introuvable")`. Codes autorisés : ceux de `components/schemas/Error` (+ `INVALID_STATE`, `LISTING_HAS_UPCOMING_BOOKING` ajoutés en S2-A1).
- **Validation** : DTO en `record` avec `@NotNull`, `@Size`, `@Min`... et `@Valid` sur le paramètre : l'erreur devient `VALIDATION_ERROR`.
- **Mapping JPA ↔ base** : `@Enumerated(EnumType.STRING)` pour les colonnes `text` + `CHECK` ; `BigDecimal` pour `numeric` ; `Instant` pour `timestamptz` ; `text[]` : `@JdbcTypeCode(SqlTypes.ARRAY) @Column(columnDefinition="text[]") List<String>` ; `jsonb` : `@JdbcTypeCode(SqlTypes.JSON)` ; `uuid` : `@GeneratedValue(strategy = GenerationType.UUID)`.
- **PostGIS** : ne **mappez pas** `geography` avec JPA. Lire/écrire avec `NamedParameterJdbcTemplate` :
  `ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography` (**longitude d'abord**), `ST_Y(location::geometry)` = latitude, `ST_X(...)` = longitude, `ST_DWithin`/`ST_Distance` en **mètres**.
- **Appeler le service Python** (`RestClient` de Spring 6.1) :
  ```java
  RestClient rc = RestClient.builder().baseUrl(intelligenceUrl).build();   // @Value("${shs.intelligence-url}")
  RankResponse r = rc.post().uri("/internal/rank").body(req).retrieve().body(RankResponse.class);
  ```
  Toujours avec un délai d'attente et un **repli** si le service ne répond pas.

### 8.2 Un écran XML fidèle à la maquette
1. Ouvrir la maquette (FR, AR, sombre si elle existe). Noter structure, marges, composants, textes, états.
2. Créer `res/layout/fragment_<ecran>.xml` avec **uniquement** des ressources du design system (§8.6) :
   ```xml
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android" xmlns:app="http://schemas.android.com/apk/res-auto"
       android:layout_width="match_parent" android:layout_height="match_parent"
       android:orientation="vertical" android:padding="@dimen/screen_margin"
       android:background="@color/shs_background">
     <TextView android:layout_width="wrap_content" android:layout_height="wrap_content"
         android:text="@string/login_title" android:textAppearance="@style/TextAppearance.Shs.ScreenTitle" />
     <com.google.android.material.textfield.TextInputLayout style="@style/Widget.Shs.TextInput"
         android:layout_width="match_parent" android:layout_height="wrap_content"
         android:layout_marginTop="@dimen/space_16" android:hint="@string/login_email">
       <com.google.android.material.textfield.TextInputEditText android:id="@+id/et_email"
           android:layout_width="match_parent" android:layout_height="wrap_content" android:inputType="textEmailAddress" />
     </com.google.android.material.textfield.TextInputLayout>
     <com.google.android.material.button.MaterialButton android:id="@+id/btn_login" style="@style/Widget.Shs.Button.Primary"
         android:layout_width="match_parent" android:layout_height="@dimen/button_height"
         android:layout_marginTop="@dimen/space_24" android:text="@string/login_submit" />
   </LinearLayout>
   ```
3. **Textes** : une entrée par langue — `values/strings_auth.xml` (français), `values-ar/strings_auth.xml`, `values-en/strings_auth.xml`, même `name`.
4. **RTL** : `android:layout_marginStart`, `paddingEnd`, `gravity="start"`... ; vérifier avec *Layout Direction > RTL* dans l'éditeur de Layout, puis sur l'émulateur en arabe. Icônes directionnelles : `android:autoMirrored="true"`.
5. **Accessibilité** : `contentDescription` sur images et boutons-icônes ; zones tactiles ≥ `@dimen/touch_target_min`.
6. **Comparer** à la maquette (capture côte à côte dans la PR). Écart → issue « Écart maquette ».

### 8.3 Un écran branché à l'API (Fragment, ViewModel, Repository)
```kotlin
// data/search/SearchApi.kt
interface SearchApi { @GET("listings/search") suspend fun search(@Query("budgetMax") budgetMax: Double?, @Query("page") page: Int): SearchResult }

// data/search/SearchRepository.kt : interface + implémentation réelle + implémentation factice
interface SearchRepository { suspend fun search(budgetMax: Double?, page: Int): Result<SearchResult> }
class RemoteSearchRepository @Inject constructor(private val api: SearchApi) : SearchRepository {
  override suspend fun search(budgetMax: Double?, page: Int) = safeApiCall { api.search(budgetMax, page) }   // safeApiCall : S1-08
}
class FakeSearchRepository : SearchRepository { /* renvoie des données codées en dur conformes à l'OpenAPI */ }

// ui/search/SearchViewModel.kt
@HiltViewModel
class SearchViewModel @Inject constructor(private val repo: SearchRepository) : ViewModel() {
  private val _state = MutableStateFlow<UiState<SearchResult>>(UiState.Loading)
  val state: StateFlow<UiState<SearchResult>> = _state
  fun load(budgetMax: Double?) = viewModelScope.launch {
    _state.value = UiState.Loading
    repo.search(budgetMax, 0).fold(
      onSuccess = { _state.value = if (it.items.isEmpty()) UiState.Empty else UiState.Content(it) },
      onFailure = { _state.value = UiState.Error(it.toUiError()) })
  }
}

// ui/search/ResultsFragment.kt
@AndroidEntryPoint
class ResultsFragment : Fragment(R.layout.fragment_results) {
  private var _b: FragmentResultsBinding? = null ; private val b get() = _b!!
  private val vm: SearchViewModel by viewModels()
  override fun onViewCreated(v: View, s: Bundle?) {
    _b = FragmentResultsBinding.bind(v)
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) { vm.state.collect(::render) }
    }
    vm.load(null)
  }
  private fun render(s: UiState<SearchResult>) { /* Loading / Content / Empty / Error → affiche la bonne vue */ }
  override fun onDestroyView() { _b = null; super.onDestroyView() }
}
```
- Fournir les dépendances dans un module Hilt (`@Binds` du repository, `@Provides` de l'API : `retrofit.create(SearchApi::class.java)`).
- **Travailler avant la fusion du backend** : commencer avec `FakeSearchRepository` (données conformes à `docs/openapi.yaml`), basculer vers `RemoteSearchRepository` dès que la route est fusionnée. Personne n'attend personne pour écrire un écran.
- Modèles Kotlin = schémas de l'OpenAPI, mêmes noms **camelCase** (Gson).

### 8.4 Une route du service Python
```python
# intelligence/main.py (ou un module importé)
class RankIn(BaseModel):
    candidates: list[Candidate]
    profile: Profile

@app.post("/internal/rank")
def rank(body: RankIn):
    return {"items": [score(c, body.profile) for c in body.candidates]}   # fonction pure, testable

# intelligence/tests/test_rank.py
def test_in_budget_scores_full():
    assert score(Candidate(price=1000, ...), Profile(budgetMax=1200, ...))["compatibilityPercent"] >= 90
```
Règles : fonctions pures et déterministes ; Pydantic pour valider les entrées ; `pytest` dans la CI (job `intelligence`) ; aucune donnée personnelle journalisée ; versions figées dans `requirements.txt`.

### 8.5 Tests
- **Backend** : classe de base `IntegrationTestBase` (S1-07, Testcontainers + image `postgis/postgis:16-3.4`) ; un test par route : succès + un cas d'erreur ; ces tests valident aussi les migrations à chaque PR. `cd backend && mvn -B verify`.
- **Android** : tests unitaires de ViewModel avec `kotlinx-coroutines-test` (`runTest`, `StandardTestDispatcher`) et MockK ; `./gradlew testDebugUnitTest`.
- **Python** : `pytest`.
- **Manuel** : décrire dans la PR ce que vous avez cliqué ou envoyé (Bruno), avec capture si c'est visible.

### 8.6 Le design system (ressources déjà présentes)
- **Couleurs** `@color/…` : `shs_teal`, `shs_teal_accent`, `shs_amber`, `shs_background`, `shs_surface`, `shs_ok`, `shs_danger`, `shs_draft`, `shs_on_background`, `shs_on_surface_variant`, `shs_on_primary`, `shs_outline`, `shs_disabled_container`, `shs_copilot_start`, `shs_copilot_end`, badges `shs_badge_{ok,warn,danger,neutral,done}_{bg,text}`. Variantes sombres dans `values-night/colors.xml`.
- **Dimensions** `@dimen/…` : `space_4/8/12/16/24/32`, `screen_margin`, `touch_target_min`, `button_height`, `radius_card/field/pill`, `elevation_card`, `text_screen_title`, `text_section_title`, `text_body`, `text_label` (+ `_line`).
- **Styles** : boutons `Widget.Shs.Button.Primary|Outlined|Text|Copilot` (sur `MaterialButton`), champs `Widget.Shs.TextInput` (sur `TextInputLayout`), cartes `Widget.Shs.Card` (sur `MaterialCardView`), badges `Widget.Shs.Badge.Ok|Warn|Danger|Neutral|Done` (sur `Chip`) ; textes `TextAppearance.Shs.ScreenTitle|SectionTitle|Body|Label` ; thème `Theme.Shs`. Police Plus Jakarta Sans déjà appliquée par ces styles.
- **Ressource manquante** ? Ne l'inventez pas dans un layout : ajoutez-la au design system dans une PR séparée et prévenez l'équipe.

## 9. Répartition

| Lot | Responsable | Périmètre |
|---|---|---|
| **A** | Asmae (`asmaaaa01ab`) | authentification (backend + écrans), publication d'annonce, photos, justificatif, indice de risque, gestion des annonces, décisions S2-D1 et S2-D3 |
| **B** | Fatima (`fatimazohraaalilouch-dev`) | profil locataire, recherche, classement, détail, favoris, carte, avis, signalements, alertes de messagerie |
| **C** | Zineb (`zinebs-dev`) | socle Android, erreurs API et tests, réservations, messagerie, Copilot, conformité, décisions S1-14 et S2-D2 |

Tâches communes : environnement (S1-04), traductions et RTL (S4-01), sombre (S4-02), recette croisée (S4-07), gel et démo (S4-08).

## 10. Planning

| Jalon | Dates | Objectif |
|---|---|---|
| **S1 · Fondations** | J1–J7 (→ 17/11) | tout compile et démarre ; authentification complète ; premiers écrans XML ; profil de recherche branché |
| **S2 · Parcours de base** | J8–J14 (→ 24/11) | publier une annonce, rechercher et voir un détail, demander une réservation |
| **S3 · Intelligence et écrans avancés** | J15–J21 (→ 01/12) | classement réel, justificatif + risque, avis, messagerie, Copilot |
| **S4 · Finitions** | J22–J30 (→ 10/12) | traductions/RTL, sombre, tests, sécurité, recette croisée, **gel du code à J28 (08/12)**, démo |

Rythme : point de 15 minutes chaque jour (fait / bloqué / prévu), revue de jalon en fin de semaine, mise à jour de `docs/PROGRESS.md`.

## 11. Passages de relais : qui attend quoi

| Livrable | Auteur | Attendu par | Date visée | Si ce n'est pas encore fusionné |
|---|---|---|---|---|
| Build Android OK (S1-02), pile Docker OK (S1-03) | Fatima, Zineb | tous | 14/11 | pas de contournement : à traiter en premier |
| `ApiException` + gestion d'erreurs + base de tests (S1-07) | Zineb | Asmae (S1-05), Fatima (S1-06) | 14/11 | Asmae crée la classe avec la signature du guide (S1-07, étape 1) |
| Authentification JWT (S1-05) | Asmae | tous (routes authentifiées) | 15/11 | écrire et tester avec un utilisateur simulé (`authentication(...)` en test) ; côté Android, repositories *fake* (§8.3) |
| Socle Android : graphes, `UiState`, `safeApiCall`, textes par lot (S1-08) | Zineb | Fatima, Asmae (écrans) | 15/11 | écrire les layouts XML (ne dépendent de rien), brancher plus tard |
| PR « contrat » : `ListingDetail`, nouveaux codes d'erreur, `/media` (S2-A1, S2-A2) | Asmae | Fatima (S2-B2), Zineb | 18/11 | Fatima/Zineb proposent le schéma dans la PR de Asmae |
| Annonces publiées de test (seed dev, S2-B1) | Fatima | Zineb (réservations), Asmae | 19/11 | chacun peut créer ses annonces par l'API |
| Réservations `COMPLETED` (S2-C1/C2) | Zineb | Fatima (avis, S3-B2) | 22/11 | forcer le statut en base pour tester |
| Indice de risque (S3-A3) | Asmae | Fatima (classement S3-B1) | 26/11 | traiter les annonces sans `trust_score` comme `LOW` |
| Fournisseur LLM choisi (S1-14) | Zineb | S3-C2 | 20/11 | `MockLlmClient` : aucune clé nécessaire pour développer |

**Règle anti-blocage** : si vous attendez quelqu'un plus d'une demi-journée, (1) écrivez-le en commentaire de l'issue **et** dans le canal de l'équipe ; (2) avancez sur la partie qui ne dépend pas de lui (écrans XML, tests, repositories *fake*) ; (3) si ça dure, mettez-vous à deux en partage d'écran.

## 12. Réalisme et priorités
Le total des 52 issues représente environ **120 jours-personne** pour 3 personnes, soit ~40 jours chacune en 30 jours calendaires (~21 jours ouvrés). Ce sont des estimations faites sans voir la liste exacte des 95 écrans ni vos disponibilités : **à temps plein c'est serré ; avec des cours en parallèle, tout ne tiendra pas.**
- **p1** (≈ 77 j) : le parcours de démo complet. À faire en premier.
- **p2** (≈ 38 j) : justificatif automatisé, risque, avis, messagerie avancée, Copilot.
- **p3** (≈ 6 j) : à sacrifier en premier (écrans secondaires du Copilot, mode sombre, couleurs de badges).
Point honnête à la fin de S1 : si le retard dépasse 2 jours, décidez ensemble ce que vous coupez (par exemple un Copilot réduit au conseil privé, un justificatif vérifié par règles simples sans OCR).

## 13. Points ouverts
Fournisseur LLM (S1-14, avant le 20/11) · conservation des justificatifs et seuil de suspension (S2-D1) · quota Copilot et consentement présélectionné au regard de la loi 09-08 (S2-D2) · couleurs de badges en sombre et ombre (S2-D3) · cibles du graphe de navigation (S1-15) · conformité CNDP à vérifier · arrondi du total estimé d'une réservation (S2-C1) · architecture du Copilot : l'API exécute les fonctions autorisées au lieu d'un rappel du service Python (S3-C2, écart avec le diagramme 14, à valider).

## 14. Commandes et dépannage

```
docker compose up --build                  # tout démarrer
docker compose down -v                     # tout arrêter ET vider la base (rejoue Flyway)
docker compose logs -f api                 # logs de l'API
docker compose exec db psql -U shs -d shs  # ouvrir la base (\dt = tables, \q = quitter)
cd backend && mvn -B verify                # tests backend (JDK 21, Docker lancé)
cd android && ./gradlew assembleDebug testDebugUnitTest
cd intelligence && pip install -r requirements.txt && uvicorn main:app --reload
```

**Tester la contrainte de réservation** (dans `psql`) :
```sql
INSERT INTO app_user(id,email,password_hash,full_name) VALUES
 ('00000000-0000-0000-0000-000000000001','o@x.ma','h','Owner'),('00000000-0000-0000-0000-000000000002','t@x.ma','h','Tenant');
INSERT INTO listing(id,owner_id,status,price_monthly) VALUES ('00000000-0000-0000-0000-0000000000a1','00000000-0000-0000-0000-000000000001','PUBLISHED',1500);
INSERT INTO booking(reference,listing_id,tenant_id,start_date,end_date,status,monthly_price,total_estimated) VALUES
 ('SH-2026-0001','00000000-0000-0000-0000-0000000000a1','00000000-0000-0000-0000-000000000002','2026-12-01','2027-01-01','ACCEPTED',1500,1500);
INSERT INTO booking(reference,listing_id,tenant_id,start_date,end_date,status,monthly_price,total_estimated) VALUES
 ('SH-2026-0002','00000000-0000-0000-0000-0000000000a1','00000000-0000-0000-0000-000000000002','2026-12-20','2027-02-01','ACCEPTED',1500,1500);
-- la 2e insertion doit échouer : conflicting key value violates exclusion constraint
```

| Symptôme | Cause probable | Remède |
|---|---|---|
| `CI / android` rouge en ~12 s | `gradlew` non exécutable ou fins de ligne CRLF | `git update-index --chmod=+x android/gradlew` ; `git add --renormalize .` ; commit |
| `api` redémarre en boucle | migration en erreur, ou `SHS_JWT_SECRET` vide | `docker compose logs api` ; `docker compose down -v` |
| Migration : `no default operator class ... gist` | `btree_gist` absent | `V2__schema.sql` doit commencer par `CREATE EXTENSION IF NOT EXISTS btree_gist;` |
| `Schema-validation: missing column` au démarrage | entité JPA différente de la table | aligner noms et types sur `V2__schema.sql` |
| Port 5432 déjà utilisé | PostgreSQL installé sur Windows | arrêter le service, ou changer le port **localement** sans commiter |
| L'émulateur n'atteint pas l'API | `localhost` utilisé | utiliser `10.0.2.2` |
| `SDK location not found` | `local.properties` absent | créer `android/local.properties` : `sdk.dir=C\:\\Users\\<vous>\\AppData\\Local\\Android\\Sdk` |
| Gradle ne résout pas une dépendance | version inexistante | corriger la version dans `build.gradle.kts` et le noter dans l'issue |
| Tests backend échouent : « Could not find a valid Docker environment » | Docker non lancé | lancer Docker Desktop |
| `gh issue create` : « Could not resolve to a user » | nom d'utilisateur faux ou invitation non acceptée | vérifier le nom exact et l'acceptation de l'invitation |
| `TLS handshake timeout` | réseau instable | changer de réseau, relancer (les scripts sont relançables) |
| Écran arabe mal aligné | `left/right` ou marge fixe | `start/end` partout, relancer l'aperçu RTL |
| App plante au lancement après ajout d'un écran | Fragment sans `@AndroidEntryPoint`, ou module Hilt manquant | ajouter l'annotation / le `@Binds` |
