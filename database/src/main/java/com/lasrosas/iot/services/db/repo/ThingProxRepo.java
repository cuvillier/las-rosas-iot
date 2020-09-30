package com.lasrosas.iot.services.db.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.services.db.entities.thg.ThingProxy;

@Repository
public interface ThingProxRepo extends JpaRepository<ThingProxy,Long>, ThingProxyRepoCustom  {

}
