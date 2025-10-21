package troublog.backend.domain.terms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.terms.entity.UserTermsConsent;

public interface UserTermsConsentRepository extends JpaRepository<UserTermsConsent, Long> {
	List<UserTermsConsent> findAllByUserId(Long userId);
}