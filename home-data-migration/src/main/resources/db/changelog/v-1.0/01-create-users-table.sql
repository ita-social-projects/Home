CREATE TABLE users (
                              id bigint(20) not null auto_increment,
                            first_name varchar (255) character set utf8 not null,
                            last_name varchar (255) character set utf8 not null,
                            login varchar (255) character set utf8 not null,
                            password varchar (255) character set utf8 not null,
                            phone_number varchar (255) character set utf8 not null,
                            flat varchar (255) character set utf8 not null,
                            user_role enum ('USER', 'ADMIN', 'STAFF') default 'USER',
                            active boolean not null default false,
                              primary key (id)
--                               UNIQUE KEY UK_users_corporate_email (corporate_email)
) ENGINE=InnoDB auto_increment=1000 default charset=utf8 collate =utf8_unicode_ci;