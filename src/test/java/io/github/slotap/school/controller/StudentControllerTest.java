package io.github.slotap.school.controller;

import com.google.gson.Gson;
import io.github.slotap.school.dto.StudentDto;
import io.github.slotap.school.dto.TeacherDto;
import io.github.slotap.school.service.StudentMemberService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudentMemberService studentService;

    @Test
    void shouldFetchEmptyStudentList() throws Exception {
        //Given
            when(studentService.getAllMembers(any())).thenReturn(List.of());

        //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/students")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchAllStudentList() throws Exception {
        //Given
            List<StudentDto> studentDtoList = List.of( new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()));
            when(studentService.getAllMembers(any())).thenReturn(studentDtoList);

        //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/students")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("Jan")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Kowalski")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(22)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("jkowalksi@Gmail.com")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].degreeCourse", Matchers.is("Politologia")));
    }

    @Test
    void shouldFetchOneStudent() throws Exception {
        //Given
            StudentDto studentDto = new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia") ;
            when(studentService.getById(1L)).thenReturn(Optional.of(studentDto));

        //When & Then
            mockMvc
                    .perform(MockMvcRequestBuilders
                            .get("/students/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("Jan")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Kowalski")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(22)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("jkowalksi@Gmail.com")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.degreeCourse", Matchers.is("Politologia")));
    }

    @Test
    void shouldGetNotFoundWhenFetchingOneStudent() throws Exception {
        //Given
        when(studentService.getById(1L)).thenReturn(Optional.empty());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldFetchFilteredTeachers() throws Exception {
        //Given
        StudentDto student = new StudentDto("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        student.getTeachers().add(new TeacherDto(1,"Andrzej", "Wajda", 55, "wajda@gmail.com","Filmografia"));
        when(studentService.getById(1L)).thenReturn(Optional.of(student));

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/students/1/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("Andrzej")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Wajda")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(55)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("wajda@gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teachingSubject", Matchers.is("Filmografia")));
         }

    @Test
    void shouldFindStudentsByName() throws Exception {
        //Given
            List<StudentDto> studentDtoList = List.of( new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()));
            when(studentService.getByLastnameFirstname("kow","J")).thenReturn(studentDtoList);

        //When & Then
            mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/students/search?lastname=kow&firstname=J")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is(200))
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("Jan")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Kowalski")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(22)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("jkowalksi@Gmail.com")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].degreeCourse", Matchers.is("Politologia")));
    }

    @Test
    void shouldCreateStudent() throws Exception {
        //Given
            StudentDto studentDto = new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of());
            when(studentService.saveMember(any(StudentDto.class))).thenReturn(studentDto);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(studentDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        //Given
            StudentDto student = new StudentDto("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
            when(studentService.updateMember(anyLong(),any(StudentDto.class))).thenReturn(Optional.of(student));

            Gson gson = new Gson();
            String jsonContent = gson.toJson(student);

        //When & Then
            mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }
}
