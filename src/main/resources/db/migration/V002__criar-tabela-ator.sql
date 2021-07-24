create table ator (
   id bigint not null auto_increment,
   nome varchar(80) not null,
   data_nascimento date not null,
   biografia varchar(200),
   telefone varchar(20) not null,
   primary key (id)
) engine=InnoDB default charset=utf8;