DROP DATABASE IF EXISTS "opt-loc";

-- Role: optloc

DROP ROLE IF EXISTS optloc;

CREATE ROLE optloc WITH LOGIN PASSWORD 'optloc';

-- Database: "opt-loc"

CREATE DATABASE "opt-loc"
  WITH OWNER = optloc
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'en_GB.UTF-8'
       LC_CTYPE = 'en_GB.UTF-8'
       CONNECTION LIMIT = -1
       TEMPLATE template0;

ALTER DATABASE "opt-loc"
  SET search_path = "$user", public, tiger;