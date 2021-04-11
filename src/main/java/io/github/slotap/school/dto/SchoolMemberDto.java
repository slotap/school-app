package io.github.slotap.school.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public abstract class SchoolMemberDto {

    protected long id;
    @NotNull
    @Size(min = 3, message = "Firstname must have at least 3 characters")
    protected String firstname;
    @NotNull(message = "Lastname required")
    protected String lastname;
    @NotNull
    @Min(value = 18,message = "Age must be more than 18")
    protected int age;
    @NotNull
    @Email(message = "Email incorrect")
    protected String email;


}
