package io.github.slotap.school.service;

import io.github.slotap.school.dto.TeacherDto;
import io.github.slotap.school.mapper.TeacherMapper;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.repository.TeacherRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherMemberService implements SchoolMemberService<TeacherDto> {

    private final TeacherRepository teacherRepository;

    public TeacherMemberService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public List<TeacherDto> getAllMembers(Pageable pageable) {
        List<Teacher> teacherList = teacherRepository.findAll(pageable).getContent();
        return teacherList.stream()
                .map(TeacherMapper::mapToTeacherDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TeacherDto> getById(long id) {
        if(!teacherRepository.existsById(id)){
            return Optional.empty();
        }
        return teacherRepository.findById(id)
                .map(TeacherMapper::mapToTeacherDto);
    }

    @Override
    public TeacherDto saveMember(TeacherDto schoolMember) {
        Teacher teacher = teacherRepository.save(TeacherMapper.mapToTeacher(schoolMember));
        return TeacherMapper.mapToTeacherDto(teacher);
    }

    @Override
    public void deleteMember(long id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        }
    }

    @Override
    public List<TeacherDto> getByLastnameFirstname(String lastname, String firstname) {
        List<Teacher> teachers = teacherRepository.findByLastnameOrFirstnameOrderByLastname(lastname, firstname);
        return teachers.stream()
                .map(TeacherMapper::mapToTeacherDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TeacherDto> updateMember(long id, TeacherDto schoolMember) {
        Teacher studentToUpdate = TeacherMapper.mapToTeacher(schoolMember);
        return this.getById(id)
                .map(TeacherMapper::mapToTeacher)
                .map(teacher -> teacher.updateTeacher(studentToUpdate))
                .map(TeacherMapper::mapToTeacherDto)
                .map(this::saveMember);
    }

    @Override
    public boolean existById(long id) {
        return teacherRepository.existsById(id);
    }
}
