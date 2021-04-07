package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.StudentMapper;
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
    private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper;

    public StudentController(DbStudentService dbService, StudentMapper studentMapper, TeacherMapper teacherMapper) {
        this.dbService = dbService;
        this.studentMapper = studentMapper;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents(Pageable page){
        logger.info("Fetching all students - custom pageable");
        List<Student> studentList = dbService.getAll(page).getContent();
        return ResponseEntity.ok(studentMapper.mapToStudentDtoList(studentList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getOneStudent(@PathVariable long id){
            return dbService.getStudent(id)
                    .map(student -> ResponseEntity.ok(studentMapper.mapToStudentDto(student)))
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/filter/{id}")
    public ResponseEntity<?> getFilteredTeachers(@PathVariable long id){
        logger.info("Filtering all teachers assigned to a selected student");
        return dbService.getStudent(id)
                .map(Student::getTeachers)
                .map(teachers -> teachers.stream()
                        .map(teacherMapper::mapToTeacherDto)
                        .collect(Collectors.toSet()))
                .map(teachersSet -> ResponseEntity.ok(teachersSet))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid Student newStudent){
        logger.info("Creating new Student");
        Student result = dbService.saveStudent(newStudent);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(studentMapper.mapToStudentDto(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent (@PathVariable long id, @RequestBody @Valid Student toUpdate ){
        logger.info("Updating Student");
        if(!dbService.existById(id)){
            return ResponseEntity.notFound().build();
        }
        dbService.getStudent(id).ifPresent(student ->{
                                                        student.updateStudent(toUpdate);
                                                        dbService.saveStudent(student);
                                            });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable long id) {
        logger.info("Deleting Student");
        if (!dbService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        dbService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
