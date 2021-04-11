package io.github.slotap.school.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher extends SchoolMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String teachingSubject;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "students_teachers",
                        joinColumns = {
                            @JoinColumn(name="teacher_id", referencedColumnName = "id",
                            nullable = false,updatable = false)
                        },
                        inverseJoinColumns = {
                            @JoinColumn(name="student_id", referencedColumnName = "id",
                            nullable = false,updatable = false)
                        })

    private Set<Student> students = new HashSet<>();

    public Teacher(){}

    public Teacher(String firstname, String lastname, int age, String email, String subject) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.email = email;
        this.teachingSubject = subject;
    }

    public Teacher(long id, String firstname, String lastname, int age, String email, String subject) {
        this(firstname,lastname,age,email,subject);
        this.id = id;
    }

    public Teacher updateTeacher(Teacher toUpdate) {
        setFirstname(toUpdate.getFirstname());
        setLastname(toUpdate.getLastname());
        setEmail(toUpdate.getEmail());
        setAge(toUpdate.getAge());
        setTeachingSubject(toUpdate.getTeachingSubject());
        setStudents(toUpdate.getStudents());
        return this;
    }

    public String getTeachingSubject() {
        return teachingSubject;
    }

    private void setTeachingSubject(String subject) {
        this.teachingSubject = subject;
    }

    public long getId() {
        return id;
    }

    private void setId(long id) {
        this.id = id;
    }

    public Set<Student> getStudents() {
        return students;
    }

    private void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id && age == teacher.age && firstname.equals(teacher.firstname) && lastname.equals(teacher.lastname) && teachingSubject.equals(teacher.teachingSubject) && email.equals(teacher.email);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, teachingSubject,firstname,lastname, age, email);
    }

}
