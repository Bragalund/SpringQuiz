package no.group3.springquiz.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by josoder on 06.12.17.
 */
@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
    UserEntity findByUsername(String username);
}
