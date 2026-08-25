# ADR 0002: MySQL como banco de dados relacional

## Status
Aceito

## Contexto
O dominio (usuarios, times, incidentes, historico de auditoria) e fortemente relacional, com integridade referencial entre as entidades (um incidente sempre pertence a quem reportou, opcionalmente a um time e a um responsavel). As opcoes consideradas foram MySQL, PostgreSQL e um banco documento (MongoDB).

## Decisao
MySQL 8.4, acessado via Spring Data JPA/Hibernate, com o schema versionado por migrations Flyway (nao `ddl-auto: update`).

## Consequencias
- Migrations versionadas (`db/migration/V1__init_schema.sql`) tornam o historico do schema auditavel e reprodutivel — o mesmo schema e criado do zero em dev, CI (Testcontainers) e producao.
- `ddl-auto` fica travado em `validate`: o Hibernate nunca altera o schema sozinho, so confere se as entidades batem com o que existe. Qualquer mudanca de schema passa obrigatoriamente por uma nova migration.
- A escolha de um banco relacional deixa mais rigido adicionar campos muito variaveis por incidente no futuro (ex.: campos customizados por time) — se isso vier a ser necessario, provavelmente via uma coluna JSON, nao uma migracao pra NoSQL.
