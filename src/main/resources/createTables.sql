create table cameras (
	id serial primary key,
	address varchar(255) not null,
	);

create table images (
	id serial primary key,
	camera_id int not null,
	path varchar(255) not null,
	date timestamp not null,
	foreign key (camera_id) references cameras(id)
	);

