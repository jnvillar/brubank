# brubank
[Brubank Challenge](https://brubank.herokuapp.com/)

# Requisitos:

- Mysql instalado y con el schema "brubank" creado
- Grails 3.3.5
- Java 8

Para instalar grails recomiendo instalar [sdk man](https://sdkman.io/)  y luego correr las líneas:

```
sdk install java 8u161-oracle; sdk install grails 3.3.5         
sdk use java 8u161-oracle; sdk use grails 3.3.5       
```

# Run

Para levantar la aplicacion simplemente hay que correr la línea

```
grails run-app
```

# Tests

Para correr los test

```
grails test-app
```

Son todos test unitarios, queda para un futuro agregar test de integracion.

# Endpoints

```
/correlation/jnvillar            Muestra la relacion entre repositorios y temperaturas
/cache/user                      Muestra el contenido de la cache de usuarios
/cache/user/flush                Limpia la cache de usuarios
/cache/averageTemperature        Muestra el contenido de la cache de temperaturas
/cache/averageTemperature/flush  Limpia la cache de temperaturas
```

# Decisicones

Las desiciones tomadas fueron las siguientes

###  Cache:

1. **Se utilizó una cache que guarda los datos en una base local. La misma cuenta con un ttl modificable**

##### Ventajas:
 - Se puede consumir la info cacheda desde otros servicios de manera sencilla, ya que estos podrian conectarse a la base
 - Si hay multiples instancias, pueden compartir la cache

##### Desventajas
- Es mas lento que una cache almacenada en memoria ram
- Si hay muchas instancias y no se desea compartir la cache, hay que crear una base de datos diferente cada vez que se genere esta situacion

2. **La cache de temperaturas promedio no se borra nunca. Tome esta desicion porque asumi improbable que la informacion sobre la temperatura cambie**

##### Ventajas:
- Una vez que se consulta sobre cierto dia, esta info queda para siempre

##### Desventajas
- Se puede llenar de basura (informacion que solo se consulta una vez) y crecer muy rápido

3. **Al consultar sobre un usuario, primero se verifica que el usuario ya exista en la cache**

##### Ventajas:
- Es mas rápido si el usuario esta en la cache

##### Desventajas
- Como la cache tiene un tll, no se van a observar nuevos repos hasta que el item no se borre (luego de 4 dias para usuarios de github)

4. **La cache implementa una interfaz**

##### Ventajas:
- En un futuro, si se desea cambiar la cache por otra, va a ser mas sensillo

##### A futuro

- Agregar un limite de tamaño maximo para la cache
- Guardar cantidad de hits
- Ir borrando los elementos menos hiteados de la cache con algun job
- Poder pedir que no se use un dato cacheado al momento de hacer el request (mandando un header, o parametros por url)
- Tener un job que corra una vez al dia que actualice la cache de los usuarios de github con los nuevos repos que estos crean

###  Excepciones:

1. **Cada vez que algun cliente lanza una exepcion, se guarda el mensaje y el estado. Luego se crea una excepcion propia que es handleada por el controller. Se termina mostrando el estado y el mensaje de la respuesta del lado del cliente**

##### Ventajas:
- Es sencillo y escalable

##### Desventajas
- Al solo haber un tipo de excepcion, se limita el control que se puede tener sobre las mismas

##### A futuro

- Crear mas tipos de excepciones para errores especificos
- En vez de fallar, se podria tener valores for defecto. Por ej, si un usuario de github no tiene ubicacion, usar la ubicacion desde la cual esta haciendo el request en caso de ser posible.

###  Clientes:

1. Los clientes heredan de un cliente abstracto, el cual encapsula la manera de relizar requests

##### Ventajas:
- Es sencillo y escalable
- Si se desea cambiar la forma de generar los requests, solo hay que cambiar la clase apiClient

##### A futuro
- Paralelizar los llamados a la api de clima, en vez de obtener de una fecha, se podrian lanzar multiples tasks que realicen los mismos






