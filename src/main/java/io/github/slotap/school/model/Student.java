package io.github.slotap.school.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "students")
public class Student extends SchoolMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;
    @NotNull(message = "Course Name required")
    private String degreeCourse;
    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<Teacher> teachers = new HashSet<>();

    public Student(){}

    public Student(String firstname, String lastname, int age, String email, String degreeCourse) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.degreeCourse = degreeCourse;
    }

    public void updateStudent(Student toUpdate) {
        setFirstname(toUpdate.getFirstname());
        setLastname(toUpdate.getLastname());
        setEmail(toUpdate.getEmail());
        setAge(toUpdate.getAge());
        setDegreeCourse(toUpdate.getDegreeCourse());
        setTeachers(toUpdate.getTeachers());
    }

    public String getDegreeCourse() {
        return degreeCourse;
    }

    private void setDegreeCourse(String degreeCourse) {
        this.degreeCourse = degreeCourse;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    private void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return id == student.id && age == student.age && degreeCourse.equals(student.degreeCourse) && firstname.equals(student.firstname) && lastname.equals(student.lastname) && email.equals(student.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, degreeCourse, firstname, lastname, email, age);
    }
}
