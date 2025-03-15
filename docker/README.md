# TV archives aggregator

TV archive aggregator scrapping content from [telemagazyn.pl](https://telemagazyn.pl) and indexing
it with more comfortable search system.

[GitHub repository](https://github.com/milosz08/tv-archives-aggregator)
| [Support](https://github.com/sponsors/milosz08)

## Build image

```bash
docker build -t milosz08/tv-archives-aggregator-server
```

## Create container

* Using command:

```bash
docker run -d \
  --name tv-archives-aggregator-server \
  -p 8080:8080 \
  -e PM_WEB_PANEL_XMS=1024m \
  -e PM_WEB_PANEL_XMX=1024m \
  milosz08/tv-archives-aggregator-server
```

* Using `docker-compose.yml` file:

```yaml
services:
  tv-archives-aggregator-server:
    container_name: tv-archives-aggregator-server
    image: milosz08/tv-archives-aggregator-server
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
message: [personal@miloszgilga.pl](mailto:personal@miloszgilga.pl).

## License

This project is licensed under the Apache 2.0 License.
