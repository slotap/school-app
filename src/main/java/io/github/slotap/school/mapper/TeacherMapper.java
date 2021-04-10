package io.github.slotap.school.mapper;

import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherMapper {

    public TeacherDto mapToTeacherDto(final Teacher teacher){
        return new TeacherDto(
                teacher.getId(),
                teacher.getFirstname(),
                teacher.getLastname(),
                teacher.getAge(),
                teacher.getEmail(),
                teacher.getTeachingSubject(),
                teacher.getStudents());
    }
    public List<TeacherDto> mapToTeacherDtoList(final List<Teacher> teacherList){
        return teacherList.stream()
                .map(this::mapToTeacherDto)
                .collect(Collectors.toList());
    }
}
