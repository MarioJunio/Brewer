create table venda (
	id bigint not null primary key auto_increment,
	data_criacao datetime,
	valor_frete numeric(10,2),
	valor_desconto numeric(10,2),
	valor_total numeric(10,2),
	observacao text,
	data_entrega datetime,
	status_venda varchar(40),
	vendedor_id bigint,
	cliente_id bigint,
	foreign key (vendedor_id) references usuario(codigo),
	foreign key (cliente_id) references cliente(id)
);

create table item_venda(
	id bigint not null primary key auto_increment,
	venda_id bigint not null,
	quantidade integer not null,
	valor_unitario numeric(10,2),
	cerveja_id bigint not null,
	foreign key(venda_id) references venda(id),
	foreign key(cerveja_id) references cerveja(id)
);