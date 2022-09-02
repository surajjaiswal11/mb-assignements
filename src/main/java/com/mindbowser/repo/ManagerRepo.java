package com.mindbowser.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindbowser.entity.Manager;

@Repository
public interface ManagerRepo extends JpaRepository<Manager, Long> {

	Optional<Manager> findByEmailAndIsDeleted(String username, boolean b);

	List<Manager> findAllByIsDeleted(boolean b);

}
