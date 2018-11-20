package library.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotNull

@Transactional
@Service
@Validated
class UserService {
    private final UserRepository userRepository

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository
    }

    User findByUsername(@NotNull String username) {
        return userRepository.findByUsername(username)
    }
}
