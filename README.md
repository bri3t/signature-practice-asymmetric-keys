# signature-practice-asymmetric-keys
Java keytool signature practice asymmetric keys

# FileSigner

## Introducción
FileSigner es una herramienta para firmar y verificar archivos utilizando un almacén de claves (keystore). Este programa permite a los usuarios realizar operaciones de firma digital y verificación de firmas para asegurar la integridad y autenticidad de los archivos.

## Instalación
Para instalar y ejecutar FileSigner, sigue los siguientes pasos:

1. Asegúrate de tener Java instalado en tu sistema.
2. Compila los archivos `.java`:
    ```sh
    javac -d . Keybreaker.java Logic.java
    ```
3. Ejecuta el programa con el comando:
    ```sh
    java main.Keybreaker <action> <inputFile> [--sigfile=sigFile] [--keystore=keystoreFile] [--alias=alias]
    ```

## Uso
### Comandos
- `sign <inputFile> --alias=<alias> [--sigfile=<sigFile>] [--keystore=<keystoreFile>]`: Firma un archivo.
- `verify <inputFile> --alias=<alias> [--sigfile=<sigFile>] [--keystore=<keystoreFile>]`: Verifica la firma de un archivo.
- `help`: Muestra la ayuda sobre el uso del programa.

### Ejemplo
Para firmar un archivo `document.txt`:
```sh
java main.Keybreaker sign document.txt --alias=myalias
```
Características

    Firma digital de archivos utilizando claves privadas del keystore.
    Verificación de firmas utilizando claves públicas del keystore.
    Soporte para especificar archivos de keystore y alias.

Dependencias

    Java Development Kit (JDK)
    Un archivo de keystore en formato JKS que contenga las claves necesarias.

Configuración
Parámetros

    --sigfile=<sigFile>: Especifica el archivo de salida para la firma. Si no se proporciona, se generará un archivo con la extensión .md5.
    --keystore=<keystoreFile>: Especifica el archivo del keystore. El valor por defecto es keystorage.jks.
    --alias=<alias>: Especifica el alias de la clave en el keystore.

Ejemplos
Firma de un archivo

```sh

java main.Keybreaker sign document.txt --alias=myalias [--sigfile=document.md5] [--keystore=mykeystore.jks]
```
Verificación de una firma

```sh

java main.Keybreaker verify document.txt --alias=myalias [--sigfile=document.md5] [--keystore=mykeystore.jks]
```

