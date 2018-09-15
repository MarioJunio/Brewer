create table estado(
	id bigint not null primary key,
	nome varchar(80) not null,
	uf char(2) not null
);

create table cidade(
	id bigint not null auto_increment primary key,
	nome varchar(100) not null,
	estado_id bigint not null,
	foreign key (estado_id) references estado(id)
);

insert into estado(id, nome, uf) values (1, 'Minas Gerais', 'MG'), (2, 'Bahia', 'BA'), (3, 'Góias', 'GO');

insert into cidade(id, nome, estado_id) values (0, 'Monte Carmelo', 1), (0, 'Uberlândia', 1), (0, 'Irai de Minas', 1), (0, 'Belo Horizonte', 1), (0, 'Salvador', 2), (0, 'Porto Seguro', 2), (0, 'Catalão', 3), (0, 'Goiânia', 3);