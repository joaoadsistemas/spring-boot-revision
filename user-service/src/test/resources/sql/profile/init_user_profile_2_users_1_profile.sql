insert into user (id, email, first_name, last_name, password, roles)
values (1,'regularuser@gmail.com','Humberto','Nobrega', '{bcrypt}$2a$10$6lUMl3EclqLaftu.GO.vNOj6yikvCUBY/.vS0ztc4ST72vORz7nKm', 'USER');

insert into user (id, email, first_name, last_name, password, roles)
values (2,'adminuser@gmail.com','Samuel','Vieira', '{bcrypt}$2a$10$6lUMl3EclqLaftu.GO.vNOj6yikvCUBY/.vS0ztc4ST72vORz7nKm', 'ADMIN');

insert into profile (id, description, name)
values (1,'Administrator','Administrator');

insert into user_profile(id, user_id, profile_id)
values (1,1,1);

insert into user_profile(id, user_id, profile_id)
values (2,2,1);
