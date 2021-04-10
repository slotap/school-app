package io.github.slotap.school.mapper;

import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentMapper {

    public List<StudentDto> mapToStudentDtoList(final List<Student> studentList){
        return studentList.stream()
                .map(this::mapToStudentDto)
                .collect(Collectors.toList());
    }

    public StudentDto mapToStudentDto(final Student student) {
        return new StudentDto(
                    student.getId(),
                    student.getFirstname(),
                    student.getLastname(),
                    student.getAge(),
                    student.getEmail(),
                    student.getDegreeCourse(),
                    student.getTeachers());
    }
}
