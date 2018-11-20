package library.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User

@Configuration
@EnableJpaAuditing
class AuditConfig {

    @Bean
    AuditorAware<String> getAuditorProvider() {
        return new SpringSecurityAuditorAware()
    }

    class SpringSecurityAuditorAware implements AuditorAware<String> {
        Optional<String> getCurrentAuditor() {
            Authentication authentication = SecurityContextHolder.context.authentication
            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of('SYSTEM')
            }
            if (authentication.principal instanceof User) {
                return ((User)authentication.principal).username
            }
            return Optional.of(authentication.principal)
        }
    }
}
