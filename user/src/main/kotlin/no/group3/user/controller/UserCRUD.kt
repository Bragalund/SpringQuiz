package no.group3.user.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val BASE_JSON = "application/json;charset=UTF-8"
const val V2_NEWS_JSON = "application/vnd.group3.user+json;charset=UTF-8;version=2"
const val NOT_IMPLEMENTED = "Not yet implemented."


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

        // TODO remove this
        @GetMapping("")
        fun something(): String{
                return "User works"
        }

        //Get one user with Id
        @ApiOperation("Returns a User with the given ID")
        @GetMapping(path = arrayOf("/{id}"))
        fun getUserWithId(@PathVariable("id")pathId: String?) : String{

                //Check if user exists

                //Return some UserDTO

                return "NOT_IMPLEMENTED"
        }


        //Delete user with Id

        //Put User with Id

        //Post New User

}