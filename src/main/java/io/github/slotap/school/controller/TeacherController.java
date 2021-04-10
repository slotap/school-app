package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.model.Teacher;
import io.github.slotap.school.model.TeacherDto;
import io.github.slotap.school.service.DbTeacherService;
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
@RequestMapping("/teachers")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final DbTeacherService dbService;
    private final StudentMapper studentMapper;

    public TeacherController(DbTeacherService dbService, StudentMapper studentMapper) {
        this.dbService = dbService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    ResponseEntity<List<TeacherDto>> getAllTeachers(Pageable page) {
        logger.info("Fetching all teachers - custom pageable");
        List<TeacherDto> teacherList = dbService.getAll(page);
        return ResponseEntity.ok(teacherList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getOneTeacher(@PathVariable long id){
        logger.info("Fetching a teacher");
        return dbService.getDtoData(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<?> getFilteredStudents(@PathVariable long id){
        logger.info("Filtering all students assigned to a selected teacher");
        return dbService.getData(id)
                .map(Teacher::getStudents)
                .map(students -> students.stream()
                        .map(studentMapper::mapToStudentDto)
                        .collect(Collectors.toSet()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherDto>> findTeachersByName(@RequestParam(name = "lastname") String lastName,@RequestParam(name = "firstname") String firstName ){
        logger.info("Looking for searched Teachers");
        List<TeacherDto> resultList = dbService.findByName(lastName,firstName);
        return ResponseEntity.ok(resultList);
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody @Valid Teacher newTeacher){
        logger.info("Creating new Teacher");
        TeacherDto result = dbService.save(newTeacher);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher (@PathVariable long id, @RequestBody @Valid Teacher toUpdate ){
        logger.info("Updating Teacher");
        if(!dbService.existById(id)){
            return ResponseEntity.notFound().build();
        }
        dbService.getData(id).ifPresent(teacher ->{
            teacher.updateTeacher(toUpdate);
            dbService.save(teacher);
        });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable long id) {
        logger.info("Deleting Teacher");
        if (!dbService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        dbService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
