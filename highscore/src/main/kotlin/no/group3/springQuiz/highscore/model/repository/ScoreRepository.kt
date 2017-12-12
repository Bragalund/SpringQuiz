package no.group3.springQuiz.highscore.model.repository

import no.group3.springQuiz.highscore.model.entity.Score
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@Repository
interface ScoreRepository : CrudRepository<Score, Long>, ScoreRepositoryCustom {

    fun findAllByScore(score: Int) : Iterable<Score>
    fun findByUser(user: String) : Score?

}

@Transactional
interface ScoreRepositoryCustom {
    fun createScore(id: Long, user: String, score: Int) : Boolean
    fun deleteHighscoreById(id: Long) : Boolean
    fun updateHighscore(id: Long, user: String, score: Int): Boolean
    fun updateScore(id: Long, score: Int): Boolean
    fun updateUser(id: Long, user: String): Boolean

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

    override fun deleteHighscoreById(id:Long) : Boolean{
        val highscore = em.find(Score::class.java, id) ?: return false
        em.remove(highscore)
        return true
    }

    override fun updateHighscore(id: Long, user: String, score: Int): Boolean {
        val highscore = em.find(Score::class.java, id) ?: return false
        highscore.user = user
        highscore.score = score
        return true
    }

    override fun updateScore(id: Long, score: Int): Boolean {
        val highscore = em.find(Score::class.java, id) ?: return false
        highscore.score = score
        return true
    }

    override fun updateUser(id: Long, user: String): Boolean {
        val highscore = em.find(Score::class.java, id) ?: return false
        highscore.user = user
        return true
    }







}
