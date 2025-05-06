package uz.lb.fxchatserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.lb.fxchatserver.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM  account  WHERE LOWER(login) LIKE LOWER(CONCAT('%', :login, '%'))", nativeQuery = true)
    List<User> findUsersByLogin(@Param(value = "login") String login);
    User findUserByLogin(String login);
}
