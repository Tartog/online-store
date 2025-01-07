package com.example.OnlineStore.service.Impl;

import com.example.OnlineStore.model.Role;
import com.example.OnlineStore.repository.RoleRepository;
import com.example.OnlineStore.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
@Transactional
@Order(1)
public class RoleServiceImpl implements RoleService, CommandLineRunner {

    private RoleRepository repository;

    @Override
    public List<Role> findAllRole() {
        return repository.findAll();
    }

    @Override
    public Role saveRole(Role role) {
        return repository.save(role);
    }

    @Override
    public Role findById(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public Optional<Role> findByUserRole(String userRole){
        return repository.findByUserRole(userRole);
    }

    @Override
    public void updateRole(Role role) {
        repository.save(role);
    }

    @Override
    public void deleteRole(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void run(String... args) throws Exception {
        Role user = new Role();
        user.setUserRole("User");
        Role seller = new Role();
        seller.setUserRole("Seller");
        Role admin = new Role();
        admin.setUserRole("Admin");
        if (repository.findByUserRole(user.getUserRole()).isEmpty()){
            repository.save(user);
        }
        if (repository.findByUserRole(seller.getUserRole()).isEmpty()){
            repository.save(seller);
        }
        if (repository.findByUserRole(admin.getUserRole()).isEmpty()){
            repository.save(admin);
        }
    }
}
