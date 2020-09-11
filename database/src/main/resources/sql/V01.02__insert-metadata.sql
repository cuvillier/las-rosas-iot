/* THING */
INSERT INTO T_THG_THING_TYPE (THM_READABLE, THM_KIND, THM_MANUFACTURER, THM_MODEL) VALUES ('ADN-ARF8123AA', 'Lora', 'ADENUIS', 'ARF8123AA')
DECLARE @ThingMod BIGINT
SET @ThingMod=SCOPE_IDENTITY()

INSERT INTO T_THG_THING (THG_FK_THM_MODEL, THG_DISCRIMINATOR, LOR_DEV_EUI, LOR_APP_EUI, LOR_APP_KEY) VALUES (@ThingMod, 'LOR', '0018B20000002309', '0018B24441524632', 'BE0331C63844166093A8A8E3A11A6970')

INSERT INTO T_THG_THING_MODEL (THM_READABLE, THM_KIND, THM_MANUFACTURER, THM_MODEL) VALUES ('ADN-ARF8180BA', 'Lora', 'ADENUIS', 'ARF8180BAB')
SET @ThingMod=SCOPE_IDENTITY()

INSERT INTO T_THG_THING (THG_FK_THM_MODEL, THG_DISCRIMINATOR, LOR_DEV_EUI, LOR_APP_EUI, LOR_APP_KEY) VALUES (@ThingMod, 'LOR', '0018B2200000093C', '0018B254454D5031', 'A2F47FE01562A61E3C88E07E5F6C0E0B')


INSERT INTO T_TSR_PROVIDER (PRO_REFID, PRO_READABLE, PRO_PROTOCOL, PRO_URL, PRO_LOGIN, PRO_PASSWORD)
		VALUES('SIG', 'Réseau LORA des SIG', 'lora', 'https://lora-ns.sig-ge.ch', 'imad_test', '8Qtx-JURA-ucYL-mhdh')
DECLARE @ProSIG BIGINT
SET @ProSIG=SCOPE_IDENTITY()

INSERT INTO T_TSR_PROVIDER (PRO_REFID, PRO_READABLE, PRO_PROTOCOL)
		VALUES('TEST', 'Lora provider de test', 'test')
DECLARE @ProTEST BIGINT
SET @ProTEST=SCOPE_IDENTITY()

INSERT INTO T_TSR_PROVIDER (PRO_REFID, PRO_READABLE, PRO_PROTOCOL)
		VALUES('openweathermap.org', 'openweathermap.org', 'wst')
DECLARE @ProWeather BIGINT
SET @ProWeather=SCOPE_IDENTITY()

/* ONTHOOGY */
INSERT INTO T_TSR_TIME_SERIE_TYPE (TST_REFID, TST_FORMAT, TST_SCHEMA) VALUES ('indoorEnvironmentObserved', 'json', 'IndoorEnvironmentObserved')
DECLARE @IndoorEnvObs BIGINT
SET @IndoorEnvObs=SCOPE_IDENTITY()

INSERT INTO T_TSR_TIME_SERIE_TYPE (TST_REFID, TST_FORMAT, TST_SCHEMA) VALUES ('weatherObserved', 'json', 'WeatherObserved')
DECLARE @WeatherObs BIGINT
SET @WeatherObs=SCOPE_IDENTITY()

/* Weather Station */
insert into T_THG_THING_MODEL (THM_KIND, THM_BEGTIME, THM_MANUFACTURER, THM_MODEL, THM_READABLE) VALUES('WST', SYSDATETIME ( ), 'openweathermap.org', 'API-2.5', 'Weather Sation')
DECLARE @ThingModWst BIGINT
SET @ThingModWst=SCOPE_IDENTITY()

INSERT INTO T_THG_THING (THG_FK_THM_MODEL, THG_BEGTIME, THG_DISCRIMINATOR, WST_STATION_ID, THG_READABLE) VALUES (@ThingModWst, SYSDATETIME ( ), 'WST', 'Geneva', 'Geneva Weather Sation')

/* TEST SENSORS */

INSERT INTO T_THG_THING_MODEL (THM_READABLE, THM_KIND, THM_MANUFACTURER, THM_MODEL) VALUES ('IMAD-TEST001', 'Test', 'IMAD', 'A')
DECLARE @ThingModImadA BIGINT
SET @ThingModImadA=SCOPE_IDENTITY()

INSERT INTO T_THG_THING (THG_FK_THM_MODEL, THG_READABLE, THG_FK_ASS_ASSET, THG_DISCRIMINATOR, TST_SERIAL) VALUES (@ThingModImadA, 'TST-001', @IMMO_BUR7_19, 'TST', '001')
DECLARE @ThingImadA01 BIGINT
SET @ThingImadA01=SCOPE_IDENTITY()

INSERT INTO T_THG_THING (THG_FK_THM_MODEL, THG_READABLE, THG_FK_ASS_ASSET, THG_DISCRIMINATOR, TST_SERIAL) VALUES (@ThingModImadA, 'TST-002', @IMMO_BUR7_19, 'TST', '002')
DECLARE @ThingImadA02 BIGINT
SET @ThingImadA02=SCOPE_IDENTITY()

INSERT INTO T_THG_THING_MODEL (THM_READABLE, THM_KIND, THM_MANUFACTURER, THM_MODEL) VALUES ('IMAD-TEST001', 'Test', 'IMAD', 'B')
DECLARE @ThingModImadB BIGINT
SET @ThingModImadB=SCOPE_IDENTITY()

INSERT INTO T_THG_THING (THG_FK_THM_MODEL, THG_FK_ASS_ASSET, THG_DISCRIMINATOR, TST_SERIAL) VALUES (@ThingModImadB, @IMMO_BUR7_19, 'TST', '003')
DECLARE @ThingImadB01 BIGINT
SET @ThingImadB01=SCOPE_IDENTITY()

/* Test thing data */
INSERT INTO T_TSR_TIME_SERIE (TSR_FK_THG_THING, TSR_FK_TST_TYPE, TSR_FK_PRO_PROVIDER) VALUES (@ThingImadA01, @IndoorEnvObs, @ProTest)
DECLARE @ThingImadA01IndoorEnvObs BIGINT
SET @ThingImadA01IndoorEnvObs=SCOPE_IDENTITY()

INSERT INTO T_TSR_TIME_SERIE_POINT (TSP_FK_TSR_TIME_SERIE, TSP_TIME, TSP_VALUE) VALUES
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:00:00 AM', 5), '{"temperature":20.3,"humidity":40.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:01:00 AM', 5), '{"temperature":21.3,"humidity":41.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:02:00 AM', 5), '{"temperature":22.3,"humidity":42.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:03:00 AM', 5), '{"temperature":23.3,"humidity":43.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:04:00 AM', 5), '{"temperature":24.3,"humidity":44.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:05:00 AM', 5), '{"temperature":25.3,"humidity":45.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:06:00 AM', 5), '{"temperature":26.3,"humidity":46.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:07:00 AM', 5), '{"temperature":27.3,"humidity":47.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:08:00 AM', 5), '{"temperature":28.3,"humidity":48.2}'),
	(@ThingImadA01IndoorEnvObs, convert(datetime,'01-06-18 01:09:00 AM', 5), '{"temperature":29.3,"humidity":49.2}')

INSERT INTO T_TSR_TIME_SERIE (TSR_FK_THG_THING, TSR_FK_TST_TYPE, TSR_FK_PRO_PROVIDER) VALUES (@ThingImadA01, @WeatherObs, @ProTest)
DECLARE @ThingImadA01WeatherObs BIGINT
SET @ThingImadA01WeatherObs=SCOPE_IDENTITY()

INSERT INTO T_TSR_TIME_SERIE_POINT (TSP_FK_TSR_TIME_SERIE, TSP_TIME, TSP_VALUE) VALUES
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:00:00 AM', 5), '{"temperature":10.3,"humidity":60.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:01:00 AM', 5), '{"temperature":11.3,"humidity":61.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:02:00 AM', 5), '{"temperature":12.3,"humidity":62.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:03:00 AM', 5), '{"temperature":13.3,"humidity":63.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:04:00 AM', 5), '{"temperature":14.3,"humidity":64.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:05:00 AM', 5), '{"temperature":15.3,"humidity":65.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:06:00 AM', 5), '{"temperature":16.3,"humidity":66.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:07:00 AM', 5), '{"temperature":17.3,"humidity":67.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:08:00 AM', 5), '{"temperature":18.3,"humidity":68.2}'),
	(@ThingImadA01WeatherObs, convert(datetime,'01-06-18 01:09:00 AM', 5), '{"temperature":19.3,"humidity":69.2}')

INSERT INTO T_TSR_TIME_SERIE (TSR_FK_THG_THING, TSR_FK_TST_TYPE, TSR_FK_PRO_PROVIDER) VALUES (@ThingImadA02, @IndoorEnvObs, @ProTest)
DECLARE @ThingImadA02IndoorEnvObs BIGINT
SET @ThingImadA02IndoorEnvObs=SCOPE_IDENTITY()

INSERT INTO T_TSR_TIME_SERIE_POINT (TSP_FK_TSR_TIME_SERIE, TSP_TIME, TSP_VALUE) VALUES
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:00:00 AM', 5), '{"temperature":30.3,"humidity":50.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:01:00 AM', 5), '{"temperature":31.3,"humidity":51.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:02:00 AM', 5), '{"temperature":32.3,"humidity":52.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:03:00 AM', 5), '{"temperature":33.3,"humidity":53.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:04:00 AM', 5), '{"temperature":34.3,"humidity":54.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:05:00 AM', 5), '{"temperature":35.3,"humidity":55.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:06:00 AM', 5), '{"temperature":36.3,"humidity":56.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:07:00 AM', 5), '{"temperature":37.3,"humidity":57.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:08:00 AM', 5), '{"temperature":38.3,"humidity":58.2}'),
	(@ThingImadA02IndoorEnvObs, convert(datetime,'01-06-18 01:09:00 AM', 5), '{"temperature":39.3,"humidity":59.2}')

/*----- Twins ----------------*/
	
INSERT INTO T_TWI_TWIN_TYPE (TWT_BEGTIME, TWT_REFID) VALUES (convert(datetime,'01-06-18 01:00:00 AM', 5), 'Default')
DECLARE @TWT_DEFAULT BIGINT
SET @TWT_DEFAULT=SCOPE_IDENTITY()

INSERT INTO T_TWI_TWIN (TWI_BEGTIME, TWI_ENDTIME, TWI_DISCRIMINATOR, TWI_FK_TWT_TYPE, TWI_VALUE, TAS_FK_ASS_ASSET)
SELECT ass.ASS_BEGTIME, ass.ASS_ENDTIME, 'ASS', @TWT_DEFAULT, '{}', ass.ASS_TECHID FROM T_ASS_ASSET ass WHERE (SELECT COUNT(*) FROM T_TWI_TWIN twi WHERE twi.TAS_FK_ASS_ASSET = ass.ASS_TECHID) = 0

INSERT INTO T_TWI_TWIN (TWI_BEGTIME, TWI_ENDTIME, TWI_DISCRIMINATOR, TWI_FK_TWT_TYPE, TWI_VALUE, TTH_FK_THG_THING)
SELECT thg.THG_BEGTIME, thg.THG_ENDTIME, 'THG', @TWT_DEFAULT, '{}', thg.THG_TECHID FROM T_THG_THING thg WHERE (SELECT COUNT(*) FROM T_TWI_TWIN twi WHERE twi.TTH_FK_THG_THING = thg.THG_TECHID) = 0


