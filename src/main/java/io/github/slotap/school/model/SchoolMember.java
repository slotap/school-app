package io.github.slotap.school.model;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@MappedSuperclass
abstract class SchoolMember {

    @NotNull
    @Size(min = 3, message = "Firstname must have at least 3 characters")
    protected String firstname;
    @NotNull(message = "Lastname required")
    protected String lastname;
    @NotNull
    @Min(value = 18,message = "Age must be more than 18")
    protected int age;
    @NotNull
    @Email(message = "email incorrect")
    protected String email;

    public String getFirstname() {
        return firstname;
    }

    protected void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    protected void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    protected void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }
}
