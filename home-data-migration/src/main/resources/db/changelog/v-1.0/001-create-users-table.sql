CREATE TABLE if not exists public.users (
                            id int not null  primary key,
                            first_name varchar (255)  not null,
                            last_name varchar (255)  not null,
                            login varchar (255) not null,
                            password varchar (255)  not null,
                            phone_number varchar (255) not null,
                            flat varchar (255) not null,
--                             user_role enum ('USER', 'ADMIN', 'STAFF','OWNER') default 'USER',
                            active boolean not null default false
                              );

