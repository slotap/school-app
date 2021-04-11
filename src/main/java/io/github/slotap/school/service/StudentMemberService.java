package io.github.slotap.school.service;

import io.github.slotap.school.dto.StudentDto;
import io.github.slotap.school.mapper.StudentMapper;
import io.github.slotap.school.model.Student;
import io.github.slotap.school.repository.StudentRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentMemberService implements SchoolMemberService<StudentDto>{

    private final StudentRepository studentRepository;

    public StudentMemberService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public List<StudentDto> getAllMembers(Pageable pageable) {
        List<Student> studentList = studentRepository.findAll(pageable).getContent();
        return studentList.stream()
                .map(StudentMapper::mapToStudentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDto> getById(long id) {
        if(!studentRepository.existsById(id)){
            return Optional.empty();
        }
        return studentRepository.findById(id)
                .map(StudentMapper::mapToStudentDto);
    }

    @Override
    public StudentDto saveMember(StudentDto schoolMember) {
        Student student = studentRepository.save(StudentMapper.mapToStudent(schoolMember));
        return StudentMapper.mapToStudentDto(student);
    }

    @Transactional
    @Override
    public void deleteMember(final long id) {
        if (studentRepository.existsById(id)) {
            Student student = studentRepository.findById(id).get();
            student.getTeachers().forEach(teacher -> teacher.getStudents().remove(student));
            studentRepository.deleteById(id);
        }
    }

    @Override
    public List<StudentDto> getByLastnameFirstname(String lastname, String firstname) {
        List<Student> students = studentRepository.findByLastnameOrFirstnameOrderByLastname(lastname, firstname);
        return students.stream()
                .map(StudentMapper::mapToStudentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StudentDto> updateMember(long id, StudentDto schoolMember) {
        Student studentToUpdate = StudentMapper.mapToStudent(schoolMember);
        return this.getById(id)
                .map(StudentMapper::mapToStudent)
                .map(student -> student.updateStudent(studentToUpdate))
                .map(StudentMapper::mapToStudentDto)
                .map(this::saveMember);
    }

    @Override
    public boolean existById(long id) {
        return studentRepository.existsById(id);
    }
}
