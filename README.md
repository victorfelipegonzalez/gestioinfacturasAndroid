# Factur Expres

Esta aplicación móvil permite a las empresas registrar y gestionar datos de clientes, productos y facturación de manera eficiente.

## Características

- Registro y Gestión de Clientes: Permite a los usuarios registrar información detallada de clientes, incluyendo nombre, contacto, dirección, etc. Además, ofrece funcionalidades para la gestión eficiente de la información de los clientes, como búsqueda por nombre y visualización ordenada alfabéticamente.
- Gestión de Productos: Permite a los usuarios añadir productos al inventario, junto con información relevante como descripción y precio
- Facturación Integrada: Ofrece la capacidad de generar y enviar facturas a los clientes directamente desde la aplicación. Se integra con Jasper Report para generar facturas profesionales y otros informes necesarios para la gestión empresarial.
- Sistema de Autenticación Seguro: Utiliza Firebase Authentication para garantizar la seguridad y protección de los datos del usuario. Los usuarios pueden iniciar sesión de manera segura y acceder a sus datos desde cualquier dispositivo.
- Interfaz de Usuario Intuitiva: Diseñada con una interfaz de usuario amigable e intuitiva para facilitar la navegación y el uso de la aplicación, incluso para usuarios no técnicos.
- Gestión de Empleados: Permite a los administradores gestionar la información de los empleados, incluyendo sus roles, y otra información relevante para la gestión de recursos humanos.
- Generación de Informes: Proporciona herramientas para generar informes detallados sobre las ventas, ingresos para la toma de decisiones empresariales.

## Instalación

Para utilizar esta aplicación, sigue estos pasos:

1. Descarga el código fuente desde este repositorio.
2. Abre Android Studio.
3. Conecta tu dispositivo Android al ordenador.
4. En tu dispositivo, habilita el modo desarrollador y la depuración USB.
5. Ejecuta la aplicación desde Android Studio y selecciona tu dispositivo como destino de implementación.

## Uso

Una vez instalada la aplicación en tu dispositivo, puedes abrir la aplicación y comenzar a utilizar sus funciones.

Al ejecutar la aplicación por primera vez, el proceso inicial implica registrarse, proporcionando los datos necesarios de la empresa para acceder al menú principal.
Una vez registrados, podremos accedemos al menú principal, que ofrece una variedad de opciones para gestionar la información de la empresa, para ello debemos introducir las credenciales, correo y contraseña:
1.	Registro y Gestión de Clientes: La aplicación permite registrar clientes, introduciendo sus datos esenciales para futuras transacciones. Además, ofrece la funcionalidad de visualizar la información de los clientes almacenados, ordenados alfabéticamente. Se puede realizar una búsqueda por nombre para acceder rápidamente a la información deseada. Desde la visualización de un cliente, se puede acceder a su número de teléfono y correo electrónico para comunicarse directamente con ellos.
2.	Registro de Productos: La aplicación ofrece la posibilidad de añadir productos, incluyendo una descripción breve y el precio unitario sin IVA. Todos los productos añadidos se almacenan en la base de datos para su uso posterior en la generación de facturas.
3.	Gestión de Empleados: Los empleados pueden registrarse desde la pantalla de login. Una vez registrados, se pueden asociar a la empresa mediante su correo electrónico y asignarles un rol específico que determina su funcionalidad en la aplicación.
4.	Generación de Facturas: Para generar una factura, se selecciona al cliente destinatario mediante una búsqueda por nombre. Luego se añaden los productos a la factura, buscándolos por nombre y especificando la cantidad deseada. Una vez completada, la factura se almacena en la base de datos y se envía por correo electrónico al cliente.
5.	Visualización de Facturas e Informes: Los administradores tienen acceso a tres secciones para la visualización de facturas e informes: facturas de clientes, facturas realizadas por empleados e informes. Los empleados solo tienen acceso a la sección de facturas de clientes. La búsqueda se puede realizar por nombre de cliente o empleado, según la sección correspondiente. Además, se pueden generar informes que detallan el número total de facturas generadas por la empresa y por cada empleado, organizados por fecha.
6.	Informe de Facturación Anual: Se proporciona una sección para buscar facturas por fecha, permitiendo seleccionar el año de facturación. Se puede visualizar un informe de facturación anual que detalla el total de facturas generadas por la empresa y por cliente, lo que facilita el análisis de consumo y rendimiento del negocio.
7.	Funcionalidad de Desconexión: Para mayor comodidad, se incluye un menú en la pantalla principal que permite desconectarse y volver a la pantalla de inicio de la aplicación.
Estas funcionalidades proporcionan una experiencia completa y eficiente para la gestión de la empresa, ofreciendo herramientas poderosas para la generación de informes, seguimiento de clientes y control de facturación.

## Tecnologías Utilizadas

- Android Studio
- IntelilliJ Idea
- Java
- Firebase Authentication
- Spring Boot
- PostgreSQL
- Jasper Report

## Contribución

Actualmente no aceptamos contribuciones externas en este proyecto.

## Licencia
Derechos de autor 2024, Víctor Felipe González

Se concede permiso, de forma gratuita, a cualquier persona que obtenga una copia de este software y archivos de documentación asociados a Factur Expres, para tratar el Software sin restricción, incluidos, entre otros, los derechos de uso, copia, modificación y fusión , publicar, distribuir, sublicenciar y / o vender copias del Software, y permitir a las personas a quienes se les proporciona el Software que lo hagan, sujeto a las siguientes condiciones:

El aviso de derechos de autor anterior y este aviso de permiso se incluirán en todas las copias o partes sustanciales del Software.

EL SOFTWARE SE PROPORCIONA "TAL CUAL", SIN GARANTÍA DE NINGÚN TIPO, EXPRESA O IMPLÍCITA, INCLUIDAS, ENTRE OTRAS, LAS GARANTÍAS DE COMERCIABILIDAD, IDONEIDAD PARA UN PROPÓSITO PARTICULAR Y NO INFRACCIÓN. EN NINGÚN CASO LOS AUTORES O PROPIETARIOS DE DERECHOS DE AUTOR SERÁN RESPONSABLES DE NINGUNA RECLAMACIÓN, DAÑOS U OTRAS RESPONSABILIDADES, YA SEA EN UNA ACCIÓN DE CONTRATO, AGRAVIO O DE OTRO MODO, DERIVADAS DE, FUERA DE O EN CONEXIÓN CON EL SOFTWARE O EL USO U OTROS TRATOS EN EL SOFTWARE.
