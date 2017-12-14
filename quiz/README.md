# Quiz-service #
 
 ### <i>Description</i> ###
 <p> The quiz module is a rest-service for handling quizzes. It contains 3 rest-controllers one for
 each model entity: Quiz, Question, Category. 
 
 ### <i> RESTful </i> ###
 <p> The controllers has been implemented according to REST lvl 2. They follow the specifications 
 for HTTP and the URL is defined to follow the principals of RESTful API implementation standards.
 Level 2 since we did not find it necassary with HATEOS as it would only add complexity and increase
 the traffic between our services without actually adding any value.  
  </p>
 
 ### <i> Swagger </i> ###
  Swagger is implemented and can be viewed at the path below when the system is running with 
  docker-compose.
  <br/> 
  <i> /quiz/api/swagger-ui.html </i>
 
 ![alt text](doc/swagger-screen.png "swagger representation of the endpoints described 
 in the text above") 

 
 ### <i>Clarifications about the endpoints</i> ### 
 <p>
    <b><i>category-controller:</i></b> Is selfexplanatory GET, POST and DELETE to perform what the 
    HTTP-verbs describes. <br/>
    <b><i>question-controller:</i></b> Contains one endpoint for every common HTTP-verb GET, POST, 
     PUT, PATCH and DELETE.
 </p>
 
 
 ### <i>Tests</i> ###
 
 