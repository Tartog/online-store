package com.example.OnlineStore.service;

import com.example.OnlineStore.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAllRole();
    Role saveRole(Role role);
    Role findById(Long id);
    void updateRole(Role role);
    void deleteRole(Long id);
    Optional<Role> findByUserRole(String userRole);
}
