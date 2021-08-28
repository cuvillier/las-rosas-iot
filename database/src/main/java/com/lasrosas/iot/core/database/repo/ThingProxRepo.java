package com.lasrosas.iot.core.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.core.database.entities.thg.ThingProxy;

@Repository
public interface ThingProxRepo extends JpaRepository<ThingProxy,Long>, ThingProxyRepoCustom  {

}
