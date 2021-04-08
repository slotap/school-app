package io.github.slotap.school.service;

import io.github.slotap.school.model.Student;
import io.github.slotap.school.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbStudentService {
    private final StudentRepository studentRepository;

    public DbStudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> getAll(Pageable page) {
        return studentRepository.findAll(page);
    }

    public Student saveStudent(final Student createStudent) {
        return studentRepository.save(createStudent);
    }

    public void deleteStudent(final long id){
        if(studentRepository.existsById(id)) {
            Student student = studentRepository.findById(id).get();
            student.getTeachers().forEach(teacher -> teacher.getStudents().remove(student));
            studentRepository.deleteById(id);
        }
    }

    public Optional<Student> getStudent (final long id){
        return studentRepository.findById(id);
    }

    public boolean existById(long id){
        return studentRepository.existsById(id);
    }

    public List<Student> findByName(final String lastName, final String firstName){
        return studentRepository.findByLastnameOrFirstnameOrderByLastname(lastName,firstName);
    }
}
