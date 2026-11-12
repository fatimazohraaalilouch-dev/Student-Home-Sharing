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

## Points ouverts
- Fournisseur du LLM externe (non précisé).
- Couleurs de badges en mode sombre (à relever sur les 5 écrans sombres).
- Ombre « 0/6/20 » approximée par une élévation de 6 dp (à comparer).
- Consentement Copilot : coché par défaut (maquette) à revalider au regard de la loi 09-08 (CNDP).
- Durée de conservation des justificatifs ; seuil de suspension automatique ; quota Copilot.
- Conformité loi 09-08 (CNDP) à vérifier.
- Cibles des boutons du graphe de navigation (docs/05) à confirmer sur les maquettes.
