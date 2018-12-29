![logo-portal](http://iconwanted.com/downloads/th3-prophetman/game-icons-ii/png/128x128/portal.png) 
# Portal
Um simples portal de serviços para autenticação e cadastro de usuário, via tokens JWT - JSON Web Token.

### Tecnologias
 O serviço foi desenvolvido em Java 8, utilizando as bibliotecas:
 * [Spring Boot](https://spring.io/projects/spring-boot)
 * [Spring Data](https://spring.io/projects/spring-data)
 * [Spring Security](https://spring.io/projects/spring-ws)
 * [JWT - JSON Web Token](https://jwt.io/)
 * [Gson](https://github.com/google/gson)
 * [HSQLDB](http://hsqldb.org/)
 * [JUnit4](https://junit.org/junit4/)
 * [Mockito](https://site.mockito.org/)

### Paramêtros de configuração
Para configuração do projeto, deve alterar o arquivo 'application.properties'. Nele, pode-se personalizar:
```sh
#locale - idioma do aplicação
#spring.datasource.username - usuário do banco de dados
#spring.datasource.password - senha do banco de dados
#jwt.secret - secret para geração dos tokens
#jwt.expiration - tempo de expiração dos tokens, em minuto
#jwt.route.authentication.path= - endpoint para authenticar o usuário
```
