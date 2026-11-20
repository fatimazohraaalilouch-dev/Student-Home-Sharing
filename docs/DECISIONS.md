# DECISIONS

## 2026-11-11 · Session 1 (J1)
- Équipe de 3, lots A (annonces, justificatif) / B (recherche, classement) / C (réservation, messagerie, Copilot). Lot B : Fza.
- Calendrier : J1 = 11/11/2026, J30 = 10/12/2026.
- Périmètre : app Android + backend Spring Boot + service Python (hypothèse retenue, non contredite).
- Stack : stack par défaut du brief ; Plus Jakarta Sans embarquée dans res/font.
- Modèle de données : la v2 (doc 02) remplace la v1.
- Référence de réservation : `SH-2026-0128` (écran, base de données, docs 02 et 04 à corriger).

### Incohérences du doc 04 (section 7), validées
1. Types : 4 valeurs (ROOM, STUDIO, APARTMENT, SHARED_ROOM) ; « Colocation » = libellé de SHARED_ROOM.
2. Équipements : catalogue unique de 6 codes ; « Meublé » reste un booléen.
3. Publication : Informations, Prix, Détails, Localisation, Photos, Récapitulatif.
4. Budget min : filtres seulement ; `budget_min` nullable dans le profil.
5. Mes réservations : s'ouvre depuis Profil.
6. Consentement Copilot : rien n'est enregistré avant « Autoriser » ; présélection = comme la maquette (coché).
7. Frais de service : `service_fee` conservé, fixé à 0.

### Autres décisions
- Annonce publiée : reste « Publiée » pendant le re-contrôle des champs sensibles ; « Suspendue » si le risque devient élevé.
- Design system : couleurs relevées sur la planche (rouge `#B93838` confirmé) ; neutres et badges échantillonnés sur l'image.

### 2026-11-19 · S1-15 (Fza) : cibles du graphe de navigation, vérifiées sur les 104 maquettes
Décisions prises par Fza (Lot B) d'après les usages courants des applications de réservation et les contraintes du projet. À relire par Asmae et Zineb dans la PR.
8. Types : « Appartement » ajouté dans Profil 2 et Filtres ; libellé « Colocation » aussi côté propriétaire (application de la décision n°1). Maquettes à corriger.
9. Équipements : les 6 codes et les libellés de `amenity.label_fr` sur tous les écrans, donc « Eau chaude » et « Bureau » ajoutés où ils manquent, « Machine à laver » partout (application de la décision n°2).
10. Rôle « Propriétaire » à l'inscription : active l'espace OWNER (`POST /me/spaces`), saute le profil locataire et arrive sur « Mes annonces ». Le profil locataire est demandé à la première bascule vers l'espace locataire.
11. Profil locataire 1 à 3 : à la première configuration, barre d'onglets masquée, flèche de retour et pas de « Passer » ; depuis Profil > Mes préférences, mêmes écrans avec barre visible et « Enregistrer ».
12. « Passer » (Découverte) mène à Inscription. Suppression de compte : retour à Découverte 1, session effacée. Déconnexion : retour à Connexion.
13. Pas de mode invité en v1 : « Explorer sans compte » est retiré de l'écran Session expirée (les routes de recherche exigent un JWT et le classement dépend du profil locataire).
14. Consentement Copilot : proposé uniquement au moment de l'usage, depuis le Conseil privé de la messagerie ; gestion ensuite dans Réglages (complète la décision n°6).
15. « Proposer d'autres dates » (propriétaire) : pas de contre-offre en v1 ; le bouton ouvre la conversation avec un message prérempli (aucun nouveau statut de réservation).
16. Alternatives proches : affichées quand la recherche stricte est vide mais qu'un assouplissement limité du budget donne des résultats ; sinon « Aucun résultat ». Le budget du profil ne change jamais sans accord (S3-B1).
17. Notifications : 5 clés `messages`, `booking_updates`, `listing_updates`, `copilot_tips`, `news` ; `copilot_tips` et `news` désactivées par défaut (maquette), les trois autres activées. L'API fournit ces valeurs quand aucune ligne n'existe ; la table garde `DEFAULT true`.
18. Écrans à créer, sans maquette : Mot de passe oublié (p2 : saisie de l'e-mail puis confirmation neutre), Galerie photos (p3), Apparence (p3, avec S4-02).
19. Consentement Copilot présélectionné comme la maquette (« Analyse de la conversation » activée) : la décision n°6 est confirmée. Rien n'est enregistré avant « Autoriser ». La revalidation au regard de la loi 09-08 reste en S2-D2.
20. Réglages · Consentements Copilot garde ses 3 lignes (Préférences de recherche, Annonces et favoris, Conversation sélectionnée). Le contrat actuel n'a que 2 portées (`CONVERSATION_ANALYSIS`, `AUTO_SUGGESTIONS`) et la contrainte CHECK de `copilot_consent` (V2, non modifiable) les limite à ces 2 valeurs : la 3e portée et le rattachement des 3 lignes aux portées se font en S3-C1, avec une nouvelle migration (V4) et la mise à jour de `ConsentScope`. Pas de changement de contrat en S1.

## Points ouverts
- Fournisseur du LLM externe (non précisé).
- Couleurs de badges en mode sombre (à relever sur les 5 écrans sombres).
- Ombre « 0/6/20 » approximée par une élévation de 6 dp (à comparer).
- Consentement Copilot : coché par défaut (maquette, décisions 6 et 19) à revalider au regard de la loi 09-08 (CNDP), S2-D2.
- Durée de conservation des justificatifs ; seuil de suspension automatique ; quota Copilot.
- Conformité loi 09-08 (CNDP) à vérifier.
- Consentements : 3 lignes à l'écran (décision 20) contre 2 portées dans l'API ; migration V4 et `ConsentScope` à faire en S3-C1.
- Navigation : entrée dans l'espace propriétaire à l'ouverture de l'app (dernier espace actif ?), sous-écrans de « Modifier l'annonce », permission caméra pour la photo de profil.
