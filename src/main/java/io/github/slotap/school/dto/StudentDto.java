package io.github.slotap.school.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class StudentDto extends SchoolMemberDto {
    @NotNull(message = "Course Name required")
    private String degreeCourse;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<TeacherDto> teachers;

    public StudentDto(){}

    public StudentDto(String firstname, String lastname, int age, String email, String degreeCourse) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.degreeCourse = degreeCourse;
        this.teachers = new HashSet<>();
    }

    public StudentDto(long id, String firstname, String lastname, int age, String email, String degreeCourse) {
        this(firstname, lastname, age, email, degreeCourse);
        this.id = id;
    }

    public StudentDto(long id, String firstname, String lastname, int age, String email, String degreeCourse, Set<TeacherDto> teachers) {
        this(id, firstname, lastname, age, email, degreeCourse);
        this.teachers = teachers;
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    public Set<TeacherDto> getTeachers() {
        return teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentDto)) return false;
        StudentDto student = (StudentDto) o;
        return id == student.id && age == student.age && degreeCourse.equals(student.degreeCourse) && firstname.equals(student.firstname) && lastname.equals(student.lastname) && email.equals(student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, degreeCourse, firstname, lastname, email, age);
    }
}
