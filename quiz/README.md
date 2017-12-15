# Quiz-service #
 
 ### <i>Description</i> ###
 <p> The quiz module is a rest-service for handling quizzes. It contains 3 rest-controllers one for
 each model entity: Quiz, Question, Category. 
 <br/>
  The reason why there is so many endpoints(in 3 different controllers is because it is 
  meant to be handled with a GUI. Where an administrator can create categories add question 
  to it and then compose quizzes out of those questions. It would make sense to be able to have 
  multiple ways of sorting the questions, categories and quizzes and thats why it is implemented
  with three seperate controllers.
  <br/>
 
 
 </p>
 
 ### <i> RESTful </i> ###
 <p> The controllers has been implemented according to REST lvl 2. They follow the specifications 
 for HTTP and the URL is defined to follow the principals of RESTful API implementation standards.
 Level 2 since we did not find it necassary with HATEOS as it would only add complexity and increase
 the traffic between our services without actually adding any value.  
  </p>
 
 ### <i> Swagger </i> ###
  <p> Swagger is implemented and can be viewed at the path below when the system is running with 
  docker-compose.
  <br/> 
  <i> /quiz/api/swagger-ui.html </i>
 </p>
 
 ![alt text](doc/swagger-screen.png "swagger representation of the endpoints described 
 in the text above") 

 
 ### <i>Clarifications about the endpoints</i> ### 
 <p>
    <b><i>category-controller:</i></b> <br/> Is selfexplanatory GET, POST and DELETE to perform what the 
    HTTP-verbs describes. <br/>
    <b><i>question-controller:</i></b> <br/> 
    Contains one endpoint for every common HTTP-verb GET, POST, PUT, PATCH and DELETE. <br/>
    <b><i>quiz-controller:</i></b> <br/> 
    <code> GET /quizzes </code> <br/> 
     can take an extra param(not required) using 
    <code> /quizzes?category={category} </code> <br/>
    will get all quizzes from a given category.  <br/> <br/>
    <code> POST /quizzes/{id}/check </code> <br/>
    This endpoint is used to submit answers to the quiz given by the id. It will
    calculate the score based on the nr of correct answers given multiplied with 
    the difficulty of the quiz(1-3). 
    <br/>
    This endpoint will publish the calculated score along with the username of
    the playing user to rabbitmq. It will then eventually end up in the highscore
    service. See root docs for more info on this feature.
    </p>
    
   [root docs](../README.md)
 
 Besides the features described above the rest is self-explanatory. For more information
 about the endpoints swagger is as mentioned available as well. 
 
 ### DB ### 
 Postgres is used when running in production, an image dedicated to this module is started
 with docker-compose. 
 <br/>
 The tests however is running with h2. 
 
 </br>
 For the purpose of the e2e tests, to make it easier to test, the instance will get pre-loaded
 with 2 quizzes. 
 
 ### <i>Tests</i> ###
 ##### rest assured #####
 For each endpoint in the quiz-modul there is a corresponding rest assured test.
 All the tests can be found 
 [here](/src/test/kotlin/no/group3/springQuiz/quiz/api/QuizApiTest.kt).
 To run the tests use
 <code> mvn package </code> <br/>
 
 ##### e2e ##### 
 The endpoint <code>POST /quizzes/{id}/check </code> is tested in the 
 [e2e modul](../e2e/src/test/kotlin/no.group3.SpringQuiz.e2e/HighscoreQuizAmqpIT.kt)
 <br/>
 <br/>
 Quiz is also tested in this [e2e](../e2e/src/test/kotlin/no.group3.SpringQuiz.e2e/Quize2eIT.kt)
 which is a form om production test that simulates a game with the whole system
 running.
 <br/>
 To run the e2e tests use the following in the e2e module:
 <br/>
 <code>mvn verify</code> <br/>
 or 
 <code>mvn clean install</code> in root folder.