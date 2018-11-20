package library.user

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface UserRepository extends CrudRepository<User, Long> {

    @Query('FROM User u WHERE u.username = ?1 AND u.isDeleted = false')
    User findByUsername(String username)
}
