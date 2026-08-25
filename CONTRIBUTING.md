# Contribuindo

Esse e um projeto de portfolio, mas segue o mesmo fluxo que eu seguiria num time real.

## Branches

- `main` e sempre estavel e protegida — nada e commitado direto nela depois da configuracao inicial do projeto.
- Uma branch por tarefa, a partir da `main` atualizada: `feature/nome-da-funcionalidade`, `fix/nome-do-bug`, `chore/nome-da-tarefa`, `docs/nome-do-assunto`.

## Commits

[Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` — nova funcionalidade
- `fix:` — correcao de bug
- `docs:` — documentacao
- `test:` — testes
- `ci:` — pipeline/CI
- `chore:` — manutencao, configuracao, sem impacto em codigo de producao
- `refactor:` — mudanca de codigo sem alterar comportamento

## Antes de abrir um PR

```bash
mvn spotless:apply   # formata o codigo
mvn test              # confirma que os testes passam localmente
```

## Pull Request

1. Push da branch, abrir PR contra a `main`.
2. O CI roda automaticamente (`build`, `lint`, `docker-build`) — o merge so e liberado com os tres verdes.
3. Squash merge, apaga a branch.

## Versionamento

Tags seguem [SemVer](https://semver.org/lang/pt-BR/) (`vMAJOR.MINOR.PATCH`), criadas na `main` depois de um merge que marca um ponto estavel do projeto.
