package no.group3.user.model.repository

import no.group3.user.model.entity.Password
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext


@Repository
interface PasswordRepository : CrudRepository<Password, Long>, PasswordRepositoryCustom {
}

@Transactional
interface PasswordRepositoryCustom {
    fun updatePassword(id: Long, passwordHash: String): Boolean
}

open class PasswordRepositoryImpl : PasswordRepositoryCustom {

    @PersistenceContext
    private lateinit var em: EntityManager

    override fun updatePassword(id: Long, passwordHash: String): Boolean {
        var password = em.find(Password::class.java, id) ?: return false
        password.passwordHash = passwordHash
        return true
    }

}