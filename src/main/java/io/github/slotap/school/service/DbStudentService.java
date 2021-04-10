package io.github.slotap.school.service;

import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.model.StudentDto;
import io.github.slotap.school.repository.StudentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DbStudentService implements SchoolService<StudentDto, Student> {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public DbStudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentDto> getAll(Pageable pageable) {
        List<Student> studentList = studentRepository.findAll(pageable).getContent();
        return studentMapper.mapToStudentDtoList(studentList);
    }

    @Override
    public StudentDto save(Student student) {
        Student savedStudent =  studentRepository.save(student);
        return studentMapper.mapToStudentDto(savedStudent);
    }

    @Override
    public void delete(final long id) {
        if (studentRepository.existsById(id)) {
            Student student = studentRepository.findById(id).get();
            student.getTeachers().forEach(teacher -> teacher.getStudents().remove(student));
            studentRepository.deleteById(id);
        }
    }

    @Override
    public Optional<StudentDto> getDtoData(final long id) {
         return studentRepository.findById(id)
                 .map(studentMapper::mapToStudentDto);
    }

    @Override
    public Optional<Student> getData(long id) {
        return studentRepository.findById(id);
    }

    @Override
    public boolean existById(long id) {
        return studentRepository.existsById(id);
    }

    @Override
    public List<StudentDto> findByName(String lastName, String firstName) {
        List<Student> students = studentRepository.findByLastnameOrFirstnameOrderByLastname(lastName, firstName);
        return studentMapper.mapToStudentDtoList(students);
    }
}
