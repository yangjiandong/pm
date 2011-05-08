package org.ssh.pm.orm.hibernate;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    protected Object determineCurrentLookupKey() {
        // TODO Auto-generated method stub
        return CustomerContextHolder.getCustomerType();
    }

    public DataSource returnTargetDataSource() {
        return determineTargetDataSource();
    }
}
