package no.group3.springQuiz.quiz.model.repository

import no.group3.springQuiz.quiz.model.entity.Category
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.TypedQuery
import javax.transaction.Transactional

/**
 * Created by josoder on 18.10.17.
 */
@Repository
interface CategoryRepository : CrudRepository<Category, Long>, CategoryRepositoryCustom {
    fun findByName(name: String) : Category?
}

@Transactional
interface CategoryRepositoryCustom {
    fun update(id:Long,
               name: String): Boolean
}

open class CategoryRepositoryImpl : CategoryRepositoryCustom {
    @PersistenceContext
    private lateinit var em: EntityManager

    override fun update(id: Long,
                        newName: String): Boolean {

        var category = em.find(Category::class.java, id) ?: return false
        category.name =  newName
        return true
    }
}