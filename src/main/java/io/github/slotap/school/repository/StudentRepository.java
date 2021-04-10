package io.github.slotap.school.repository;

import io.github.slotap.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {
    List<Student> findByLastnameOrFirstnameOrderByLastname(@Param("lastName") String lastName, @Param("firstName") String firstName);
}
