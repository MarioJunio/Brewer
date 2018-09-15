create table estilo (
	id bigint primary key auto_increment,
	nome varchar(255) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table cerveja (
	id bigint primary key auto_increment,
	sku varchar(50) not null,
	nome varchar(80) not null,
	descricao text not null,
	foto varchar(100),
	valor decimal(10,2) not null,
	teor_alcoolico decimal(10,2) not null,
	comissao decimal(10,2),
	quantidade_estoque integer,
	sabor varchar(50) not null,
	origem varchar(50) not null,
	estilo_id bigint not null,
	foreign key (estilo_id) references estilo(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into estilo values(0, 'Amber Lager');
insert into estilo values(0, 'Dark Lager');
insert into estilo values(0, 'Pale Lager');
insert into estilo values(0, 'Pilsner');