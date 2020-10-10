package com.lasrosas.iot.reactore.reactores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.lasrosas.iot.database.entities.dtw.DigitalTwin;

public class TwinReactors {
	public static final Logger log = LoggerFactory.getLogger(TwinReactors.class);

	@Autowired
	private ApplicationContext appContext;

	public TwinReactor getReactor(DigitalTwin twin) {
		var twinType = twin.getType();

		String twinReactorName = twinType.getSpace().getName()+"-"+ twinType.getName();
		if( !appContext.containsBean(twinReactorName)) {
			twinReactorName = twinReactorName + "-" + twin.getName();
			if( !appContext.containsBean(twinReactorName)) {
				return null;
			}
		}

		return appContext.getBean(twinReactorName, TwinReactor.class);
	}
}
