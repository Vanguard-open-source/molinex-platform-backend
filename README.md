# Molinex Platform Backend

Base Spring Boot del backend REST de Molinex para el curso de Open Source.

Abre el proyecto Maven en IntelliJ y selecciona JDK 27. Desde la raíz puedes
compilar con `./mvnw clean verify` o `.\mvnw.cmd clean verify` en Windows.
Para iniciar la aplicación ejecuta `./mvnw spring-boot:run` o
`.\mvnw.cmd spring-boot:run` en Windows.

El código de los bounded contexts se incorporará mediante ramas feature
creadas desde develop. Cada contexto organizará sus responsabilidades en
interfaces, application, domain e infrastructure.
