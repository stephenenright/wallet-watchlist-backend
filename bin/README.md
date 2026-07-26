# Running and Testing the API

## Start the API
The application is packaged as a fat jar file which includes the java runtime required to execute the api.
It will also write to and read from an h2 file based database output to this directory.

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
A postman collection to test the API's can also be found here:

[postman-collection.json](../docs/postman-collection.json)
