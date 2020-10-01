package com.lasrosas.iot.database.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lasrosas.iot.database.entities.thg.ThingProxy;

@Repository
public interface ThingProxRepo extends JpaRepository<ThingProxy,Long>, ThingProxyRepoCustom  {

}
