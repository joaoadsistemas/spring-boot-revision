insert into "user" (id, email, first_name, last_name)
values (1,'humbertonobrega@gmail.com','Humberto','Nobrega');

insert into "user" (id, email, first_name, last_name)
values (2,'samuelvieira@gmail.com','Samuel','Vieira');

insert into profile (id, description, name)
values (1,'Administrator','Administrator');

insert into user_profile(id, user_id, profile_id)
values (1,1,1);

insert into user_profile(id, user_id, profile_id)
values (2,2,1);
