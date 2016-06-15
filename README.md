# sales-temperature-v3

<p> Re-development "sales-temperature-v2" with Spring Framework </p>

<p> [Goal of this project] </p>
<p>
1. Replace with proper component for Impala / Redis </br>
   : Remove python-specific-background process layer - Celery </br>
   : Query with JDBCTemplate
</p>
<p>
2. TDD oriented development with JUnit </br>
   : Jersey Test Framework / Spring D
   : Not only Unit Test but also Integration Test(with Jersey Test Framework / Spring-mvc-test(MockMvc))
</p>
<p>
3. Considering proper Design Pattern - eg. Template/Callback, Proxy, AOP </br>
   : [TBD] Redis cache business logic with AOP.
</p>
<p>
4. Imagine the diifferent owner for each technical layer </br>
   : eg. person #1 taking charge of implementing Redis Data Cache logic </br>
         person #2 taking charge of impelmenting Data Analysis logic with Impala query </br>
         person #3 taking cahrge of implementing standard 3-tier(Service/Dao/Template(View)) layer </br>
</p>
<p>
5. Adaptation enhanced implementation : Kafka-Spark processing concept
</p>
6. Considering Bean Scope base DTO(exactly not) usage purpose. </br>
   : Need to do Session Bean Scope integration testing
