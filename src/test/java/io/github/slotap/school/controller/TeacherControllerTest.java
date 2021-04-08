package io.github.slotap.school.controller;

import com.google.gson.Gson;
import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.mapper.TeacherMapper;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import io.github.slotap.school.service.DbTeacherService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(TeacherController.class)
class TeacherControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DbTeacherService dbTeacherService;

    @MockBean
    StudentMapper studentMapper;

    @MockBean
    TeacherMapper teacherMapper;

    @MockBean
    Page<Teacher> page;

    @Test
    void shouldFetchEmptyTeacherList() throws Exception {
        //Given
        when(dbTeacherService.getAll(any())).thenReturn(page);
        when(page.getContent()).thenReturn(List.of());
        when(teacherMapper.mapToTeacherDtoList(anyList())).thenReturn(List.of());

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
        List<Teacher> teacherList = List.of( new Teacher("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia"));
        List<TeacherDto> teacherDtoList = List.of( new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()));
        when(dbTeacherService.getAll(any())).thenReturn(page);
        when(page.getContent()).thenReturn(teacherList);
        when(teacherMapper.mapToTeacherDtoList(teacherList)).thenReturn(teacherDtoList);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studentSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchOneStudent() throws Exception {
        //Given
        Teacher teacher = new Teacher("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        TeacherDto teacherDto = new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()) ;
        when(dbTeacherService.getTeacher(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.mapToTeacherDto(teacher)).thenReturn(teacherDto);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.studentSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetNotFoundWhenFetchingOneStudent() throws Exception {
        //Given
        when(dbTeacherService.getTeacher(1L)).thenReturn(Optional.empty());

        //When & Then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(404));
    }

    @Test
    void shouldFetchFilteredTeachers() throws Exception {
        //Given
        Teacher teacher = new Teacher("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        teacher.getStudents().add(new Student("Andrzej", "Wajda", 55, "wajda@gmail.com","Filmografia"));
        StudentDto studentDto = new StudentDto(1,"Andrzej", "Wajda", 55, "wajda@gmail.com","Filmografia",Set.of());
        when(dbTeacherService.getTeacher(1L)).thenReturn(Optional.of(teacher));
        when(studentMapper.mapToStudentDto(any())).thenReturn(studentDto);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].degreeCourse", Matchers.is("Filmografia")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teacherSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldFindStudentsByName() throws Exception {
        //Given
        List<Teacher> teacherList = List.of( new Teacher("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia"));
        List<TeacherDto> teacherDtoList = List.of( new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()));
        when(dbTeacherService.findByName("kow","J")).thenReturn(teacherList);
        when(teacherMapper.mapToTeacherDtoList(teacherList)).thenReturn(teacherDtoList);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studentSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldCreateStudent() throws Exception {
        //Given
        Teacher teacher = new Teacher("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        TeacherDto teacherDto = new TeacherDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of());
        when(dbTeacherService.saveTeacher(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.mapToTeacherDto(teacher)).thenReturn(teacherDto);

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
        Teacher teacher = new Teacher("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        when(dbTeacherService.existById(anyLong())).thenReturn(true);
        when(dbTeacherService.getTeacher(anyLong())).thenReturn(Optional.of(teacher));

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