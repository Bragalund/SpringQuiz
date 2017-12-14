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
    <b><i>category-controller:</i></b> <br/> Is selfexplanatory GET, POST and DELETE to perform what the 
    HTTP-verbs describes. <br/>
    <b><i>question-controller:</i></b> <br/> Contains one endpoint for every common HTTP-verb GET, POST, 
     PUT, PATCH and DELETE. <br/>
    <b><i>quiz-controller:</i></b> <br/> 
    <code> GET /quizzes </code> <br/> 
     can take an extra param using 
    <code> /quizzes?category={category} </code> <br/>
    will get all quizzes from a given category.  <br/> <br/>
    <code> POST /quizzes/{id}/check </code> <br/>
    This endpoint is used to submit answers to the quiz given by the id. It will
    calculate the score based on the nr of correct answers given multiplied with 
    the difficulty of the quiz(1-3). 
    <br/>
    This endpoint will publish the calculated score along with the username of
    the playing user to rabbitmq. It will then eventually end up in the highscore
    service.
    
 </p>
 
 
 ### <i>Tests</i> ###
 
 