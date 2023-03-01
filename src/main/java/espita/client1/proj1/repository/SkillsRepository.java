package espita.client1.proj1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import espita.client1.proj1.entity.Skills;
@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long>{

	Skills save(String skills); 

}
