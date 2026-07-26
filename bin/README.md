# Running and Testing the API

## Start the API
The application is packaged as a fat JAR file which includes the Java runtime required to execute the API.
It will also write to and read from an H2 file-based database in this directory.

To run the app from a Linux or macOS terminal:

```bash
./run.sh
```

Or from Windows:

```bat
run.bat
```

## Testing the API

The application can be tested via the Swagger UI:

http://localhost:8081/swagger-ui/index.html


## Postman collection
A Postman collection to test the APIs can also be found here:

[postman-collection.json](../docs/postman-collection.json)
