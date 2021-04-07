package io.github.slotap.school.service;

import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.repository.TeacherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbTeacherService {
    private final TeacherRepository teacherRepository;

    public DbTeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Page<Teacher> getAll(Pageable page) {
        return teacherRepository.findAll(page);
    }

    public Teacher saveTeacher(final Teacher createTeacher) {
        return teacherRepository.save(createTeacher);
    }

    public void deleteTeacher(final long id){
        teacherRepository.deleteById(id);
    }

    public Optional<Teacher> getTeacher (final long id){
        return teacherRepository.findById(id);
    }

    public boolean existById(long id){
        return teacherRepository.existsById(id);
    }

    public List<Teacher> findByName(final String lastName, final String firstName){
       return teacherRepository.findByLastnameOrFirstnameOrderByLastname(lastName,firstName);
    }
}
