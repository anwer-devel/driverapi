package espita.client1.proj1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import espita.client1.proj1.entity.Condidat;


@Repository
	public interface condidaRepository extends JpaRepository<Condidat, Long> {

	
}



