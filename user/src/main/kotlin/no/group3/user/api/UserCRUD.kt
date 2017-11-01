package no.group3.user.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.group3.user.model.dto.UserConverter
import no.group3.user.model.dto.UserDto
import no.group3.user.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

const val BASE_JSON = "application/json;charset=UTF-8"
const val V2_NEWS_JSON = "application/vnd.group3.user+json;charset=UTF-8;version=2"
const val ID_PARAM = "Id of user"
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

    @Autowired
    private lateinit var crud: UserRepository

    // TODO remove this
    @GetMapping("")
    fun something(): String {
        return "User works"
    }

    //Get one user with Id
    @ApiOperation("Returns a User with the given ID")
    @GetMapping(path = arrayOf("/{id}"))
    fun getUserWithId(@ApiParam(ID_PARAM) //documentation for swagger
                      @PathVariable("id")  //The actual parameter passed in by url
                      userId: String?): ResponseEntity<UserDto>  //input and Return-values
    {
        val id: Long
        try {
            id = userId!!.toLong() //Cast String(userId) to Long
        } catch (exception: Exception) {
            return ResponseEntity.status(404).build() //User not found
        }

        //Check if user exists
        val userDto = crud.findOne(id) ?: return ResponseEntity.status(404).build()

        //Return 200 header and userDto after creating it.
        return ResponseEntity.ok(UserConverter.transform(userDto))
    }


    //Delete user with Id

    //Put User with Id

    //Post New User

}