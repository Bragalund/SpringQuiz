package no.group3.SpringQuiz.user.model

import junit.framework.TestCase.assertNotNull
import no.group3.SpringQuiz.user.model.entity.User
import no.group3.SpringQuiz.user.model.repository.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
open class UserJPATest {

    @Autowired
    lateinit var userRepository : UserRepository

    @Test
    fun createUser(){
        var user = User(null,"AwesomeUsername", "Fredrik", "Thorelfsen", "somemail@mail.com", "jlkdsflkjfdslkj")
        val savedUserId = userRepository.save(user)
        assertNotNull(savedUserId)
    }
}