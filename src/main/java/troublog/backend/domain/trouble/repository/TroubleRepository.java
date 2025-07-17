package troublog.backend.domain.trouble.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import troublog.backend.domain.trouble.entity.Trouble;

public interface TroubleRepository extends JpaRepository<Trouble, Long> {
}