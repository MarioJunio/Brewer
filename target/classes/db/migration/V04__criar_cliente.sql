create table cliente (
	id bigint primary key auto_increment,
	nome varchar(255) not null,
	cpf_cnpj varchar(20) not null,
	telefone varchar(20),
	email varchar(300),
	logradouro varchar(80),
	numero integer,
	complemento text,
	cep varchar(10),
	cidade_id bigint,
	foreign key (cidade_id) references cidade(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;