# Simplewpps Back End

> Status: Concluído

## Api Rest para buscar e salvar wallpapers

### Funcionalidades

A api permite registro e login de usuários, que podem realizar o CRUD completo de Wallpapers, buscas personalizadas por categorias e salvar seus wallpapers favoritos através de um sistema de curtir e descurtir. A aplicação contém autenticação e autorização para segurança assim como uma camada de testes automatizados para cada um de seus controllers.

### Tecnologias utilizadas

O projeto foi feito completamente em Java e Spring, no ambiente de desenvolvimento utiliza-se o banco h2 para realizar testes, enquanto a versão de produção usa MySQL. A api conta com sistema de autenticação via token JWT. Para fazer o gerenciamento de dependências foi utilizado o maven.
