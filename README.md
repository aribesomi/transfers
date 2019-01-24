# Transfers - JAVA RESTful API

Java RESTFul API to transfer money between accounts - Revolut Backend Test

## Frameworks Used

1. Jetty
2. JAX-RS
3. Log4j2
4. H2 in memory DB
5. JUnit for testing

## Services

The API consists in 2 services. One to manage accounts (CRUD) and one to List/Find/Make transfers. It runs on port **8080** and the available services are the following.

### Account Services
  
#### List all accounts
```
URL: /api/accounts/list
METHOD: GET
```

#### Find account by id
```
URL: /api/accounts/{id}
METHOD: GET
```

#### Update an account
```
URL: /api/accounts/{id}
METHOD: PUT
CONTENT-TYPE: application/json
BODY: 
{
  "balance" : 1234.56,
  "currency": "EUR",
  "accountIdentification": "12345678909876543210"
}
```

#### Create an account
```
URL: /api/accounts/create
METHOD: POST
CONTENT-TYPE: application/json
BODY: 
{
  "balance" : 1234.56,
  "currency": "EUR",
  "accountIdentification": "12345678909876543210"
}
```

#### Delete an account by id
```
URL: /api/accounts/{id}
METHOD: DELETE
```

### Transfer Services

#### List all transfers
```
URL: /api/transfers/list
METHOD: GET
```

#### Find transfer by id
```
URL: /api/transfers/{id}
METHOD: GET
```


#### Make a transfer between accounts
```
URL: /api/transfers/make-transfer
METHOD: POST
CONTENT-TYPE: application/json
BODY: 
{
  "from" : {
    "id": 1
  },
  "to" : {
    "id": 2
  },
  "value" : 1234.56,
  "description": "TEST TRANSFER"
}
```


## How to use/run

Although the API is provided with JUnit tests, the best way to take a look at the API is to run it with some of the following commands and then use examples shown above.

1. The simplest way is using maven exec command. Download the project and then in its folder run the following command.
```
mvn exec:java
```

2. Compile and install JAR with maven and then use JAVA to run the project.
```
mvn clean install
cd path/to/installed/jar
java -jar transfers-1.0.0.jar
```
