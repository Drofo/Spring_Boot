package web.crud_spring_hiber.service;

import org.springframework.stereotype.Service;
import web.crud_spring_hiber.model.Role;
import web.crud_spring_hiber.dao.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Role> getRolesByIds(List<Long> roleIds) {
        return roleRepository.findAllById(roleIds);
    }
}
