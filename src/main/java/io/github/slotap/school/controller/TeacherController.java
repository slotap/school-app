package io.github.slotap.school.controller;

import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.mapper.TeacherMapper;
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
    private final TeacherMapper teacherMapper;
    private final StudentMapper studentMapper;

    public TeacherController(DbTeacherService dbService, TeacherMapper teacherMapper, StudentMapper studentMapper) {
        this.dbService = dbService;
        this.teacherMapper = teacherMapper;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    ResponseEntity<List<TeacherDto>> getAllTeachers(Pageable page) {
        logger.info("Fetching all teachers - custom pageable");
        List<Teacher> teacherList = dbService.getAll(page).getContent();
        return ResponseEntity.ok(teacherMapper.mapToTeacherDtoList(teacherList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> getOneTeacher(@PathVariable long id){
        logger.info("Fetching a teacher");
        return dbService.getTeacher(id)
                .map(teacher -> ResponseEntity.ok(teacherMapper.mapToTeacherDto(teacher)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<?> getFilteredStudents(@PathVariable long id){
        logger.info("Filtering all students assigned to a selected teacher");
        return dbService.getTeacher(id)
                .map(Teacher::getStudents)
                .map(students -> students.stream()
                                    .map(studentMapper::mapToStudentDto)
                                    .collect(Collectors.toSet()))
                .map(studentSet -> ResponseEntity.ok(studentSet))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TeacherDto>> findTeachersByName(@RequestParam(name = "lastname") String lastName,@RequestParam(name = "firstname") String firstName ){
        logger.info("Looking for searched Teachers");
        List<Teacher> resultList = dbService.findByName(lastName,firstName);
        return ResponseEntity.ok(teacherMapper.mapToTeacherDtoList(resultList));
    }

    @PostMapping
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody @Valid Teacher newTeacher){
        logger.info("Creating new Teacher");
        Teacher result = dbService.saveTeacher(newTeacher);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(teacherMapper.mapToTeacherDto(result));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTeacher (@PathVariable long id, @RequestBody @Valid Teacher toUpdate ){
        logger.info("Updating Teacher");
        if(!dbService.existById(id)){
            return ResponseEntity.notFound().build();
        }
        dbService.getTeacher(id).ifPresent(teacher ->{
            teacher.updateTeacher(toUpdate);
            dbService.saveTeacher(teacher);
        });
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable long id) {
        logger.info("Deleting Teacher");
        if (!dbService.existById(id)) {
            return ResponseEntity.notFound().build();
        }
        dbService.deleteTeacher(id);
        return ResponseEntity.noContent().build();
    }
}
