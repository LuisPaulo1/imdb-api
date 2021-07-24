create table filme (
   id bigint not null auto_increment,
   nome varchar(100) not null,
   titulo varchar(120) not null,
   data_lancamento date not null,
   genero varchar(30) not null,
   diretor varchar(50) not null,   
   data_cadastro datetime not null,
   data_atualizacao datetime not null,
   primary key (id)
) engine=InnoDB default charset=utf8;