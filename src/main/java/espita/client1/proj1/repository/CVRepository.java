package espita.client1.proj1.repository;

import java.util.List;

import org.apache.catalina.connector.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import espita.client1.proj1.entity.CV;
import espita.client1.proj1.entity.Condidat;

public interface CVRepository extends JpaRepository<CV, Long>{
	
	 List<CV> findByFolder(String folder);

	    List<CV> findBySubfolder(String subfolder);

	    List<CV> findBySubsubfolder(String subsubfolder);


	/*
	  @Query("SELECT c FROM CV c WHERE LOWER(c.text) LIKE LOWER(concat('%java%'))")
	    List<CV> searchByContenu(String search);*/
	  

}
