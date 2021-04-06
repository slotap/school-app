package io.github.slotap.school.repository;

import io.github.slotap.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

}
