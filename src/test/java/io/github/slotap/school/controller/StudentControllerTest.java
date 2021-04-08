package io.github.slotap.school.controller;

import com.google.gson.Gson;
import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.mapper.TeacherMapper;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import io.github.slotap.school.service.DbStudentService;
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
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DbStudentService dbStudentService;

    @MockBean
    StudentMapper studentMapper;

    @MockBean
    TeacherMapper teacherMapper;

    @MockBean
    Page<Student> page;

    @Test
    void shouldFetchEmptyStudentList() throws Exception {
        //Given
            when(dbStudentService.getAll(any())).thenReturn(page);
            when(page.getContent()).thenReturn(List.of());
            when(studentMapper.mapToStudentDtoList(anyList())).thenReturn(List.of());

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
            List<Student> studentList = List.of( new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia"));
            List<StudentDto> studentDtoList = List.of( new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()));
            when(dbStudentService.getAll(any())).thenReturn(page);
            when(page.getContent()).thenReturn(studentList);
            when(studentMapper.mapToStudentDtoList(studentList)).thenReturn(studentDtoList);

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
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].degreeCourse", Matchers.is("Politologia")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].teacherSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldFetchOneStudent() throws Exception {
        //Given
            Student student = new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
            StudentDto studentDto = new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()) ;
            when(dbStudentService.getStudent(1L)).thenReturn(Optional.of(student));
            when(studentMapper.mapToStudentDto(student)).thenReturn(studentDto);

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
                    .andExpect(MockMvcResultMatchers.jsonPath("$.degreeCourse", Matchers.is("Politologia")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.teacherSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetNotFoundWhenFetchingOneStudent() throws Exception {
        //Given
        when(dbStudentService.getStudent(1L)).thenReturn(Optional.empty());

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
        Student student = new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
        student.getTeachers().add(new Teacher("Andrzej", "Wajda", 55, "wajda@gmail.com","Filmografia"));
        TeacherDto teacherDto = new TeacherDto(1,"Andrzej", "Wajda", 55, "wajda@gmail.com","Filmografia",Set.of());
        when(dbStudentService.getStudent(1L)).thenReturn(Optional.of(student));
        when(teacherMapper.mapToTeacherDto(any())).thenReturn(teacherDto);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].teachingSubject", Matchers.is("Filmografia")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studentSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldFindStudentsByName() throws Exception {
        //Given
            List<Student> studentList = List.of( new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia"));
            List<StudentDto> studentDtoList = List.of( new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of()));
            when(dbStudentService.findByName("kow","J")).thenReturn(studentList);
            when(studentMapper.mapToStudentDtoList(studentList)).thenReturn(studentDtoList);

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
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].degreeCourse", Matchers.is("Politologia")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].teacherSet", Matchers.hasSize(0)));
    }

    @Test
    void shouldCreateStudent() throws Exception {
        //Given
            Student student = new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
            StudentDto studentDto = new StudentDto(1,"Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia", Set.of());
            when(dbStudentService.saveStudent(any(Student.class))).thenReturn(student);
            when(studentMapper.mapToStudentDto(student)).thenReturn(studentDto);

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
            Student student = new Student("Jan","Kowalski",22,"jkowalksi@Gmail.com","Politologia");
            when(dbStudentService.existById(anyLong())).thenReturn(true);
            when(dbStudentService.getStudent(anyLong())).thenReturn(Optional.of(student));

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