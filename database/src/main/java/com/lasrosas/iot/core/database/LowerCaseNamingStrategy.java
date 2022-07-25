package com.lasrosas.iot.core.database;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class LowerCaseNamingStrategy extends PhysicalNamingStrategyStandardImpl {
     
    @Override
    public Identifier toPhysicalTableName(final Identifier identifier, final JdbcEnvironment jdbcEnv) {
        if (identifier == null) {
            return null;
        }
 
        return Identifier.toIdentifier(identifier.getText().toLowerCase());
    }
}