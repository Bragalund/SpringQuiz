package no.group3.springQuiz.highscore.model.repository

import no.group3.springQuiz.highscore.model.entity.Score
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

/**
 * Created by johannes on 01.11.2017.
 */
@Repository
interface ScoreRepository : CrudRepository<Score, Long>, ScoreRepositoryCustom {
}

@Transactional
interface ScoreRepositoryCustom {
    fun createScore(user: String, score: Int) : Long


}