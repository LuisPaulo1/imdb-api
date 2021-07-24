create table filme_ator (
   filme_id bigint not null,
   ator_id bigint not null,
   primary key (filme_id, ator_id)
) engine=InnoDB default charset=utf8;

 alter table filme_ator add constraint fk_filme_ator_ator foreign key (ator_id) references ator (id);
 alter table filme_ator add constraint fk_filme_ator_filme foreign key (filme_id) references filme (id);