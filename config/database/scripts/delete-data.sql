
DELETE FROM choses.T_THG_THING_ASSET
DBCC CHECKIDENT ('choses.T_THG_THING_ASSET', RESEED, 0)

DELETE FROM choses.T_PRS_ASSET_ALLOCATION
DBCC CHECKIDENT ('choses.T_PRS_ASSET_ALLOCATION', RESEED, 0)

DELETE FROM choses.T_THG_ASSET
DBCC CHECKIDENT ('choses.T_THG_ASSET', RESEED, 0)

DELETE FROM choses.T_PRS_PERSON
DBCC CHECKIDENT ('choses.T_PRS_PERSON', RESEED, 0)

DELETE FROM choses.T_BCO_BEACON_SCAN
DBCC CHECKIDENT ('choses.T_BCO_BEACON_SCAN', RESEED, 0)

DELETE FROM choses.T_BCO_DETECTED_BEACON
DBCC CHECKIDENT ('choses.T_BCO_DETECTED_BEACON', RESEED, 0)

DELETE FROM choses.T_JSON_POINT
DBCC CHECKIDENT ('choses.T_JSON_POINT', RESEED, 0)

DELETE FROM choses.T_THG_PROPERTY_VALUE
DBCC CHECKIDENT ('choses.T_THG_PROPERTY_VALUE', RESEED, 0)

DELETE FROM choses.T_THG_THING
DBCC CHECKIDENT ('choses.T_THG_THING', RESEED, 0)

DELETE FROM choses.T_THG_THING_TYPE_PROPERTY
DBCC CHECKIDENT ('choses.T_THG_THING_TYPE_PROPERTY', RESEED, 0)

DELETE FROM choses.T_THG_THING_TYPE
DBCC CHECKIDENT ('choses.T_THG_THING_TYPE', RESEED, 0)

DELETE FROM choses.T_PLC_PLACE
DBCC CHECKIDENT ('choses.T_PLC_PLACE', RESEED, 0)

DELETE FROM choses.T_PLC_PLACE_TYPE
DBCC CHECKIDENT ('choses.T_PLC_PLACE_TYPE', RESEED, 0)
