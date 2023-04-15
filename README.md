Getting Started
-----

Launch on macOS
-----

* Install the latest version of [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* Install the **_Makefile Language_** and **_CheckStyle_** plugins
* Go to Preferences... -> Tools -> Checkstyle and choose checkstyle.xml, which lies in the root directory. Activate this configuration file
* Specify the address of the device on the local network in docker-compose.yml file in the property:
  ```SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KEYCLOAK_ISSUER-URI=http://192.168.XXX.XXX:8080/realms/smm-assistant```
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
* Specify the address of the device on the local network in docker-compose.yml file in the property:
   ```SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_KEYCLOAK_ISSUER-URI=http://192.168.XXX.XXX:8080/realms/smm-assistant```
* To launch the application:
  ```bash
  make upw
  ```
* To shut down the application:
  ```bash
  make downw
  ```
