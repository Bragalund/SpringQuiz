# Highscore-service #

 ### <i>Description</i> ###
 <p> The highscore microservice is made to keep track of all the best scores from the quiz.

 ### <i> RESTful </i> ###
 <p> The api is made with level 2 standards of RESTful api. It follows rules for the URL and HTTP verbs.
  </p>

 ### <i> Swagger </i> ###
  I have implemented Swagger and you can view it with running docker-compose.
  <br/>
  <i> /highscore/swagger-ui.html </i>


 ### <i>Clarifications about the endpoints</i> ###
 <p>
    I have implemented all five http verbs required in the ScoreApi class. <br/>
    <code> GET /highscore </code> <br/>
    My GET method returns all scores and sorts them with the highest score on top using the sortedByDescending function. <br/>
    My Patch method gets in a score it wants to change, and
 </p>


 ### <i>Tests</i> ###