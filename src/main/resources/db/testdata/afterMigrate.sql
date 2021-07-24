set foreign_key_checks = 0;

delete from ator;

set foreign_key_checks = 1;

alter table ator auto_increment = 1;

insert into ator (id, nome, data_nascimento, biografia, telefone) values
(1, 'Brad Pitt', '1963-12-18', '‎Ator e produtor', '99-222333'),
(2, 'Edward Norton', '1969-10-18', 'O ator, cineasta e ativista norte-americano', '55-666777'),
(3, 'Meat Loaf', '1947-09-27', '‎Meat Loaf nasceu Marvin Lee Aday em Dallas, Texas', '44-965779'),
(4, 'Anthony Mackie', '1978-09-23', 'Anthony Mackie é um ator americano', '77-961279'),
(5, 'Sebastian Stan', '1982-10-13', 'Stan estudou na Rutgers Mason Gross School of the Arts', '33-965788'),
(6, 'Wyatt Russell', '1986-07-10', '‎É ator e produtor, conhecido por ‎‎Operação Overlord‎‎ (2018)', '11-695735');