package com.mindbowser.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mindbowser.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Set<Role> findByName(String name);

}
