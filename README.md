# Phone Booking Service

The service provides REST API to book test phones as well as to obtain information about them.

## API Documentation

API documentation is available under the following URL:

    localhost:8080/swagger-ui/index.html

## Important Notes

### Persistence

Due to the demonstration character of this project the H2 in memory database was used. Therefore any changes to the bookings **WILL NOT** be persistent.
 
### Phones' specifications

As per the project requirements phones' technical details are supposed to be fetched from the FonoApi.

However, the API ( https://github.com/shakee93/fonoapi ) seems to be dead for the last 3 years. 
In the result the technical details of all the phones fallback to hardcoded `UNKNOWN` value.

## Running the project

### Prerequisites 

- gradle-8.0
- JDK 19

### To run the project locally  

    ./gradlew bootRun

