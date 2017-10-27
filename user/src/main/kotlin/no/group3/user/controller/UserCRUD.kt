package no.group3.user.controller

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val BASE_JSON = "application/json;charset=UTF-8"
const val V2_NEWS_JSON = "application/no.group3.user+json;charset=UTF-8;version=2"

// Creates base url. example: www.someurl.com/user
@Api(value = "/user", description = "CRUD for User") //Creates documentation of API with Swagger
@RequestMapping(
        path = arrayOf("/user"),
        produces = arrayOf(
                V2_NEWS_JSON, //custom Json with versioning
                BASE_JSON //old format
        )
)
@RestController
class UserCRUD {

        //Get one user with Id

        //Delete user with Id

        //Put User with Id

        //Post New User

}