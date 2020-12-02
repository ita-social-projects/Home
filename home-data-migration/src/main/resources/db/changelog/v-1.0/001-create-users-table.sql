CREATE TABLE public.users (
                            id bigint not null  primary key,
                            first_name varchar (255)  not null,
                            last_name varchar (255)  not null,
                            email varchar (255) not null,
                            password varchar (255)  not null,
                            contact varchar (255) not null,
                            flat varchar (255) not null,
--                             user_role enum ('USER', 'ADMIN', 'STAFF','OWNER') default 'USER',
                            active boolean not null
                              );

