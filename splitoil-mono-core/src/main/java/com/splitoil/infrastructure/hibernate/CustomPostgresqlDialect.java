package com.splitoil.infrastructure.hibernate;

import org.hibernate.dialect.PostgreSQL10Dialect;

import java.sql.Types;

public class CustomPostgresqlDialect extends PostgreSQL10Dialect {

    public CustomPostgresqlDialect() {

        super();

        registerColumnType(Types.JAVA_OBJECT, "json");
        registerHibernateType(Types.OTHER, "JsonUserType");
    }
}
