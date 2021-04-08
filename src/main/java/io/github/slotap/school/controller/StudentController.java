package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.TeacherMapper;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import io.github.slotap.school.service.DbStudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final DbStudentService dbService;
    private final TeacherMapper teacherMapper;

    public StudentController(DbStudentService dbService, TeacherMapper teacherMapper) {
        this.dbService = dbService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents(Pageable page) {
        logger.info("Fetching all students - custom pageable");
        List<StudentDto> studentList = dbService.getAll(page);
        return ResponseEntity.ok(studentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getOneStudent(@PathVariable long id) {
        logger.info("Fetching a student");
        return dbService.getEntity(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/teachers")
    public ResponseEntity<?> getFilteredTeachers(@PathVariable long id) {
        logger.info("Filtering all teachers assigned to a selected student");
        return dbService.getEntity(id)
                .map(StudentDto::getTeacherSet)
                .map(teachers -> teachers.stream()
                        .map(teacherMapper::mapToTeacherDto)
                        .collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentDto>> findStudentsByName(@RequestParam(name = "lastname") String lastName, @RequestParam(name = "firstname") String firstName) {
        logger.info("Looking for searched Teachers");
        List<StudentDto> resultList = dbService.findByName(lastName, firstName);
        return ResponseEntity.ok(resultList);
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid Student newStudent) {
        logger.info("Creating new Student");
        StudentDto result = dbService.save(newStudent);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable long id, @RequestBody @Valid Student toUpdate) {
        logger.info("Updating Student");
        if (!dbService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        dbService.getEntityFromDB(id).ifPresent(student -> {
            student.updateStudent(toUpdate);
            dbService.save(student);
        });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id) {
        logger.info("Deleting Student");
        if (!dbService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        dbService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
