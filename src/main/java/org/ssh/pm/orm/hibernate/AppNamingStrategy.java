package org.ssh.pm.orm.hibernate;

import java.io.Serializable;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.DefaultNamingStrategy;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.util.StringHelper;

// hibernate3.5 默认策略
public class AppNamingStrategy implements NamingStrategy, Serializable {


//    @Override
//    public String columnName(String columnName) {
//        //return addUnderscores(columnName).toUpperCase();
//        return columnName;
//    }
//
//    @Override
//    public String tableName(String tableName) {
//        //return addUnderscores(tableName).toUpperCase();
//        return tableName.toUpperCase();
//    }
//
//    @Override
//    public String classToTableName(String className) {
//        return "T_" + tableName(className);
//    }
//
//    @Override
//    public String propertyToColumnName(String propertyName) {
//        //return addUnderscores(propertyName).toUpperCase();
//        return propertyName;
//    }
//
    /**
     * The singleton instance
     */
    public static final NamingStrategy INSTANCE = new DefaultNamingStrategy();

    /**
     * Return the unqualified class name
     */
    public String classToTableName(String className) {
        return StringHelper.unqualify(className);
    }
    /**
     * Return the unqualified property name
     */
    public String propertyToColumnName(String propertyName) {
        return StringHelper.unqualify(propertyName);
    }
    /**
     * Return the argument
     */
    public String tableName(String tableName) {
        return tableName;
    }
    /**
     * Return the argument
     */
    public String columnName(String columnName) {
        return columnName;
    }

    /**
     * Return the unqualified property name, not the best strategy but a backward compatible one
     */
    public String collectionTableName(
            String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable,
            String propertyName
    ) {
        //use a degenerated strategy for backward compatibility
        return StringHelper.unqualify(propertyName);
    }

    /**
     * Return the argument
     */
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return columnName( joinedColumn );
    }

    /**
     * Return the property name or propertyTableName
     */
    public String foreignKeyColumnName(
            String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName
    ) {
        String header = propertyName != null ? StringHelper.unqualify( propertyName ) : propertyTableName;
        if (header == null) throw new AssertionFailure("NammingStrategy not properly filled");
        return columnName( header ); //+ "_" + referencedColumnName not used for backward compatibility
    }

    /**
     * Return the column name or the unqualified property name
     */
    public String logicalColumnName(String columnName, String propertyName) {
        return StringHelper.isNotEmpty( columnName ) ? columnName : StringHelper.unqualify( propertyName );
    }

    /**
     * Returns either the table name if explicit or
     * if there is an associated table, the concatenation of owner entity table and associated table
     * otherwise the concatenation of owner entity table and the unqualified property name
     */
    public String logicalCollectionTableName(String tableName,
                                             String ownerEntityTable, String associatedEntityTable, String propertyName
    ) {
        if ( tableName != null ) {
            return tableName;
        }
        else {
            //use of a stringbuffer to workaround a JDK bug
            return new StringBuffer(ownerEntityTable).append("_")
                    .append(
                        associatedEntityTable != null ?
                        associatedEntityTable :
                        StringHelper.unqualify( propertyName )
                    ).toString();
        }
    }
    /**
     * Return the column name if explicit or the concatenation of the property name and the referenced column
     *
     */
    public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
        return StringHelper.isNotEmpty( columnName ) ? columnName : propertyName + "_" + referencedColumn;
    }
}