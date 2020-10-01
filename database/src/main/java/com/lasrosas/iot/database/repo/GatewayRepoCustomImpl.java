package com.lasrosas.iot.database.repo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class GatewayRepoCustomImpl implements GatewayRepoCustom {

    @PersistenceContext
    EntityManager entityManager;

}
