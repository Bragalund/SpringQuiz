package no.group3.springQuiz.highscore.model

/**
 * Created by johannes on 13.12.2017.
 */
import junit.framework.TestCase.assertNotNull
import no.group3.springQuiz.highscore.model.entity.Score
import no.group3.springQuiz.highscore.model.repository.ScoreRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
open class ScoreJPATest {

    @Autowired
    lateinit var scoreRepository : ScoreRepository

    @Test
    fun createScore(){
        var score = Score(null,"Fred",6)
        val savedScoreId = scoreRepository.save(score)
        assertNotNull(savedScoreId)
    }
}