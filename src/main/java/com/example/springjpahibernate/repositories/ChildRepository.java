package com.example.springjpahibernate.repositories;

import com.example.springjpahibernate.entities.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildRepository extends JpaRepository<Child,Long> {
}
