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

<p>The highscore module can create a new score and then sort all scores with the
highest score on top.

You can view the documentation of this module
[here](highscore/README.md).
</p>

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
All PUT, POST and PATCH methods that is routed through zuul are secured from XSRF 
attacks, this is proven in the e2e tests where unsafe methods has XSRF-token in both
the header and the cookie.

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
<p>
Quiz, highscore and user modules all contains rest assured tests. Atleast one per endpoint
 all the test can be run seperately using <code> mvn package </code> in the given module.
 To run all tests run use the same command in the root module. 
 <br/>
 To run all test including e2e run <code>mvn install</code> in root.
</p>

#### e2e ####
<p>
The e2e tests can be found in the e2e module and consist of one test class that performs
test with the whole system running. 
<br/>
The system test is using the docker-compose file in the root folder and is quite heavy to run
since it starts 12 docker containers. If this test is run on a computer with low CPU and/or
 available memory, it might actually fail.
<br/>
The test will take about 2-3 minutes to start up all the services. This is partly because
of all the containers and partly because eureka takes some time to get all instances registered.
<br/> 
<br/>
Because of the fact that this might happen a link to when the test are run on a computer
with sufficient CPU and memory is available(to prove that they actually work) here:
<br/>
[link to youtube]


[amqp-test](e2e/src/test/kotlin/no.group3.SpringQuiz.e2e/HighscoreQuizAmqpIT.kt)
<br/> 
[system-test](e2e/src/test/kotlin/no.group3.SpringQuiz.e2e/SpringQuizIT.kt)

</p>

## Other ##

### Git Repo ###
<p>
Our git repo for the project can be found here https://github.com/Bragalund/SpringQuiz
</p>


### Contributions ###

#### Joakim ####
<p>
Github Username: josoder <br/>
Main service: Quiz
<br/>
Additional features:
AMQP, 
eureka,
zuul, 
e2e, 
docs,
docker-compose,
security
</p>

#### Henrik ####
<p>
Github Username: Bragalund <br/>
Main service: User
</p>

#### Johannes ####
<p>
Github Username: husjoh15 <br/>
Main service: Highscore
</p>
