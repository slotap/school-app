package io.github.slotap.school.service;

import io.github.slotap.school.model.Student;
import io.github.slotap.school.repository.StudentRepository;
import io.github.slotap.school.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DbService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public DbService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public Page<Student> getAll(Pageable page) {
        return studentRepository.findAll(page);
    }
}
