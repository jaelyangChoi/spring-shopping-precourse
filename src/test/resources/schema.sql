drop table if exists product CASCADE;
create table product
(
    product_id bigint generated by default as identity,
    product_name varchar(45),
    price integer,
    image_url varchar(200),
    primary key (product_id)
);

drop table if exists member CASCADE;
create table member
(
    member_id bigint generated by default as identity,
    email varchar(100),
    password varchar(30),
    primary key (member_id)
);

drop table if exists wish CASCADE;
create table wish
(
    wish_id bigint generated by default as identity,
    member_id varchar(30),
    product_id bigint,
    primary key (wish_id)
);