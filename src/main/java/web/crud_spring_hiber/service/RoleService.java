package web.crud_spring_hiber.service;

import web.crud_spring_hiber.model.Role;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    List<Role> getRolesByIds(List<Long> roleIds);
}
