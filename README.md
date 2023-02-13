# Simplewpps Back End

> Status: Concluído

Api Rest simples para buscar e salvar wallpapers.

| :placard: Vitrine.Dev |                                                    |
|-----------------------|----------------------------------------------------|
| :sparkles: Nome       | **Simple Wallpapers**                              |
| :label: Tecnologias   | Java, Spring, postgresql                           |

![Screenshot do projeto](https://github.com/BrenoMorim/simplewpps-back-end/blob/main/imagem-do-projeto.png?raw=true#vitrinedev)

## Detalhes do Projeto

A api permite registro e login de usuários, que podem realizar o CRUD completo de Wallpapers, buscas personalizadas por categorias e salvar seus wallpapers favoritos através de um sistema de curtir e descurtir. A aplicação contém autenticação e autorização para segurança assim como uma camada de testes automatizados para cada um de seus controllers.

### Tecnologias utilizadas

O projeto foi feito completamente em Java e Spring, no ambiente de testes utiliza-se o banco h2, enquanto a versão de produção e desenvolvimento usam PostgreSQL, com configuração via variáveis de ambiente. A api conta com sistema de autenticação via token JWT. Para fazer o gerenciamento de dependências foi utilizado o maven.

## Endpoints

### GET /wpps

Endpoint público que retorna lista com wallpapers, com paginação padrão do Spring, pode ser usada com os query parameters size (número de elementos por página) e page (número da página). Além disso, é possível buscar os wallpaper por título e pelo nome da categoria, através dos query parameters titulo e categoriaNome.

> Exemplo de resposta para a url /wpps?page=0&size=2
><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"content": [</p>
><p style="margin: 0; margin-left: 40px;">{</p>
><p style="margin: 0; margin-left: 60px;">"titulo": "Paisagem Noturna com a Lua",</p>
><p style="margin: 0; margin-left: 60px;">"id": 2,</p>
><p style="margin: 0; margin-left: 60px;">"url": "https://images.urldosite.com/imagem.jpg"</p>
><p style="margin: 0; margin-left: 40px;">},</p>
><p style="margin: 0; margin-left: 40px;">{</p>
><p style="margin: 0; margin-left: 60px;">"titulo": "Wallpaper conceitual",</p>
><p style="margin: 0; margin-left: 60px;">"id": 1,</p>
><p style="margin: 0; margin-left: 60px;">"url": "https://images.urldosite.com/imagem.jpg"</p>
><p style="margin: 0; margin-left: 40px;">}</p>
><p style="margin: 0; margin-left: 20px;">],</p>
><p style="margin: 0; margin-left: 20px;">"pageable": {</p>
><p style="margin: 0; margin-left: 40px;">"sort": {</p>
><p style="margin: 0; margin-left: 60px;">"empty": true,</p>
><p style="margin: 0; margin-left: 60px;">"sorted": false,</p>
><p style="margin: 0; margin-left: 60px;">"unsorted": true</p>
><p style="margin: 0; margin-left: 40px;">},</p>
><p style="margin: 0; margin-left: 40px;">"offset": 0,</p>
><p style="margin: 0; margin-left: 40px;">"pageNumber": 0,</p>
><p style="margin: 0; margin-left: 40px;">"pageSize": 2,</p>
><p style="margin: 0; margin-left: 40px;">"paged": true,</p>
><p style="margin: 0; margin-left: 40px;">"unpaged": false</p>
><p style="margin: 0; margin-left: 20px;">},</p>
><p style="margin: 0; margin-left: 20px;">"totalPages": 3,</p>
><p style="margin: 0; margin-left: 20px;">"totalElements": 6,</p>
><p style="margin: 0; margin-left: 20px;">"last": true,</p>
><p style="margin: 0; margin-left: 20px;">"size": 2,</p>
><p style="margin: 0; margin-left: 20px;">"number": 0,</p>
><p style="margin: 0; margin-left: 20px;">"sort": {</p>
><p style="margin: 0; margin-left: 40px;">"empty": true,</p>
><p style="margin: 0; margin-left: 40px;">"sorted": false,</p>
><p style="margin: 0; margin-left: 40px;">"unsorted": true</p>
><p style="margin: 0; margin-left: 20px;">},</p>
><p style="margin: 0; margin-left: 20px;">"numberOfElements": 2,</p>
><p style="margin: 0; margin-left: 20px;">"first": true,</p>
><p style="margin: 0; margin-left: 20px;">"empty": false</p>
><p style="margin: 0;">}</p>

### POST /wpps

Requer autenticação, permite a criação de um novo wallpaper, recebendo o título do wallpaper, a url da imagem e uma lista de categorias, retorna status code 201 em caso de sucesso. 

> Exemplo de corpo para a requisição:
><p style="margin: 0">{</p>
><p style="margin: 0; margin-left: 20px;">"titulo": "Imagem de exemplo",</p>
><p style="margin: 0; margin-left: 20px;">"categorias": ["minimalista"],</p>
><p style="margin: 0; margin-left: 20px;">"url": "https://www.urldaimagem.com.br/imagem" </p>
><p style="margin: 0;">}</p>

### GET /wpps/{id}

Não requer autenticação, retorna os detalhes de um wallpaper específico, contendo a data de criação e o nome do usuário que publicou o wallpaper.

> Exemplo de resposta:
><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"id": 617706903,
><p style="margin: 0; margin-left: 20px;">"titulo": "Fundo rosa abstrato",
><p style="margin: 0; margin-left: 20px;">"url": "https://urldaimagem.com.br/imagem",
><p style="margin: 0; margin-left: 20px;">"categorias": [
><p style="margin: 0; margin-left: 40px;">{
><p style="margin: 0; margin-left: 60px;">"id": 5,
><p style="margin: 0; margin-left: 60px;">"nome": "minimalista"
><p style="margin: 0; margin-left: 40px;">}
><p style="margin: 0; margin-left: 20px;">],
><p style="margin: 0; margin-left: 20px;">"dataCriacao": "2023-02-12T13:49:04.930847",
><p style="margin: 0; margin-left: 20px;">"nomeAutor": "breno"
><p style="margin: 0;">}</p>

### PUT /wpps/{id}

Requer autenticação, para poder editar um wallpaper, o usuário deve ser o autor do wallpaper ou possuir a role moderador. O corpo da requisição pode conter somente os campos que serão alterados.

> Exemplo de corpo para a requisição:
><p style="margin: 0">{</p>
><p style="margin: 0; margin-left: 20px;">"titulo": "Novo título"</p>
><p style="margin: 0;">}</p>

### DELETE /wpps/{id}

Deleta um wallpaper do banco de dados, assim como na requisição PUT, requer que o usuário seja autor do wallpaper ou moderador.

### GET /wpps/salvos

Retorna uma lista de todos os wallpapers que foram curtidos pelo usuário, essa lista contém paginação assim como a requisição GET para /wpps que pode ser ajustada com os query parameters size e page. No caso do usuário não estar autenticado, é retornado o status code 401.

> Exemplo de resposta:
><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"content": [</p>
><p style="margin: 0; margin-left: 40px;">{</p>
><p style="margin: 0; margin-left: 60px;">"titulo": "Paisagem Noturna com a Lua",</p>
><p style="margin: 0; margin-left: 60px;">"id": 2,</p>
><p style="margin: 0; margin-left: 60px;">"url": "https://images.urldosite.com/imagem.jpg"</p>
><p style="margin: 0; margin-left: 40px;">}</p>
><p style="margin: 0; margin-left: 20px;">],</p>
><p style="margin: 0; margin-left: 20px;">"pageable": {</p>
><p style="margin: 0; margin-left: 40px;">"sort": {</p>
><p style="margin: 0; margin-left: 60px;">"empty": true,</p>
><p style="margin: 0; margin-left: 60px;">"sorted": false,</p>
><p style="margin: 0; margin-left: 60px;">"unsorted": true</p>
><p style="margin: 0; margin-left: 40px;">},</p>
><p style="margin: 0; margin-left: 40px;">"offset": 0,</p>
><p style="margin: 0; margin-left: 40px;">"pageNumber": 0,</p>
><p style="margin: 0; margin-left: 40px;">"pageSize": 10,</p>
><p style="margin: 0; margin-left: 40px;">"paged": true,</p>
><p style="margin: 0; margin-left: 40px;">"unpaged": false</p>
><p style="margin: 0; margin-left: 20px;">},</p>
><p style="margin: 0; margin-left: 20px;">"totalPages": 1,</p>
><p style="margin: 0; margin-left: 20px;">"totalElements": 1,</p>
><p style="margin: 0; margin-left: 20px;">"last": true,</p>
><p style="margin: 0; margin-left: 20px;">"size": 10,</p>
><p style="margin: 0; margin-left: 20px;">"number": 0,</p>
><p style="margin: 0; margin-left: 20px;">"sort": {</p>
><p style="margin: 0; margin-left: 40px;">"empty": true,</p>
><p style="margin: 0; margin-left: 40px;">"sorted": false,</p>
><p style="margin: 0; margin-left: 40px;">"unsorted": true</p>
><p style="margin: 0; margin-left: 20px;">},</p>
><p style="margin: 0; margin-left: 20px;">"numberOfElements": 1,</p>
><p style="margin: 0; margin-left: 20px;">"first": true,</p>
><p style="margin: 0; margin-left: 20px;">"empty": false</p>
><p style="margin: 0;">}</p>

### GET /wpps/curtir/{id}

Adiciona um wallpaper à lista de salvos do usuário, requer autenticação para ser acessada retorna status 204 em sucesso.

### GET /wpps/descurtir/{id}

Remove um wallpaper da lista de salvos do usuário, também requer autenticação para ser usada e retorna status 204 em sucesso.

---

### POST /auth/login

Endpoint para login, recebe email e senha no corpo:

><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"email": "user@email.com",</p>
><p style="margin: 0; margin-left: 20px;">"senha": "password"</p>
><p style="margin: 0;">}</p>

Em caso de sucesso, o token de autenticação é enviado na resposta:

><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"token": ""eyJhbGciOiJIUzI1NiJ9.eyJp...(resto do token)",</p>
><p style="margin: 0; margin-left: 20px;">"tipo": "Bearer"</p>
><p style="margin: 0;">}</p>

### POST /auth/register

Permite o cadastro de um novo usuário na api, no corpo da requisição são enviados o nickname, o email e a senha:

><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"nickname": "User",</p>
><p style="margin: 0; margin-left: 20px;">"email": "user@email.com",</p>
><p style="margin: 0; margin-left: 20px;">"senha": "password"</p>
><p style="margin: 0;">}</p>

---

### GET /categorias

Não requer autenticação e retorna uma lista de categorias paginada:

><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"content": [</p>
><p style="margin: 0; margin-left: 40px;">{</p>
><p style="margin: 0; margin-left: 60px;">"id": 1,</p>
><p style="margin: 0; margin-left: 60px;">"nome": "categorias"</p>
><p style="margin: 0; margin-left: 40px;">},</p>
><p style="margin: 0; margin-left: 40px;">{</p>
><p style="margin: 0; margin-left: 60px;">"id": 2,</p>
><p style="margin: 0; margin-left: 60px;">"nome": "filmes"</p>
><p style="margin: 0; margin-left: 40px;">}</p>
><p style="margin: 0; margin-left: 20px;">],</p>
><p style="margin: 0; margin-left: 20px;">(Configurações de paginação omitidas)</p>
><p style="margin: 0;">}</p>

Esta rota cria um cache chamado listaDeCategorias, já que as categorias são alteradas raramente, usar cache se torna vantajoso nesse caso.

### GET /categorias/{nome}

Não requer autenticação e retorna as informações de uma categoria específica (nome e id):

> GET /categorias/paisagem
><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"id": 1,</p>
><p style="margin: 0; margin-left: 20px;">"nome": "paisagem"</p>
><p style="margin: 0;">}</p>

### POST /categorias

Rota exclusiva para moderadores, cria uma nova categoria, recebendo um nome:

><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"nome": "pokemon"</p>
><p style="margin: 0;">}</p>

### PUT /categorias/{id}

Também exclusiva para moderadores, altera o nome de uma categoria:

> PUT /categorias/7
><p style="margin: 0;">{</p>
><p style="margin: 0; margin-left: 20px;">"nome": "Pokemon"</p>
><p style="margin: 0;">}</p>

### DELETE /categorias/{id}

Rota restrita aos moderadores, deleta uma categoria pelo id, retorna 204 em caso de sucesso.

---
