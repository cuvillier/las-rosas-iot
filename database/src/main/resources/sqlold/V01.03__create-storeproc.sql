-- =============================================
-- Author:		Thibault Cuvillier
-- Create date: 01.06.2018
-- Description:	Update the Thing refid
-- =============================================

CREATE PROCEDURE UpdateAssPathId
AS
BEGIN
	WITH t(level, path, ASS_FK_ASS_PARTOF, ASS_TECHID) as (
		select 0, cast(cast(SPA_TECHID as nvarchar(max)) + ':' + cast(ASS_TECHID as nvarchar(max)) as nvarchar(max)), ASS_FK_ASS_PARTOF, ASS_TECHID
			from T_ASS_ASSET a 
			join T_ASS_ASSET_TYPE on a.ASS_FK_AST_TYPE = AST_TECHID
			join T_ASS_SPACE on AST_FK_SPA_SPACE = SPA_TECHID
			where ASS_FK_ASS_PARTOF is null
		union all
		select
			level + 1,
			cast(path + ':' + cast(e.ASS_TECHID as nvarchar(max)) as nvarchar(max)),
			e.ASS_FK_ASS_PARTOF,
			e.ASS_TECHID
		from 
			T_ASS_ASSET e
			inner join t on e.ASS_FK_ASS_PARTOF = t.ASS_TECHID                
	) update T_ASS_ASSET SET ASS_PATHID = i.path FROM (SELECT * FROM t ) i WHERE i.ASS_TECHID = T_ASS_ASSET.ASS_TECHID

END
GO

-- =============================================
-- Author:		Thibault Cuvillier
-- Create date: 04.06.2018
-- Description:	Update the TSR last_time and last_value
-- =============================================
CREATE PROCEDURE UpdateTsrCurrentValue
AS
BEGIN
	update T_TSR_TIME_SERIE set TSR_CURRENT_VALUE_TIME=(SELECT MAX(TSP_TIME) FROM T_TSR_TIME_SERIE_POINT WHERE TSP_FK_TSR_TIME_SERIE = TSR_TECHID)
	update T_TSR_TIME_SERIE set TSR_CURRENT_VALUE=(SELECT TSP_VALUE FROM T_TSR_TIME_SERIE_POINT WHERE TSP_FK_TSR_TIME_SERIE = TSR_TECHID AND TSP_TIME=TSR_CURRENT_VALUE_TIME)
END
GO