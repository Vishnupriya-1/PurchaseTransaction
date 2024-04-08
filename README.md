Purchase Transaction application is to store the recently purchased transaction and to retrieve the purchased transaction with the currency conversion supported by the Treasury Reporting Rates of Exchange API based upon the exchange rate active for the date of the purchase.

How to Run:-

You can run with the command "mvn spring-boot:run"

Alternatively you can run Docker compose file "docker-compose.dev.yml"

Version Used:-

Java 17 Spring boot 3.2.4

Clone this repository:-

Clone the PurchaseTransaction repository and build the project using the command "mvn spring-boot:run"

Check the myapp.log file to make sure no exceptions are thrown Once the application runs you should see something like this

server-1  | 2024-04-08T20:43:00.116Z  INFO 1 --- [transaction] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
server-1  | 2024-04-08T20:43:00.138Z  INFO 1 --- [transaction] [           main] c.p.transaction.TransactionApplication   : Started TransactionApplication in 5.419 seconds (process running for 5.891)
server-1  | 2024-04-08T20:43:12.986Z  INFO 1 --- [transaction] [nio-8080-exec-2] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'

About the Service:-

The service is a purchase transaction REST service. It uses an in-memory database (H2) to store the data. You can also do with any database. If your database connection properties work, you can call some REST endpoints defined in com.routePlanner.htc.controller; on port 8080. (see below)

Here are the endpoints you can call:-

POST: /createTransaction -  to store the purchase transaction
http://localhost:8080/createTransaction 
Request Body:
{
    "description":"test",
    "transactionDate":"1/1/2020",
    "purchaseAmount":"10.12"
}
Response: HTTP 201 {
    "id": 2,
    "description": "test2",
    "transactionDate": "01/01/2020",
    "purchaseAmount": 10.12
}

GET: http://localhost:8080/transaction/Australia/dollar - to retrieve all the transactions with the converted amount for the provided country's currency
Response: HTTP 200 [
    {
        "id": "1",
        "description": "test",
        "transactionDate": "2020-01-01",
        "purchaseAmount": "10.12",
        "exchangeRate": null,
        "convertedAmount": null,
        "errorMsg": "purchase cannot be converted to the target currency"
    }
]

To view H2 in-memory database:-

Application runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8080/h2-console. Default username is 'sa' with password as 'password'.
