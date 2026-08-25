# ADR 0003: Arquitetura em camadas simples (Controller -> Service -> Repository)

## Status
Aceito

## Contexto
O dominio do projeto e de porte pequeno/media (quatro entidades principais). Arquiteturas mais elaboradas (hexagonal, DDD completo com agregados e portas/adaptadores) trazem beneficios claros em dominios grandes e com regras de negocio complexas, mas tem custo de indirecao que nem sempre se paga em projetos desse tamanho.

## Decisao
Camadas convencionais do Spring: `Controller` (HTTP/validacao de entrada) -> `Service` (regra de negocio, transacao) -> `Repository` (Spring Data JPA) -> banco. Sem camada de dominio separada da entidade JPA.

## Consequencias
- Curva de entrada baixa — qualquer dev Spring reconhece o padrao imediatamente, o que importa bastante num projeto de portfolio que vai ser lido por outras pessoas (recrutadores, entrevistadores).
- Entidades JPA fazem duplo papel (persistencia e modelo de dominio), o que uma arquitetura hexagonal evitaria — aceitavel dado o tamanho atual do dominio, mas seria o primeiro ponto a revisar se o projeto crescesse bastante.
- Toda regra de negocio fica concentrada nos `Service`s (ex.: `IncidentService` sempre grava o historico junto da mudanca de status) — evita que a mesma regra seja reimplementada em mais de um lugar.
