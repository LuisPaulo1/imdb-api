create table avaliacao (
   id bigint not null auto_increment,
   voto integer,
   total bigint,
   filme_id bigint,
   primary key (id)
) engine=InnoDB default charset=utf8;

alter table avaliacao add constraint fk_filme_avaliacao foreign key (filme_id) references filme (id);