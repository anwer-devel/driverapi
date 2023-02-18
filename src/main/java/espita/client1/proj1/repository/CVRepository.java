package espita.client1.proj1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import espita.client1.proj1.entity.CV;
import espita.client1.proj1.entity.Condidat;

public interface CVRepository extends JpaRepository<CV, Long>{

	List<CV> findByTextContainingIgnoreCase(String keyword);

}
