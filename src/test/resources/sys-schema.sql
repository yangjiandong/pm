    drop table T_ROLES if exists;
    drop table T_USERS if exists;
    drop table T_USER_ROLE if exists;
    drop table ID_GENERATOR if exists;

    create table ID_GENERATOR (
         GEN_NAME varchar(255),
         GEN_VAL int
    ) ;

    create table T_ROLES (
        id bigint ,
        description varchar(100) null,
        name varchar(20) not null unique,
        primary key (id)
    );

    create table T_USERS (
        id bigint ,
        addedWho varchar(50) null,
        addedOn varchar(50) null,
        updatedWho varchar(50) null,
        updatedOn varchar(50) null,
        email varchar(50) null,
        loginName varchar(20) not null unique,
        name varchar(50) null,
        password varchar(255) null,
        status varchar(10) null,
        version int null,
        primary key (id)
    );

    create table T_USER_ROLE (
        USER_ID bigint not null,
        ROLE_ID bigint not null
    );

