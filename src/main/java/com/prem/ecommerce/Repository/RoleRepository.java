package com.prem.ecommerce.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prem.ecommerce.Model.AppRole;
import com.prem.ecommerce.Model.Roles;

public interface RoleRepository extends JpaRepository<Roles, Long>{

    Optional<Roles> findByRoleName(AppRole roleUser);

}
