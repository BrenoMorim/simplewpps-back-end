INSERT INTO PERFIS(id, nome) VALUES(1, 'ROLE_USUARIO'), (2, 'ROLE_MODERADOR');

INSERT INTO CATEGORIAS(id, nome) VALUES(1, 'paisagem'), (2, 'filmes'), (3, 'animes'), (4, 'animais'), (5, 'minimalista'), (6, 'personagem');

INSERT INTO USUARIOS(id, nickname, email, senha) VALUES(1, 'breno', 'breno@brenomail.com', '$2a$10$OJ30QBofbWvpR3vEZgIJS.zI1tqu0QSFokA3F.rDfCcNIw5R3O7my'), (2, 'moderador', 'mod@modmail.com', '$2a$10$OJ30QBofbWvpR3vEZgIJS.zI1tqu0QSFokA3F.rDfCcNIw5R3O7my');
INSERT INTO USUARIOS_PERFIS(perfis_id, usuario_id) VALUES(1, 1), (2, 2);

INSERT INTO WALLPAPERS(id, titulo, url, autor_id) VALUES(1, 'Wallpaper Conceitual', 'https://images.unsplash.com/photo-1622737133809-d95047b9e673?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8d2FsbHBhcGVyJTIwNGt8ZW58MHx8MHx8&w=1000&q=80', 1), (2, 'Paisagem Noturna com a Lua', 'https://images.wallpapersden.com/image/download/4k-talking-to-the-moon_a2pubmuUmZqaraWkpJRobWllrWdma2U.jpg', 2);

INSERT INTO WALLPAPERS_CATEGORIAS(categorias_id, wallpaper_id) VALUES(5, 1), (1, 2);
