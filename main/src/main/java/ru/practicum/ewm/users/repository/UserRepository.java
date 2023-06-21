package ru.practicum.ewm.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.users.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByIdIn(Integer[] usersId);
}