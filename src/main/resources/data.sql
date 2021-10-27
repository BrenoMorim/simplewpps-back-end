INSERT INTO PERFIL(id, nome) VALUES(1, 'ROLE_USUARIO'), (2, 'ROLE_MODERADOR');

INSERT INTO CATEGORIA(id, nome) VALUES(1, 'paisagem');

INSERT INTO USUARIO(id, nickname, email, senha) VALUES(1, 'breno', 'breno@brenomail.com', '$2a$10$OJ30QBofbWvpR3vEZgIJS.zI1tqu0QSFokA3F.rDfCcNIw5R3O7my'), (2, 'moderador', 'mod@modmail.com', '$2a$10$OJ30QBofbWvpR3vEZgIJS.zI1tqu0QSFokA3F.rDfCcNIw5R3O7my');
INSERT INTO USUARIO_PERFIS(perfis_id, usuario_id) VALUES(1, 1), (2, 2);

INSERT INTO WALLPAPER(id, titulo, descricao, url, autor_id) VALUES(1, 'wpp legal', 'wpp bem legal', 'https://www.linkqualquer.com/wpp', 1), (2, 'wpp lindo', 'wpp muito legal', 'https://www.outrolinkqualquer.com/wpp', 2);

INSERT INTO WALLPAPER_CATEGORIAS(categorias_id, wallpaper_id) VALUES(1, 1), (1, 2);