package com.ctrl.dscatalog.services;

import com.ctrl.dscatalog.dto.RoleDTO;
import com.ctrl.dscatalog.dto.UserDTO;
import com.ctrl.dscatalog.dto.UserInsertDTO;
import com.ctrl.dscatalog.entities.Role;
import com.ctrl.dscatalog.entities.User;
import com.ctrl.dscatalog.repositories.RoleRepository;
import com.ctrl.dscatalog.repositories.UserRepository;
import com.ctrl.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(Pageable pageable) {
        Page<User> list = repository.findAll(pageable);
        return list.map(user -> new UserDTO(user));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User user = new User();
        copyDtoToEntity(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = repository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(UserDTO dto, Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        copyDtoToEntity(dto, user);
        return new UserDTO(user);
    }

    @Transactional
    public void delete(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        repository.delete(user);
        repository.flush();
    }

    private void copyDtoToEntity(UserDTO dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        user.getRoles().clear();
        for (RoleDTO roleDTO : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoles().add(role);
        }
    }
}
