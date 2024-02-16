-- DROP SCHEMA public;

CREATE SCHEMA public AUTHORIZATION pg_database_owner;

COMMENT ON SCHEMA public IS 'standard public schema';

-- DROP SEQUENCE public.artist_id_seq;

CREATE SEQUENCE public.artist_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.genre_id_seq;

CREATE SEQUENCE public.genre_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.playlist_genre_id_seq;

CREATE SEQUENCE public.playlist_genre_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.playlist_id_seq;

CREATE SEQUENCE public.playlist_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.playlist_songs_id_seq;

CREATE SEQUENCE public.playlist_songs_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.playlist_songs_playlist_id_seq;

CREATE SEQUENCE public.playlist_songs_playlist_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.playlist_songs_song_id_seq;

CREATE SEQUENCE public.playlist_songs_song_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.song_artists_artist_id_seq;

CREATE SEQUENCE public.song_artists_artist_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.song_artists_id_seq;

CREATE SEQUENCE public.song_artists_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.song_artists_song_id_seq;

CREATE SEQUENCE public.song_artists_song_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;
-- DROP SEQUENCE public.song_id_seq;

CREATE SEQUENCE public.song_id_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 2147483647
	START 1
	CACHE 1
	NO CYCLE;-- public.artist definition

-- Drop table

-- DROP TABLE public.artist;

CREATE TABLE public.artist (
	id serial4 NOT NULL,
	spotify_name varchar NULL,
	beatport_name varchar NULL,
	CONSTRAINT artist_pk PRIMARY KEY (id)
);


-- public.genre definition

-- Drop table

-- DROP TABLE public.genre;

CREATE TABLE public.genre (
	id serial4 NOT NULL,
	"name" varchar NULL,
	code varchar NULL,
	status varchar NULL,
	url varchar NULL,
	CONSTRAINT genre_pk PRIMARY KEY (id)
);


-- public.song definition

-- Drop table

-- DROP TABLE public.song;

CREATE TABLE public.song (
	id serial4 NOT NULL,
	beatport_name varchar NULL,
	spotify_id varchar NULL,
	beatport_image_url varchar NULL,
	status varchar NULL,
	spotify_name varchar NULL,
	spotify_image_url varchar NULL,
	CONSTRAINT song_pk PRIMARY KEY (id)
);


-- public.monthly_playlist_songs definition

-- Drop table

-- DROP TABLE public.monthly_playlist_songs;

CREATE TABLE public.monthly_playlist_songs (
	id serial4 NOT NULL,
	status varchar NULL,
	monthly_playlist_id int4 NULL,
	song_id int4 NULL
);


-- public.playlist definition

-- Drop table

-- DROP TABLE public.playlist;

CREATE TABLE public.playlist (
	id serial4 NOT NULL,
	"name" varchar NULL,
	creation_date date NULL,
	modification_date date NULL,
	genre_id int4 NULL,
	CONSTRAINT playlist_pk PRIMARY KEY (id),
	CONSTRAINT playlist_fk FOREIGN KEY (genre_id) REFERENCES public.genre(id)
);


-- public.playlist_songs definition

-- Drop table

-- DROP TABLE public.playlist_songs;

CREATE TABLE public.playlist_songs (
	id serial4 NOT NULL,
	playlist_id int4 NULL,
	song_id int4 NULL,
	status varchar NULL,
	CONSTRAINT playlist_songs_pk PRIMARY KEY (id),
	CONSTRAINT playlist_songs_fk FOREIGN KEY (playlist_id) REFERENCES public.playlist(id),
	CONSTRAINT playlist_songs_fk_1 FOREIGN KEY (song_id) REFERENCES public.song(id)
);


-- public.song_artists definition

-- Drop table

-- DROP TABLE public.song_artists;

CREATE TABLE public.song_artists (
	id serial4 NOT NULL,
	song_id int4 NULL,
	artist_id int4 NULL,
	CONSTRAINT song_artists_pk PRIMARY KEY (id),
	CONSTRAINT song_artists_fk FOREIGN KEY (artist_id) REFERENCES public.artist(id),
	CONSTRAINT song_artists_fk_1 FOREIGN KEY (song_id) REFERENCES public.song(id)
);


-- public.monthly_playlist definition

-- Drop table

-- DROP TABLE public.monthly_playlist;

CREATE TABLE public.monthly_playlist (
	id serial4 NOT NULL,
	"name" varchar NULL,
	creation_date date NULL,
	modification_date date NULL,
	genre_id int4 NULL,
	CONSTRAINT monthly_playlist_pk PRIMARY KEY (id),
	CONSTRAINT monthly_playlist_fk FOREIGN KEY (genre_id) REFERENCES public.genre(id)
);
