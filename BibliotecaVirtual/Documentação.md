ola, se voce for usar esse programa para testar, porfavor, crie e cole esse codigo em: src/main/resources/application.properties

spring.application.name=loginapp
spring.datasource.url=jdbc:mariadb://localhost:3306/loginapp
spring.datasource.username=seunome
spring.datasource.password=suasenha
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect
spring.thymeleaf.cache=false
spring.security.user.name=
spring.security.user.password=
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration

troque o "seunome" pelo seu nome (duh)
e faça o mesmo com "suasenha" usando sua senha (duh parte 2)