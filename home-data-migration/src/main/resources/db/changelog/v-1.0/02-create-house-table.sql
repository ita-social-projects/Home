CREATE TABLE house (
                              id bigint(20) not null auto_increment,
                            street varchar (255) character set utf8 not null,
                            house_number varchar (255) character set utf8 not null,
                              primary key (id)
) ENGINE=InnoDB auto_increment=1000 default charset=utf8 collate =utf8_unicode_ci;