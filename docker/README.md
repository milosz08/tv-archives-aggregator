# TV archives aggregator client

TV archive aggregator scrapping content from [telemagazyn.pl](https://telemagazyn.pl) and indexing
it with more comfortable search system.

[GitHub repository](https://github.com/milosz08/tv-archives-aggregator)
| [Support](https://github.com/sponsors/milosz08)

## Build image

```bash
docker build -t milosz08/tv-archives-aggregator-client
```

## Create container

* Using command:

```bash
docker run -d \
  --name tv-archives-aggregator-client \
  -p 8080:8080 \
  -e DB_HOST=<database hostname> \
  -e DB_PORT=<database port> \
  -e DB_NAME=<database name> \
  -e DB_USERNAME=<database username> \
  -e DB_PASSWORD=<database password> \
  -e TV_ARCHIVES_AGGREGATOR_XMS=1024m \
  -e TV_ARCHIVES_AGGREGATOR_XMX=1024m \
  milosz08/tv-archives-aggregator-client
```

* Using `docker-compose.yml` file:

```yaml
services:
  tv-archives-aggregator-client:
    container_name: tv-archives-aggregator-client
    image: milosz08/tv-archives-aggregator-client
    ports:
      - '8080:8080'
    environment:
      DB_HOST: <database hostname>
      DB_PORT: <database port>
      DB_NAME: <database name>
      DB_USERNAME: <database username>
      DB_PASSWORD: <database password>
      TV_ARCHIVES_AGGREGATOR_XMS: 1024m
      TV_ARCHIVES_AGGREGATOR_XMX: 1024m
    networks:
      - tv-archives-aggregator-network

  # other containers...

networks:
  tv-archives-aggregator-network:
    driver: bridge
```

## Author

Created by Miłosz Gilga. If you have any questions about this application, send
message: [miloszgilga@gmail.com](mailto:miloszgilga@gmail.com).

## License

This project is licensed under the Apache 2.0 License.
