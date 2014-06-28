DELETE FROM raw_places_gb WHERE timezone != 'Europe/London';

INSERT INTO public.places_gb(
            name,
            geom,
            feature_class,
            feature_code,
            admin_code,
            admin_code_2,
            admin_code_3,
            population,
            geoname_id)
    (SELECT name,
        ST_SetSRID(ST_MakePoint(latitude, longitude), 4326),
        feature_class,
        feature_code,
        admin_code,
        admin_code_2,
        admin_code_3,
        population,
        geoname_id
    FROM public.raw_places_gb);



