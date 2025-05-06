package uz.lb.fxchatserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.lb.fxchatserver.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
