package io.github.slotap.school.service;

import io.github.slotap.school.dto.SchoolMemberDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SchoolMemberService <T extends SchoolMemberDto>{
    List<T> getAllMembers(Pageable pageable);

    Optional<T> getById(long id);

    T saveMember(T schoolMember);

    void deleteMember(long id);

    List<T> getByLastnameFirstname(String lastname, String firstname);

    Optional<T> updateMember(long id, T schoolMember);

    boolean existById(long id);
}
