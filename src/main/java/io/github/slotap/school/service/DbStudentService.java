package io.github.slotap.school.service;

import io.github.slotap.school.model.Student;
import io.github.slotap.school.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DbStudentService {
    private final StudentRepository studentRepository;

    public DbStudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Page<Student> getAll(Pageable page) {
        return studentRepository.findAll(page);
    }
}
