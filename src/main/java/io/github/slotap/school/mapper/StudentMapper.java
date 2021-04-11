package io.github.slotap.school.mapper;

import io.github.slotap.school.dto.StudentDto;
import io.github.slotap.school.dto.TeacherDto;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.Teacher;

import java.util.Set;
import java.util.stream.Collectors;

public class StudentMapper {

    public static StudentDto mapToStudentDto(final Student student) {
        return new StudentDto(
                    student.getId(),
                    student.getFirstname(),
                    student.getLastname(),
                    student.getAge(),
                    student.getEmail(),
                    student.getDegreeCourse(),
                    mapTeachers(student.getTeachers()));
    }

    private static Set<TeacherDto> mapTeachers(Set<Teacher> teachers) {
        return teachers
                .stream()
                .map(TeacherMapper::mapToTeacherDtoWithoutStudents)
                .collect(Collectors.toSet());
    }

    public static Student mapToStudent(final StudentDto studentDto) {
        return new Student(
                studentDto.getId(),
                studentDto.getFirstname(),
                studentDto.getLastname(),
                studentDto.getAge(),
                studentDto.getEmail(),
                studentDto.getDegreeCourse()
        );
    }

    public static StudentDto mapToStudentDtoWithoutTeachers(final Student student) {
        return new StudentDto(
                student.getId(),
                student.getFirstname(),
                student.getLastname(),
                student.getAge(),
                student.getEmail(),
                student.getDegreeCourse());
    }
}
