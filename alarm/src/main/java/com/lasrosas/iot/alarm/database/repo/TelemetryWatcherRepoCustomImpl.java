package com.lasrosas.iot.alarm.database.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.lasrosas.iot.alarm.database.entity.TelemetryWatcher;
import com.lasrosas.iot.core.database.HibernateUtils;
import com.lasrosas.iot.core.database.entities.dtw.DigitalTwin;
import com.lasrosas.iot.core.database.entities.thg.Thing;
import com.lasrosas.iot.core.shared.utils.Junion;

public class TelemetryWatcherRepoCustomImpl implements TelemetryWatcherRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

	@Override
	public List<TelemetryWatcher> findMatchingWatchers(Thing thing, Object payload) {
		return findMatchingWatchers(new Junion(thing, Thing.class), payload);
	}

	@Override
	public List<TelemetryWatcher> findMatchingWatchers(DigitalTwin twin, Object payload) {
		return findMatchingWatchers(new Junion(twin, DigitalTwin.class), payload);
	}

	@SuppressWarnings("unchecked")
	public List<TelemetryWatcher> findMatchingWatchers(Junion thingOrTwin, Object payload) {
		var query = new StringBuilder();

		query.append("SELECT * FROM " + TelemetryWatcher.TABLE + " WHERE ");

		if(thingOrTwin.isA(Thing.class)) {
			var thing = thingOrTwin.getValueAs(Thing.class).get();

			query.append(
					"(" + TelemetryWatcher.COL_DATA_BASE_TYPE + " is null OR "+
					"'Thing' ~ " + TelemetryWatcher.COL_DATA_BASE_TYPE + ") ");

			var naturalId = thing.getType().getManufacturer() + "/" +
							thing.getType().getModel() + "/" +
							thing.getNaturalId();
			query.append(
					"AND (("+ TelemetryWatcher.COL_DATA_NATURAL_ID + " is null) OR " +
					"('"+ naturalId +"' ~ " + TelemetryWatcher.COL_DATA_NATURAL_ID + ")) ");

		} else {
			var twin = thingOrTwin.getValueAs(DigitalTwin.class).get();

			query.append(
					"(" + TelemetryWatcher.COL_DATA_BASE_TYPE +" is null " +
					"OR 'DigitalTwin' ~ "+ TelemetryWatcher.COL_DATA_BASE_TYPE +") ");

			var naturalId = twin.getName();
			query.append(
					"AND ((" + TelemetryWatcher.COL_DATA_NATURAL_ID + "is null) OR " +
					"('"+ naturalId +"' ~ "+TelemetryWatcher.COL_DATA_NATURAL_ID+") ");
		}

		var schema = HibernateUtils.getCleanClass(payload).getSimpleName();
		if(schema != null)
			query.append(
					"AND ("+ TelemetryWatcher.COL_SCHEMA + " is null OR " +
					"'" + schema + "' ~ " + TelemetryWatcher.COL_SCHEMA + ")");

		var sql = query.toString();		
		System.out.println(sql);

		var q = entityManager.createNativeQuery(sql, TelemetryWatcher.class);

		return q.getResultList();
	}
}
