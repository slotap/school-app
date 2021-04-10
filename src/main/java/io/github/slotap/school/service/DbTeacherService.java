package io.github.slotap.school.service;

import io.github.slotap.school.mapper.TeacherMapper;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import io.github.slotap.school.repository.TeacherRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbTeacherService implements SchoolService<TeacherDto, Teacher> {
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    public DbTeacherService(TeacherRepository teacherRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public List<TeacherDto> getAll(Pageable pageable) {
        List<Teacher> teacherList = teacherRepository.findAll(pageable).getContent();
        return teacherMapper.mapToTeacherDtoList(teacherList);
    }

    @Override
    public TeacherDto save(Teacher teacher) {
        Teacher savedTeacher =  teacherRepository.save(teacher);
        return teacherMapper.mapToTeacherDto(savedTeacher);
    }

    @Override
    public void delete(long id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public Optional<TeacherDto> getDtoData(long id) {
        return teacherRepository.findById(id)
                .map(teacherMapper::mapToTeacherDto);
    }

    @Override
    public Optional<Teacher> getData(long id) {
        return teacherRepository.findById(id);
    }

    @Override
    public boolean existById(long id) {
        return teacherRepository.existsById(id);
    }

    @Override
    public List<TeacherDto> findByName(String lastName, String firstName) {
        List<Teacher> teacherList = teacherRepository.findByLastnameOrFirstnameOrderByLastname(lastName,firstName);
        return teacherMapper.mapToTeacherDtoList(teacherList);
    }
}
