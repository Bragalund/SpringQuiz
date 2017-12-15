# Highscore-service #

 ### <i>Description</i> ###
 <p> The highscore microservice is made to keep track of all the best scores from the quiz.

 ### <i> RESTful </i> ###
 <p> The api is made with level 2 standards of RESTful api. It follows rules for the URL and HTTP verbs. My url is /highscore and not /highscores because it will always be one list over the best scores,
 not multiple. It still could probably make more sense to have it as /highscores according to REST standards, but I just left it at /highscore
  </p>

 ### <i> Swagger </i> ###
  I have implemented Swagger and you can view it in the following url
  <br/>
  <i> /highscore/swagger-ui.html </i>

  ![alt text](pictures/swagger.png "swagger representation")


 ### <i>Clarifications about the endpoints</i> ###
 <p>
    I have implemented all five http verbs required in the ScoreApi class. <br/>
    <code> GET /highscore </code> <br/>
    The GET method returns all scores and sorts them with the highest score on top using the sortedByDescending function. <br/>
    <code> POST /highscore </code> <br/>
    The POST method creates a new score and adds it to the database, and the method returns the id of the created score with a 201 response <br/>
    <code> DELETE /highscore/{id} </code> <br/>
    The DELETE method takes in a id of the score it wants to delete and then removes it from the database with a 204 response<br/>
    <code> PUT /highscore/{id} </code> <br/>
    The PUT method find a score it wants to replace and puts a new resource over it and returns 204<br/>
    <code> PATCH /highscore/{id} </code> <br/>
    The PATCH method gets in a score it wants to change with an id, and then it swaps that value for the new one. Returns 204 <br/>
 </p>


 ### <i>Tests</i> ###
 #### Rest Assured ####
 I have made RestAssured tests for all of my endpoints. <br/>
 My GET test was kinda tricky so I used an assertEquals to the expected output. <br/>
 It is not the best way to do it, but it works. <br/>

 #### TestBase ####
 I have two methods in the ScoreTestBase class which makes testing easier. <br/>
 Both methods are a post and they return either a dto or the id to the created score.