
-- V3__seed_establishments.sql
-- Insertion des établissements initiaux d'Al Hoceima

INSERT INTO establishment (
    name,
    university_name,
    address_text,
    city,
    location
)
VALUES
(
    'École Nationale des Sciences Appliquées d''Al-Hoceima (ENSAH)',
    'Université Abdelmalek Essaâdi',
    'Ctre Ait Youssef Ou Ali 32003',
    'Al Hoceima (Ait Youssef Ou Ali)',
    ST_SetSRID(ST_MakePoint(-3.861684, 35.172465), 4326)::geography
),
(
    'FST : Faculté des Sciences et Techniques_Al Hoceima',
    'Université Abdelmalek Essaâdi',
    'B.P : 34 Ajdir Al Hoceima 32 003, Ctre Ait Youssef Ou Ali',
    'Al Hoceima (Ajdir / Ait Youssef Ou Ali)',
    ST_SetSRID(ST_MakePoint(-3.863819, 35.174510), 4326)::geography
),
(
    'University Campus of Al Hoceima',
    'Université Abdelmalek Essaâdi',
    '42X3+JW, Ait-Kamara',
    'Al Hoceima (Ait Kamara)',
    ST_SetSRID(ST_MakePoint(-3.976600, 35.159951), 4326)::geography
),
(
    'ISPITS AL HOCEIMA (Annexe de l''Institut Supérieur des Professions Infirmières et Techniques de Santé)',
    'Ministère de la Santé (Réseau des ISPITS)',
    'Al Hoceïma',
    'Al Hoceima',
    ST_SetSRID(ST_MakePoint(-3.8875079, 35.1957408), 4326)::geography
);
