package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    public Role findByRoleName(String roleName);

    public List<Role> findAll();

    public Collection<Role> findRolesByIds(Collection<Long> roleIds);
}
