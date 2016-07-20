# sales-temperature-v3

<p> Re-development "sales-temperature-v2" with Spring Framework </p>

<p> [Implementation Goal of this project] </p>
<p>
1. Replace with proper component for Impala / Redis </br>
   : Remove python-specific-background process layer - Celery </br>
   : Query with JDBCTemplate base on ImpalaJDBC connector.
</p>
<p>
2. Impala Query Enhancement with "User Defined Aggregate Function" implementation.</br>
   : To reduce sub-query <br>
   : To replace expensive Impala Built-in function usage.
</p>
<p>
3. Adaptation enhanced implementation : Kafka Message - Spark Streaming processing.
   : Realtime analysis approximately under 5 sec(batch-interval)
</p>
4. Considering DAO result(by Impala Query) reusing in application business logic.
   : Utilizing Session Bean Scope instead of DTO</br>
   : Need to do Session Bean Scope integration testing
   : To implement Web Service, use Spring-MVC instead of Jersey - Jersey dose not support "Session Bean Scope" of Spring.
<p>
5. TDD oriented development with JUnit </br>
   : Not only Unit Test but also Integration Test(Spring-mvc-test(MockMvc))
</p>