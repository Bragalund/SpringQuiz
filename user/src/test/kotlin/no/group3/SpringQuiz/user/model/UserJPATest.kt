package no.group3.SpringQuiz.user.model

import junit.framework.TestCase.*
import no.group3.SpringQuiz.user.model.entity.User
import no.group3.SpringQuiz.user.model.repository.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.context.junit4.SpringRunner
import javax.validation.ConstraintViolationException

@RunWith(SpringRunner::class)
@DataJpaTest
open class UserJPATest {

    @Autowired
    lateinit var userRepository : UserRepository

    @Test
    fun createUserTest(){
        val savedUserId = userRepository.createUser("AwesomeUsername", "Fredrik", "Thorelfsen", "somemail@mail.com")
        assertNotNull(savedUserId)
    }

    @Test
    fun createAndDeleteUserTest(){
        val id = userRepository.createUser("AwesomeUsername", "Fredrik", "Thorelfsen", "somemail@mail.com")
        assertNotNull(id)
        userRepository.deleteUserById(id)
        assertNull(userRepository.findOne(id))
    }

    @Test
    fun createAndFindUserByUsernameTest(){
        var user = User(null,"AwesomeUsername", "Fredrik", "Thorelfsen", "somemail@mail.com")
        val savedUser = userRepository.save(user)
        assertEquals(savedUser.userName, userRepository.findByUserName(user.userName!!)!!.userName)
    }

    @Test(expected = DataIntegrityViolationException::class)
    fun createTwoUsersWithSameUsernameTest(){
        var user1 = User(null,"AwesomeUsername", "Fredrik", "Thorelfsen", "somemail@mail.com")
        val savedUser1 = userRepository.save(user1)
        var user2 = User(null,"AwesomeUsername", "Fredrik", "Thorelfsen", "somemail@mail.com")
        val savedUser2 = userRepository.save(user2)
    }

    @Test
    fun createAndUpdateFirstnameTest(){
        val user = User(null,"AwesomeUsername", "Fredrik", "Thorelfsen", "somemail@mail.com")
        val id = userRepository.save(user).userId
        assertEquals("Fredrik", userRepository.findOne(id).firstName)
        userRepository.updateFirstName(id!!,"Kevin")
        assertEquals("Kevin", userRepository.findOne(id).firstName)
    }


    // Large test that does alot
    @Test
    fun createTwoUsersAndChangeThemAndDeleteThemTest(){
        val id1 = userRepository.createUser("AwesomeUsername1", "Fredrik", "Thorelfsen", "somemail@mail.com")
        val id2 = userRepository.createUser("AwesomeUsername2", "Fredrik", "Thorelfsen", "somemail@mail.com")

        assertNotNull(id1)
        assertNotNull(id2)

        assertEquals("Fredrik", userRepository.findOne(id1).firstName)
        assertEquals("Fredrik", userRepository.findOne(id2).firstName)

        userRepository.updateFirstNameLastNameAndEmail(id1, "Kåre", "Kåresen", "kåre@mail.com")
        userRepository.updateFirstNameLastNameAndEmail(id2, "Lars", "Larsen", "lars@mail.com")

        assertEquals("Kåre", userRepository.findOne(id1).firstName)
        assertEquals("Kåresen", userRepository.findOne(id1).lastName)
        assertEquals("kåre@mail.com", userRepository.findOne(id1).email)
        assertEquals("Lars", userRepository.findOne(id2).firstName)
        assertEquals("Larsen", userRepository.findOne(id2).lastName)
        assertEquals("lars@mail.com", userRepository.findOne(id2).email)

        userRepository.deleteAll()

        assertNull(userRepository.findOne(id1))
        assertNull(userRepository.findOne(id2))
    }

    @Test
    fun findByUsernameTest(){
        userRepository.createUser("AwesomeUsername1", "Fredrik", "Thorelfsen", "somemail@mail.com")
        val user = userRepository.findByUserName("AwesomeUsername1")
        assertEquals("Fredrik", user!!.firstName)
    }

//    @Test
//    fun patchDtoTest(){
//        val id = userRepository.createUser("AwesomeUsername1", "Fredrik", "Thorelfsen", "somemail@mail.com")
//        val patchDto = PatchDto("Torgeir", "helgesen", "helgesen@mail.com")
//        userRepository.updateFirstNameLastNameAndEmail(id, patchDto.firstName!!, patchDto.lastName!!, patchDto.email!!)
//    }

    @Test(expected = ConstraintViolationException::class)
    fun createUserWithNull(){
        val user = User(null, "AwesomeUsername1", null, "Thorelfsen", "somemail@mail.com")
        userRepository.save(user)
    }
}