package io.github.slotap.school.model;

import java.util.Set;

public class TeacherDto {
    private final long id;
    private final String firstname;
    private final String lastname;
    private final int age;
    private final String email;
    private final String teachingSubject;
    private final Set<Student> studentSet;

    public TeacherDto(long id, String firstname, String lastname, int age, String email, String teachingSubject, Set<Student> studentSet) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.teachingSubject = teachingSubject;
        this.studentSet = studentSet;
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

    public Set<Student> getStudentSet() {
        return studentSet;
    }
}
