package io.github.slotap.school.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class TeacherDto extends SchoolMemberDto{
    @NotNull(message = "Subject name required")
    private String teachingSubject;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<StudentDto> students;

    public TeacherDto()  {
    }

    public TeacherDto(String firstname, String lastname, int age, String email, String teachingSubject) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.teachingSubject = teachingSubject;
        this.students = new HashSet<>();
    }

    public TeacherDto(long id, String firstname, String lastname, int age, String email, String teachingSubject) {
        this(firstname, lastname, age, email, teachingSubject);
        this.id = id;
    }

    public TeacherDto(long id, String firstname, String lastname, int age, String email, String teachingSubject, Set<StudentDto> students) {
        this(id, firstname, lastname, age, email, teachingSubject);
        this.students = students;
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

    public String getTeachingSubject() {
        return teachingSubject;
    }

    public Set<StudentDto> getStudents() {
        return students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeacherDto)) return false;
        TeacherDto teacher = (TeacherDto) o;
        return id == teacher.id && age == teacher.age && firstname.equals(teacher.firstname) && lastname.equals(teacher.lastname) && teachingSubject.equals(teacher.teachingSubject) && email.equals(teacher.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, teachingSubject,firstname,lastname, age, email);
    }
}
