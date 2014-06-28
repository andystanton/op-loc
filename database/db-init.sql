-- Table: raw_places_gb

CREATE TABLE public.raw_places_gb
(
  geoname_id integer NOT NULL,
  name character varying(200),
  ascii_name character varying(200),
  alternate_names character varying(8000),
  latitude double precision,
  longitude double precision,
  feature_class character(1),
  feature_code character varying(10),
  country_code character(2),
  country_code_2 character varying(60),
  admin_code character varying(20),
  admin_code_2 character varying(80),
  admin_code_3 character varying(20),
  admin_code_4 character varying(20),
  population bigint,
  elevation integer,
  dem_raw integer,
  timezone character varying(40),
  modification_date date,
  CONSTRAINT pk_raw_places_gb_geoname_id PRIMARY KEY (geoname_id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.raw_places_gb
  OWNER TO optloc;

-- Table: places_gb

CREATE TABLE public.places_gb
(
  id BIGSERIAL,
  name character varying(200),
  alternate_names character varying(8000),
  geom geometry(Point,4326),
  feature_class character(1),
  feature_code character varying(10),
  admin_code character varying(20),
  admin_code_2 character varying(80),
  admin_code_3 character varying(20),
  population bigint,
  geoname_id integer NOT NULL,
  CONSTRAINT pk_places_gb_id PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE public.places_gb
  OWNER TO optloc;

-- Index: idx_places_gb_geom

CREATE INDEX idx_places_gb_geom
  ON public.places_gb
  USING gist
  (geom);

