package com.lasrosas.iot.services.db.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GatewayRepoCustomImpl implements GatewayRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

}
