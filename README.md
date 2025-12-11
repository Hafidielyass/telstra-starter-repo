# Telstra Starter Repo :bird:

## Running the actuator stub

The provided actuator service JAR lives in `services/`. Run it with Java 11 in a separate terminal:

```bash
java -jar services/actuator.jar
```

It starts on `http://localhost:8444/actuate` and accepts `POST` payloads like `{"iccid":"<value>"}`.

## Running this service

Build and start on Java 11:

```bash
./mvnw spring-boot:run
```

## Activating a SIM

Send a `POST` to `http://localhost:8080/simcards/activate`:

```json
{
  "iccid": "1234567890123456789",
  "customerEmail": "customer@example.com"
}
```

The service will relay the ICCID to the actuator, log the result, and respond with:

```json
{
  "success": true
}
```
