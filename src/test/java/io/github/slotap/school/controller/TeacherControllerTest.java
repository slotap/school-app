package io.github.slotap.school.controller;

import com.google.gson.Gson;
import io.github.slotap.school.dto.StudentDto;
import io.github.slotap.school.dto.TeacherDto;
import io.github.slotap.school.service.TeacherMemberService;
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
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TeacherMemberService teacherService;

    @Test
    void shouldFetchEmptyTeacherList() throws Exception {
        //Given
        when(teacherService.getAllMembers(any())).thenReturn(List.of());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchAllStudentList() throws Exception {
        //Given
        List<TeacherDto> teacherDtoList = List.of( new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()));
        when(teacherService.getAllMembers(any())).thenReturn(teacherDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Kowalski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("jkowalksi@Gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teachingSubject", Matchers.is("Politologia")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].students", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchOneStudent() throws Exception {
        //Given
        TeacherDto teacherDto = new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()) ;
        when(teacherService.getById(1L)).thenReturn(Optional.of(teacherDto));

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", Matchers.is("Kowalski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age", Matchers.is(22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is("jkowalksi@Gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teachingSubject", Matchers.is("Politologia")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.students", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetNotFoundWhenFetchingOneStudent() throws Exception {
        //Given
        when(teacherService.getById(1L)).thenReturn(Optional.empty());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldFetchFilteredStudents() throws Exception {
        //Given
        TeacherDto teacher = new TeacherDto("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        teacher.getStudents().add(new StudentDto(1,"Andrzej", "Wajda", 55, "wajda@gmail.com","Filmografia"));
        when(teacherService.getById(1L)).thenReturn(Optional.of(teacher));

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/teachers/1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("Andrzej")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Wajda")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(55)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("wajda@gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].degreeCourse", Matchers.is("Filmografia")));
    }

    @Test
    void shouldFindTeachersByName() throws Exception {
        //Given
        List<TeacherDto> teacherDtoList = List.of( new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia"));
        when(teacherService.getByLastnameFirstname("kow","J")).thenReturn(teacherDtoList);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/teachers/search?lastname=kow&firstname=J")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstname", Matchers.is("Jan")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastname", Matchers.is("Kowalski")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age", Matchers.is(22)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email", Matchers.is("jkowalksi@Gmail.com")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teachingSubject", Matchers.is("Politologia")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].students", Matchers.hasSize(0)));
    }

    @Test
    void shouldCreateStudent() throws Exception {
        //Given
        TeacherDto teacherDto = new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of());
        when(teacherService.saveMember(any(TeacherDto.class))).thenReturn(teacherDto);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(teacherDto);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    void shouldUpdateStudent() throws Exception {
        //Given
        TeacherDto teacher = new TeacherDto("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        when(teacherService.updateMember(anyLong(),any(TeacherDto.class))).thenReturn(Optional.of(teacher));

        Gson gson = new Gson();
        String jsonContent = gson.toJson(teacher);

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().is(204));
    }
}
