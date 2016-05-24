# sales-temperature-v3

Re-development "sales-temperature-v2" with Spring Framework

[Goal of this project]
1. Replace with proper component for Impala / Redis
   : Remove python-specific-background process layer - Celery
2. TDD oriented development with JUnit
3. Considering proper Design Pattern - eg. Template/Callback, Proxy, AOP
   : [TBD] Redis cache business logic with AOP.
4. Imagine the diifferent owner for each technical layer 
   : eg. person #1 taking charge of implementing Redis Data Cache logic
         person #2 taking charge of impelmenting Data Analysis logic with Impala query
         person #3 taking cahrge of implementing standard 3-tier(Service/Dao/Template(View)) layer
5. [Optional] Adaptation enhanced implementation : Kafka-Spark processing concept
