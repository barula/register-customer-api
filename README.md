# Proyecto demo register-customer-api
Acá va un párrafo que describa lo que es el proyecto
 
## Comenzando
Estas instrucciones te permitirán obtener una copia del proyecto en
funcionamiento en tu máquina local para propósitos de desarrollo y
pruebas.
 
URL GIT: https://github.com/ezequiel/register-customer-api
 
### Agregar librería al proyecto
Para utilizar esta librería se debe agregar la siguiente dependencia al pom.xml```
 
```
   <dependency>
       <groupId>{{groupID}}</groupId>
       <artifactId>{{artifactId}}</artifactId>
       <version>[1.0,)</version>
   </dependency>
```
 
 
## Pre-requisitos
Que cosas necesitas para instalar el software y como instalarlas
**Da un ejemplo**
 
## Instalación
 
Una serie de ejemplos paso a paso que te dice lo que debes ejecutar para tener un entorno de desarrollo ejecutandose
Dí cómo será ese paso
**Da un ejemplo**
 
    > mvn spring-boot:run
 
**Y repite**
**hasta finalizar**
 
Finaliza con un ejemplo de cómo obtener datos del sistema o como usarlos para una pequeña demo
 
## Pruebas
 
 > mvn clean install -U
 > mvn spring-boot:run

Explica como ejecutar las pruebas automatizadas para este sistem
Endpoints
 
Listar y explicar los diferentes endpoints que contesga si los contiene
 
**Da un ejemplo**
 
POST /PATH/servicio
 
    {
      variable: "valor"
    }
 
## Construir
Menciona las herramientas que utilizaste para crear tu proyect
Maven - Manejador de dependencias
 
    > mvn package
 
## Deployment
Agrega notas adicionales sobre como hacer deploy
 
    > mvn deploy
 
## Variables de entorno
Agregara las variables usadas por el proyecto
 
com.huawei.port = Puerto en el que correra la aplicacion
com.huawei.logs.location = Path absoluto de arhivo de log 
com.huawei.logs.filename = Nombre de arhivo de log
com.huawei.datastore.host = Host para conexion a base de datos
com.huawei.datastore.port = Puerto para conexion a base de datos 
 
## Versionado
Usamos SemVer para el versionado. Para todas las versiones disponibles, [mira los tags en este repositorio](https://git.paypertic.com/tu/proyecto/tags)
 
## Documentación
Links a documentos relacionados con el proyecto.
