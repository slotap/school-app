package io.github.slotap.school.service;

import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DbTeacherService {
    private final TeacherRepository teacherRepository;

    public DbTeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Page<Teacher> getAll(Pageable page) {
        return teacherRepository.findAll(page);
    }
}
