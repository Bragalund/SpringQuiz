package no.group3.springQuiz.quiz.model

import no.group3.springQuiz.quiz.model.entity.Category
import no.group3.springQuiz.quiz.model.entity.Question
import no.group3.springQuiz.quiz.model.repository.CategoryRepository
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class CategoryJPATest {
	@Autowired
	lateinit var categoryRepository : CategoryRepository

	private fun createCategory(name: String){
		var cat = Category(name = name)
	}

	@Test
	fun testCreateCategory(){
		var cat = Category(name = "testCat")

		val id = categoryRepository.save(cat)
		println("id : " + id.id)
		assertNotNull(categoryRepository.findByName("testCat"))
	}
}
