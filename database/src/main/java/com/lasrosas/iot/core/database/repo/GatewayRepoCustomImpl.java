package com.lasrosas.iot.core.database.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GatewayRepoCustomImpl implements GatewayRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

}
