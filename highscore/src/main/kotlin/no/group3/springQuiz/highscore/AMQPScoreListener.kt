package no.group3.springQuiz.highscore

import no.group3.springQuiz.highscore.model.dto.AMQPScoreDto
import no.group3.springQuiz.highscore.model.entity.Score
import no.group3.springQuiz.highscore.model.repository.ScoreRepository
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController


/**
 * Created by josoder on 12.12.17.
 */
@RestController
class AMQPScoreListener {
    @Autowired
    private lateinit var scoreRepo : ScoreRepository

    @RabbitListener(queues = arrayOf("#{queue.name}"))
    fun recieveScore(scoreDto: AMQPScoreDto) {
        if(scoreDto.username == null || scoreDto.score == null){
            return@recieveScore
        }

        val scoreEntity = scoreRepo.findByUser(scoreDto.username!!)

        if(scoreEntity==null){
            scoreRepo.save(Score(user = scoreDto.username, score = scoreDto.score))
        } else {
            var s: Int = scoreEntity.score!! // a bit hacky,,. Kotlin would not allow to just use += with nullables in
            s += scoreDto.score!!      // dot notation
            scoreEntity.score = s
            scoreRepo.save(scoreEntity)
        }

    }
}
