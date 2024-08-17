drop table if exists item CASCADE;
create table item
(
    item_id bigint generated by default as identity,
    item_name varchar(45),
    price integer,
    image_url varchar(200),
    primary key (item_id)
);

drop table if exists member CASCADE;
create table member
(
    member_id varchar(30),
    password varchar(30),
    name varchar(20),
    email varchar(100),
    primary key (member_id)
);

drop table if exists wishlist CASCADE;
create table wishlist
(
    member_id varchar(30),
    item_id bigint,
    primary key (member_id, item_id)
);