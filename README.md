# SpringQuiz # 
<p> <!-- description of the project --> 
SpringQuiz is a quiz game implemented with micro-service architecture using 
springboot.

</p>

## Services:  
### main components 
#### Quiz service 

<p>This module takes care of the actual questions, categories and quizzes and handles
the creation, deletion and delivery of quizzes as well as checking submitted answers.

There is a more detailed description of this module available 
[here](quiz/README.md).
</p>

#### Highscore service 
#### User service 

## Load balancing and routing ##
#### eureka 
<p>
Netflix eureka is used the load-balance all springboot instances. 
Quiz and User is running on 1 instance while highscore runs with 2.  
</p>

#### zuul 
<p>
Netflix zuul is used as a gateway and is the single service that is open for 
outside connections. It is running on port 80 and provides the following routes.
<br/>
<code> quiz/** </code> 
<br/>

All routes to the quiz service. See link below for more details. 
[quiz service](quiz/README.md)  


<code> user/details/** </code>
<br/>

All routes to the user service see link below for more details. 
[user service](link to user readme)

<code> highscore/** </code>

<br/>

All routes to the highscore service see link below for more details.
[highscore service](link to highscore readme)

</p>

### Security ###
<p>
Zuul is the single entrypoint and no other ports than 80 can be accessed outside of
the overlay network docker provides. Zuul also implements basic authentication and has 
a api called auth handling requests. <br/>

<code> POST /register </code> (register a new user) <br/>
<code> POST /signIn </code> (sign in as existing user) <br/>
<code> GET /user </code> (get information about the authenticated user) <br/>
<br/>

<i><b> Basic auth: </b></i> <br/>
Authentication is handled by spring security and the sessions 
are stored in a redis store which is shared among the components 
that will need it. 

The actual user credentials(username, password etc) is stored in a postgresql 
db.

<i><b> XSRF protection: </b></i> <br/>

</p>

## Other features

#### AMQP ####
<p>
 The quiz module communicates with the highscore using AMQP(rabbitmq). Highscore subscribes to the
 queue containing new entries to be inserted(patched) into the database of the highscore service.
 
 <br/>
 Go to the following links to see the implementations: 
 <br/>
 
 [(producer in quiz) /quiz/api/quizzes/{id}/check](quiz/src/main/kotlin/no/group3/springQuiz/quiz/api/QuizController.kt) 
 
 [(receiver in highscore) AMQPScoreListener](highscore/src/main/kotlin/no/group3/springQuiz/highscore/AMQPScoreListener.kt)
 
 This feature is also included in both of the tests in the [e2e module](e2e/src/test/kotlin/no.group3.SpringQuiz.e2e)
 
 
</p>


## Testing: ##
#### e2e ####
