package no.group3.springQuiz.highscore.model.repository

import no.group3.springQuiz.highscore.model.entity.Score
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

/**
 * Created by johannes on 01.11.2017.
 */
@Repository
interface ScoreRepository : CrudRepository<Score, Long>, ScoreRepositoryCustom {

    fun findAllByScore(score: Int) : Iterable<Score>

}

@Transactional
interface ScoreRepositoryCustom {
    fun createScore(id:Long, user: String, score: Int) : Boolean

}

open class ScoreRepositoryImpl : ScoreRepositoryCustom {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun createScore(id:Long ,user: String, score: Int): Boolean {
        val scores = em.find(Score::class.java, id) ?: return false
        scores.user = user
        scores.score = score
        return true
    }
}
