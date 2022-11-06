# Simplewpps Back End

> Status: Concluído

Api Rest simples para buscar e salvar wallpapers.

| :placard: Vitrine.Dev |     |
| -------------  | --- |
| :sparkles: Nome        | **Simple Wallpapers**
| :label: Tecnologias | Java, Spring, postgresql
| :rocket: URL         | https://simplewpps.herokuapp.com/swagger-ui.html

![](https://github.com/BrenoMorim/simplewpps-back-end/blob/main/imagem-do-projeto.png?raw=true#vitrinedev)

## Detalhes do Projeto

A api permite registro e login de usuários, que podem realizar o CRUD completo de Wallpapers, buscas personalizadas por categorias e salvar seus wallpapers favoritos através de um sistema de curtir e descurtir. A aplicação contém autenticação e autorização para segurança assim como uma camada de testes automatizados para cada um de seus controllers. O serviço da api está disponível via heroku e contém documentação gerada por Swagger.

### Tecnologias utilizadas

O projeto foi feito completamente em Java e Spring, no ambiente de testes utiliza-se o banco h2, enquanto a versão de produção e desenvolvimento usam PostgreSQL, com configuração via variáveis de ambiente. A api conta com sistema de autenticação via token JWT. Para fazer o gerenciamento de dependências foi utilizado o maven.

---
