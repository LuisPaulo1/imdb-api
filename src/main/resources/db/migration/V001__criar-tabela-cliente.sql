create table cliente (
    id bigint not null auto_increment,
    nome varchar(80) not null,
    email varchar(50) not null,
    senha varchar(255) not null,
    ativo tinyint(1) not null,
    tipo varchar(13) not null,
    perfil integer not null,
    data_cadastro datetime not null,
    data_atualizacao datetime not null,
    primary key (id)
) engine=InnoDB default charset=utf8;

alter table cliente 
   add constraint UK_Email unique (email)