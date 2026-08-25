# ADR 0004: Testcontainers em vez de H2 para testes de integracao

## Status
Aceito

## Contexto
Os testes de integracao (`AuthControllerTest`, `IncidentControllerTest`) precisam de um banco real por tras do Spring context. As migrations Flyway usam tipos especificos do MySQL (ex.: `BINARY(16)` para armazenar UUID). Rodar os testes contra H2 em memoria seria mais rapido, mas H2 nao garante o mesmo comportamento do MySQL nesses detalhes de tipo — um teste passando em H2 nao garante nada sobre o comportamento em producao.

## Decisao
Testes de integracao sobem um container MySQL real via Testcontainers (`IntegrationTestBase`), com as mesmas migrations Flyway aplicadas do zero a cada execucao.

## Consequencias
- Os testes validam o comportamento real do driver e do dialeto MySQL, nao uma aproximacao.
- A suite de testes fica mais lenta (subir um container Docker a cada execucao) e passa a depender do Docker estar disponivel — tanto localmente quanto no CI (o GitHub Actions ja tem Docker disponivel por padrao nos runners `ubuntu-latest`, entao isso nao exige configuracao extra no `ci.yml`).
- Numa base de testes muito maior, valeria reavaliar: reusar o mesmo container entre classes de teste (padrao *Singleton Container*) em vez de um novo por classe, para nao pagar esse custo repetidas vezes.
