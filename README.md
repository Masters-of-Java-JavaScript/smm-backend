Getting Started
-----

Launch on macOS
-----

* Install the latest version of [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* Install the **_Makefile Language_** and **_CheckStyle_** plugins
* Go to Preferences... -> Tools -> Checkstyle and choose checkstyle.xml, which lies in the root directory. Activate this configuration file
* Specify the address of the KeyCloak Auth Server in application.yml file in the property:\
  ```spring.security.oauth2.client.provider.keycloak.issuer-uri=http://XXX.XXX.XXX.XXX:8080/realms/smm-assistant```
* To launch the application:
  ```bash
  make up
  ```
* To shut down the application:
  ```bash
  make down
  ```

Launch on Windows
-----

* Install the latest version of [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* Install the **_Makefile_** on your PC
* Install the **_Makefile Language_** and **_CheckStyle_** plugins for IDE
* Go to Settings... -> Tools -> Checkstyle and choose checkstyle.xml, which lies in the root directory. Activate this configuration file
* Specify the address of the KeyCloak Auth Server in application.yml file in the property:\
   ```pring.security.oauth2.client.provider.keycloak.issuer-uri=http://XXX.XXX.XXX.XXX:8080/realms/smm-assistant```
* To launch the application:
  ```bash
  make upw
  ```
* To shut down the application:
  ```bash
  make downw
  ```

SpringDoc
-----
[OpenAPI description](http://localhost:8080/v3/api-docs)\
[Swagger](http://localhost:8080/swagger-ui.html)
