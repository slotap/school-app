package io.github.slotap.school.mapper;

import io.github.slotap.school.dto.StudentDto;
import io.github.slotap.school.dto.TeacherDto;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.Teacher;

import java.util.Set;
import java.util.stream.Collectors;

public class TeacherMapper {

    public static TeacherDto mapToTeacherDtoWithoutStudents(final Teacher teacher) {
        return new TeacherDto(
                teacher.getId(),
                teacher.getFirstname(),
                teacher.getLastname(),
                teacher.getAge(),
                teacher.getEmail(),
                teacher.getTeachingSubject());
    }

    public static TeacherDto mapToTeacherDto(final Teacher teacher) {
        return new TeacherDto(
                teacher.getId(),
                teacher.getFirstname(),
                teacher.getLastname(),
                teacher.getAge(),
                teacher.getEmail(),
                teacher.getTeachingSubject(),
                mapStudents(teacher.getStudents()));
    }

    private static Set<StudentDto> mapStudents(Set<Student> students) {
        return students
                .stream()
                .map(StudentMapper::mapToStudentDtoWithoutTeachers)
                .collect(Collectors.toSet());
    }

    public static Teacher mapToTeacher(TeacherDto schoolMember) {
        return new Teacher(
                schoolMember.getId(),
                schoolMember.getFirstname(),
                schoolMember.getLastname(),
                schoolMember.getAge(),
                schoolMember.getEmail(),
                schoolMember.getTeachingSubject()
        );
    }
}
