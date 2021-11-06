package se.rydberg.bookmeeting.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Role getRoleByName(String name);

}
