# Senior Back-end Developer Tech Test

## Purpose

The goal of this test is to provide us with a full understanding of your coding style and skills.
We’ll pay particular attention to: code structure, design, choice of data structures, quality
and use of testing, git usage, http response codes.
The goal is not to get a solution covering all special cases in a 100% robust way; the
functions should be error free when used correctly but our main goal is to understand your
approach to the problem.

## Development Test

We would like to receive an implementation of a REST Api with the following methods:

**Login**: This endpoint returns a session key in the form of a string which shall be valid for use with
the other functions for 10 minutes. The session keys should be unique.
```
Request: POST /login
Request body: <username> and <password>
Response: <uniquesessionkey>
Code: 202
```
**Add Level Scores**: This other endpoint can be called several times per user and level and does not return
anything. Only requests with valid session keys shall be processed.
```
Request: PUT /level/3/score/1500
Set session key in header with name Session-Key
Response: (nothing)
Code: 204
```    
**High score list for a level**: Retrieves the high scores for a specific level for each user.
```
Request: GET /level/3/score?filter=highestscore
Response: JSON of array <userid> and <score>
Code: 200
```

## Tech Stack Required

We purpose you two tech stacks one for JS and another one for Java, pick up which you
could feel more comfortable:

Stack 1:
* Java 8+
* Spring Boot
* Junit and/or Cucumber

Stack 2:
* NodeJs
* NestJs
* Mocha and/or Chai
A plus would be to use an inmemory database.

## Deliverable

Take your time to deliver a solution that fairly represents your code skills.
We expect to receive a link to a git repo where we can do a clone. Make sure all steps
needed for the project to be executed are properly specified on ReadMe.

### Execution

* Download git: https://git-scm.com/downloads
* Download MongoDB Community Server: https://www.mongodb.com/download-center/community
* Create a database (localhost:27017) named scores with a collection named users with the following user document:
```
userName : "user1"
password : "$2a$10$xBq5u8YyQ1zMewSEu7SDD.fZ79x0YFOVyzTRfLOYi5S6M/PI6kSPG"
```
Original password before encoding is: changeit1 (PasswordEncodeTest could be used for encoding more passwords, in order 
to create more users)

Clone scores repo:
```
$ git clone https://github.com/llucbb/scores.git
$ cd scores
```

Execute scores app:
```
$ mvn spring-boot:run
```