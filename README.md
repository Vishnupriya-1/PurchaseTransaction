Purchase Transaction application is to store the recently purchased transaction and to retrieve the purchased transaction with the currency conversion supported by the Treasury Reporting Rates of Exchange API based upon the exchange rate active for the date of the purchase.

How to Run:-

You can run with the command "mvn spring-boot:run"

Alternatively you can run Docker compose file "docker-compose.dev.yml"

Version Used:-

Java 17 Spring boot 3.2.4

Clone this repository:-

Clone the HTC_Planner repository and build the project using the command "mvn spring-boot:run"

Check the myapp.log file to make sure no exceptions are thrown Once the application runs you should see something like this

2023-08-03T11:13:15.039+01:00 INFO 36080 --- [ main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8090 (http) with context path '' 2023-08-03T11:13:15.050+01:00 INFO 36080 --- [ main] com.routePlanner.htc.HtcApplication : Started HtcApplication in 5.718 seconds (process running for 6.248)

About the Service:-

The service is a route planner REST service. It uses an in-memory database (H2) to store the data. You can also do with any database. If your database connection properties work, you can call some REST endpoints defined in com.routePlanner.htc.controller; on port 8090. (see below)

Here are the endpoints you can call:-

GET: /transport/{distance}?passengers={number}&parking={days} - returns the cheapest vehicle to use (and the cost of the journey) for the given distance (in AUs), number or passengers and days of parking
http://localhost:8090/transport/2?passengers=8&parking=0 Response: HTTP 200 Personal Transport is the cheapest vehicle to use with the journey cost of £1.2

GET: /accelerators - returns a list of accelerators with their information
Response: HTTP 200 [ { "id": 1, "accelerator_id": "SOL", "accelerator_name": "Sol", "connections": "{"RAN":"100","PRX":"90","SIR":"100","ARC":"200","ALD":"250"}" }, { "id": 2, "accelerator_id": "PRX", "accelerator_name": "Proxima", "connections": "{"SOL":"90","SIR":"100","ALT":"150"}" }, { "id": 3, "accelerator_id": "SIR", "accelerator_name": "Sirius", "connections": "{"SOL":"80","PRX":"10","CAS":"200"}" }, { "id": 4, "accelerator_id": "CAS", "accelerator_name": "Castor", "connections": "{"SIR":"200","PRO":"120"}" }, { "id": 5, "accelerator_id": "PRO", "accelerator_name": "Procyon", "connections": "{"CAS":"80" }" }, { "id": 6, "accelerator_id": "DEN", "accelerator_name": "Denebula", "connections": "{"PRO":"5","ARC":"2","FOM":"8","RAN":"100","ALD":"3"}" }, { "id": 7, "accelerator_id": "RAN", "accelerator_name": "Ran", "connections": "{"SOL":"100"}" }, { "id": 8, "accelerator_id": "ARC", "accelerator_name": "Arcturus", "connections": "{"SOL":"500","DEN":"120"}" }, { "id": 9, "accelerator_id": "FOM", "accelerator_name": "Fomalhaut", "connections": "{"PRX":"10","DEN":"20","ALS":"9"}" }, { "id": 10, "accelerator_id": "ALT", "accelerator_name": "Altair", "connections": "{"FOM":"140","VEG":"220"}" }, { "id": 11, "accelerator_id": "VEG", "accelerator_name": "Vega", "connections": "{"ARC":"220","ALD":"580"}" }, { "id": 12, "accelerator_id": "ALD", "accelerator_name": "Aldermain", "connections": "{"SOL":"200","ALS":"160","VEG":"320"}" }, { "id": 13, "accelerator_id": "ALS", "accelerator_name": "Alshain", "connections": "{"ALT":"1","ALD":"1"}" } ]

GET: /accelerators/{acceleratorID}- returns the details of a single accelerator
http://localhost:8090/accelerators/SOL Response: HTTP 200 { "id": 1, "accelerator_id": "SOL", "accelerator_name": "Sol", "connections": "{"RAN":"100","PRX":"90","SIR":"100","ARC":"200","ALD":"250"}" }

GET: /accelerators/{acceleratorID}/to/{targetAcceleratorID} - returns the cheapest route from acceleratorID to targetAcceleratorID
http://localhost:8090/accelerators/SOL/to/VEG Response: HTTP 200 [ "SOL", "PRX", "ALT", "VEG" ]

To view H2 in-memory database:-

Application runs on H2 in-memory database. To view and query the database you can browse to http://localhost:8090/h2-console. Default username is 'sa' with password as 'password'.
