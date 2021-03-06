package no.group3.SpringQuiz.user.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiResponse
import no.group3.SpringQuiz.user.model.dto.UserConverter
import no.group3.SpringQuiz.user.model.dto.UserDto
import no.group3.SpringQuiz.user.model.entity.User
import no.group3.SpringQuiz.user.model.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.ConstraintViolationException

const val BASE_JSON = MediaType.APPLICATION_JSON_VALUE
const val ID_PARAM = "Id of user"


// Creates base url. example: www.someurl.com/user
@Api(value = "/user", description = "CRUD actions for User") //Creates documentation of API with Swagger
@RequestMapping(
        path = arrayOf("/user"),
        produces = arrayOf(BASE_JSON)
)
@RestController
@Validated
class UserCRUD {

    @Autowired
    private lateinit var userRepository: UserRepository

    @ApiOperation("Creates a new user") //Swagger documentation
    @PostMapping(consumes = arrayOf(BASE_JSON))
    @ApiResponse(code = 201, message = "Returns the ID of the new user") //Swagger expected return value
    fun createUser(
            @ApiParam("null, username, firstname, lastname, email")
            @RequestBody userDto: UserDto): ResponseEntity<Long> {


        //Checks of fields in object from client
        if (userDto.id != null) {
            return ResponseEntity.status(400).build() // Id should be null. Id is generated by database.
        }
        if (userDto.userName.isNullOrEmpty() || userDto.firstName.isNullOrEmpty() || userDto.lastName.isNullOrEmpty() || userDto.email.isNullOrEmpty()) {
            return ResponseEntity.status(400).build()
        }
        if (userRepository.findByUserName(userDto.userName!!) != null) {
            return ResponseEntity.status(409).build()
        }


        //Trying to create user

        val userId: Long?
        try {
            userId = userRepository.createUser(userDto.userName!!, userDto.firstName!!, userDto.lastName!!, userDto.email!!)
        } catch (e: ConstraintViolationException) {
            e.stackTrace
            return ResponseEntity.status(400).build()
        }

        // Checks userId from database
        if (userId == null) {
            return ResponseEntity.status(500).build()
        }

        // Returns userId generated by database
        return ResponseEntity.status(201).body(userId)
    }

    //Get one user with Id
    @ApiOperation("Get one user by id")
    @GetMapping(path = arrayOf("/{id}"))
    @ApiResponse(code = 200, message = "user-object")
    fun getUserWithId(@ApiParam(ID_PARAM) //documentation for swagger
                      @PathVariable("id") userId: String): ResponseEntity<UserDto>  //input and Return-values
    {
        val id = userId.toLong()
        //Check if user exists
        val userDto = userRepository.findOne(id) ?: return ResponseEntity.status(404).build()

        //Return 200 header and userDto after creating it.
        return ResponseEntity.ok(UserConverter.transform(userDto))
    }


    //Delete user with Id
    @ApiOperation("Delete one user with the given ID")
    @DeleteMapping(path = arrayOf("/{id}"))
    @ApiResponse(code = 204, message = "Succesfully deleted user, no body in http-response")
    fun deleteUserWithId(@ApiParam(ID_PARAM)
                         @PathVariable("id") userId: String): ResponseEntity<Any> {


        val id = userId.toLong()

        //Check if user exists
        if (!userRepository.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        userRepository.delete(id)
        return ResponseEntity.status(204).build()
    }

    //Put User
    @ApiOperation("Update user")
    @PutMapping(path = arrayOf("/{id}"))
    fun putUserWithId(
            @ApiParam(ID_PARAM)
            @PathVariable("id") userId: String,
            @ApiParam("New body to replace old one")
            @RequestBody userDto: UserDto): ResponseEntity<Any> {

        val id = userId.toLong()

        if (id != userDto.id) {
            return ResponseEntity.status(400).build()
        }

        //Check if user with given ID exists
        if (userRepository.exists(id)) {
            // Try to save updated user and check for constraintviolations
            try {
                userRepository.updateUser(id, userDto.userName!!, userDto.firstName!!, userDto.lastName!!, userDto.email!!)
                return ResponseEntity.status(204).build()
            } catch (e: ConstraintViolationException) {
                e.printStackTrace()
            }
            return ResponseEntity.status(400).build()
        } else {
            try {
                userRepository.createUser(userDto.userName!!, userDto.firstName!!, userDto.lastName!!, userDto.email!!)
                return ResponseEntity.status(201).build()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ResponseEntity.status(400).build()

        }
    }

    @ApiOperation("Modify the private userinfo")
    @PatchMapping(path = arrayOf("/{id}"))
    fun patchUserWithId(@ApiParam(ID_PARAM)
                        @PathVariable("id") userId: String,
                        @RequestBody userDto: UserDto): ResponseEntity<Any> {

        val id: Long
        try {
            id = userId.toLong() //Cast String(userId) to Long
        } catch (exception: Exception) {
            return ResponseEntity.status(400).build() //Bad request
        }

        //Check if user with given ID exists
        if (!userRepository.exists(id)) {
            return ResponseEntity.status(404).build()
        }

        val existingUser = userRepository.findOne(id)
        var everythingWentOk: Boolean


        try {

            if (userDto.userName != null) {
                // update username
                userRepository.updateUserName(id, userDto.userName!!)
            }

            if (userDto.firstName != null) {
                // update firstname
                userRepository.updateFirstName(id, userDto.firstName!!)
            }

            if (userDto.lastName != null) {
                // update lastname
                userRepository.updateLastName(id, userDto.lastName!!)
            }

            if (userDto.email != null) {
                // update email
                userRepository.updateEmail(id, userDto.email!!)

            }
            everythingWentOk = true
        } catch (e: ConstraintViolationException) {
            e.printStackTrace()
            everythingWentOk = false
        } catch (e: Exception) {
            e.printStackTrace()
            everythingWentOk = false
        }

        if (everythingWentOk) {
            val body = userRepository.findOne(id)
            return ResponseEntity.status(200).body(body)
        } else {
            if (rollbackUser(existingUser)) {
                return ResponseEntity.status(400).build()
            }
            return ResponseEntity.status(500).build()
        }
    }


    @ExceptionHandler(value = ConstraintViolationException::class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    fun handleValidationFailure(ex: ConstraintViolationException): String {

        val messages = StringBuilder()

        for (violation in ex.constraintViolations) {
            messages.append(violation.message + "\n")
        }

        return messages.toString()
    }

    private fun rollbackUser(user: User): Boolean {
        try {
            userRepository.updateUser(user.userId!!, user.userName!!, user.firstName!!, user.lastName!!, user.email!!)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun usernameTaken(username: String): Boolean {
        if (userRepository.findByUserName(username) != null) {
            return true
        }
        return false
    }

}