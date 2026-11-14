-- Nécessaire pour la contrainte d'exclusion de booking (égalité uuid dans un index GiST).
CREATE EXTENSION IF NOT EXISTS btree_gist;

-- Schéma SHS v2 (doc 02). Écarts : « user » renommée app_user (mot réservé PostgreSQL) ;
-- énumérations en text + CHECK (mapping JPA simple).

CREATE TABLE app_user (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  email text NOT NULL UNIQUE,
  password_hash text NOT NULL,
  full_name text NOT NULL,
  phone text,
  avatar_url text,
  preferred_language text NOT NULL DEFAULT 'fr' CHECK (preferred_language IN ('fr','ar','en')),
  active_space text NOT NULL DEFAULT 'TENANT' CHECK (active_space IN ('TENANT','OWNER')),
  is_active boolean NOT NULL DEFAULT true,
  deleted_at timestamptz,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE user_space (
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  space text NOT NULL CHECK (space IN ('TENANT','OWNER')),
  enabled_at timestamptz NOT NULL DEFAULT now(),
  PRIMARY KEY (user_id, space)
);

CREATE TABLE refresh_token (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  token_hash text NOT NULL,
  expires_at timestamptz NOT NULL,
  revoked_at timestamptz,
  created_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX idx_refresh_token_user ON refresh_token(user_id);

CREATE TABLE establishment (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  name text NOT NULL,
  university_name text,
  address_text text,
  city text NOT NULL,
  location geography(Point,4326) NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX idx_establishment_location ON establishment USING GIST (location);

CREATE TABLE tenant_profile (
  user_id uuid PRIMARY KEY REFERENCES app_user(id) ON DELETE CASCADE,
  establishment_id uuid REFERENCES establishment(id),
  budget_min numeric(10,2),
  budget_max numeric(10,2),
  max_distance_km numeric(5,2),
  preferred_types text[] NOT NULL DEFAULT '{}',
  required_amenities text[] NOT NULL DEFAULT '{}',
  importance_budget int NOT NULL DEFAULT 90 CHECK (importance_budget BETWEEN 0 AND 100),
  importance_distance int NOT NULL DEFAULT 75 CHECK (importance_distance BETWEEN 0 AND 100),
  importance_amenities int NOT NULL DEFAULT 70 CHECK (importance_amenities BETWEEN 0 AND 100),
  importance_rating int NOT NULL DEFAULT 40 CHECK (importance_rating BETWEEN 0 AND 100),
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE notification_preference (
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  key text NOT NULL,
  enabled boolean NOT NULL DEFAULT true,
  PRIMARY KEY (user_id, key)
);

CREATE TABLE amenity (
  code text PRIMARY KEY CHECK (code IN ('WIFI','HOT_WATER','KITCHEN','WASHER','DESK','AIR_CONDITIONING')),
  label_fr text NOT NULL,
  label_ar text,   -- à renseigner d'après les maquettes arabes
  label_en text,   -- provisoire, à aligner sur les écrans
  sort_order int NOT NULL
);
INSERT INTO amenity (code, label_fr, label_en, sort_order) VALUES
  ('WIFI','Wifi','Wi-Fi',1),
  ('HOT_WATER','Eau chaude','Hot water',2),
  ('KITCHEN','Cuisine','Kitchen',3),
  ('WASHER','Machine à laver','Washing machine',4),
  ('DESK','Bureau','Desk',5),
  ('AIR_CONDITIONING','Climatisation','Air conditioning',6);

CREATE TABLE listing (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  owner_id uuid NOT NULL REFERENCES app_user(id),
  title text,
  description text,
  type text CHECK (type IN ('ROOM','STUDIO','APARTMENT','SHARED_ROOM')),
  price_monthly numeric(10,2),
  charges_included boolean,
  deposit_amount numeric(10,2),
  address_text text,
  city text,
  location geography(Point,4326),
  surface_m2 numeric(6,2),
  bedrooms int,
  furnished boolean,
  available_from date,
  status text NOT NULL DEFAULT 'DRAFT' CHECK (status IN ('DRAFT','UNDER_REVIEW','PUBLISHED','ON_HOLD','SUSPENDED')),
  draft_step int NOT NULL DEFAULT 1,
  rating_avg numeric(3,2) NOT NULL DEFAULT 0,
  review_count int NOT NULL DEFAULT 0,
  published_at timestamptz,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX idx_listing_status_price ON listing(status, price_monthly);
CREATE INDEX idx_listing_location ON listing USING GIST (location);
CREATE INDEX idx_listing_owner ON listing(owner_id);

CREATE TABLE listing_amenity (
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  amenity_code text NOT NULL REFERENCES amenity(code),
  PRIMARY KEY (listing_id, amenity_code)
);

CREATE TABLE listing_photo (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  url text NOT NULL,
  position int NOT NULL DEFAULT 0,
  is_cover boolean NOT NULL DEFAULT false,
  phash char(16)
);
CREATE INDEX idx_listing_photo_phash ON listing_photo(phash);

CREATE TABLE owner_proof (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  owner_id uuid NOT NULL REFERENCES app_user(id),
  document_type text NOT NULL CHECK (document_type IN ('TITLE_DEED','PURCHASE_CONTRACT','LEASE_WITH_SUBLET_AUTH','POWER_OF_ATTORNEY')),
  file_ref text NOT NULL,        -- chiffré, jamais exposé par l'API
  file_hash text NOT NULL,
  extracted_name text,
  extracted_address text,
  consistency_score numeric(4,3),
  status text NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING','VERIFIED','TO_COMPLETE','INCONSISTENT')),
  reason text,
  submitted_at timestamptz NOT NULL DEFAULT now(),
  verified_at timestamptz,
  retention_until timestamptz     -- durée de conservation à fixer (loi 09-08)
);
CREATE INDEX idx_owner_proof_hash ON owner_proof(file_hash);
CREATE INDEX idx_owner_proof_listing ON owner_proof(listing_id);

CREATE TABLE trust_score (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  risk_score int NOT NULL CHECK (risk_score BETWEEN 0 AND 100),
  level text NOT NULL CHECK (level IN ('LOW','MEDIUM','HIGH')),
  reasons jsonb NOT NULL DEFAULT '[]',
  computed_at timestamptz NOT NULL DEFAULT now()
);
CREATE INDEX idx_trust_score_listing ON trust_score(listing_id, computed_at DESC);

CREATE TABLE favorite (
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  created_at timestamptz NOT NULL DEFAULT now(),
  PRIMARY KEY (user_id, listing_id)
);
CREATE INDEX idx_favorite_user ON favorite(user_id, created_at DESC);

CREATE TABLE report (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  reporter_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  reason text NOT NULL CHECK (reason IN ('DUPLICATE','SUSPICIOUS_PRICE','FAKE_PHOTOS','PAYMENT_OUTSIDE_PLATFORM','OTHER')),
  details text,
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (listing_id, reporter_id)
);

CREATE SEQUENCE booking_ref_seq;  -- référence lisible SH-AAAA-NNNN construite par l'application

CREATE TABLE booking (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  reference text NOT NULL UNIQUE,
  listing_id uuid NOT NULL REFERENCES listing(id),
  tenant_id uuid NOT NULL REFERENCES app_user(id),
  start_date date NOT NULL,
  end_date date NOT NULL,
  message text,
  status text NOT NULL DEFAULT 'REQUESTED' CHECK (status IN ('REQUESTED','ACCEPTED','REFUSED','CANCELLED','COMPLETED')),
  monthly_price numeric(10,2) NOT NULL,
  service_fee numeric(10,2) NOT NULL DEFAULT 0,
  total_estimated numeric(10,2) NOT NULL,
  cancelled_at timestamptz,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  CHECK (end_date > start_date),
  -- deux réservations ACCEPTED ne se chevauchent pas (arrivée et départ inclus)
  EXCLUDE USING gist (listing_id WITH =, daterange(start_date, end_date, '[]') WITH &&) WHERE (status = 'ACCEPTED')
);
CREATE INDEX idx_booking_tenant ON booking(tenant_id);
CREATE INDEX idx_booking_listing ON booking(listing_id);

CREATE TABLE review (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  booking_id uuid NOT NULL UNIQUE REFERENCES booking(id),
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  author_id uuid NOT NULL REFERENCES app_user(id),
  rating int NOT NULL CHECK (rating BETWEEN 1 AND 5),
  comment text,
  created_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE conversation (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  listing_id uuid NOT NULL REFERENCES listing(id) ON DELETE CASCADE,
  tenant_id uuid NOT NULL REFERENCES app_user(id),
  owner_id uuid NOT NULL REFERENCES app_user(id),
  last_message_at timestamptz,
  created_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (listing_id, tenant_id)
);

CREATE TABLE message (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  conversation_id uuid NOT NULL REFERENCES conversation(id) ON DELETE CASCADE,
  sender_id uuid NOT NULL REFERENCES app_user(id),
  content text NOT NULL,
  sent_at timestamptz NOT NULL DEFAULT now(),
  read_at timestamptz,
  risk_flags text[] NOT NULL DEFAULT '{}'   -- calculé par règles, jamais par le LLM
);
CREATE INDEX idx_message_conversation ON message(conversation_id, sent_at);

CREATE TABLE copilot_consent (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  conversation_id uuid REFERENCES conversation(id) ON DELETE CASCADE,  -- vide = valable partout
  scope text NOT NULL CHECK (scope IN ('CONVERSATION_ANALYSIS','AUTO_SUGGESTIONS')),
  granted_at timestamptz NOT NULL DEFAULT now(),
  revoked_at timestamptz
);
CREATE INDEX idx_copilot_consent_user ON copilot_consent(user_id, scope) WHERE revoked_at IS NULL;

CREATE TABLE copilot_usage (
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  window_start timestamptz NOT NULL,
  request_count int NOT NULL DEFAULT 0,
  PRIMARY KEY (user_id, window_start)
);

CREATE TABLE copilot_log (
  id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
  user_id uuid NOT NULL REFERENCES app_user(id) ON DELETE CASCADE,
  tool_name text NOT NULL,
  status text NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now()  -- sans contenu des messages
);
