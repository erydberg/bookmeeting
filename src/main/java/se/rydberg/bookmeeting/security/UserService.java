package se.rydberg.bookmeeting.security;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    public User findBy(Long id) throws NotFoundInDatabaseException {
        Optional<User> user = userRepository.findById(id);
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundInDatabaseException("Kan inte hitta användaren med det id-et"));
    }

    public UserDTO findDTOby(Long id) throws NotFoundInDatabaseException {
        User user = findBy(id);
        return toDto(user);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("hittar inte användare " + username);
        }
        return user.get();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User savenew(UserDTO userDto) {
        User user = toEntity(userDto);
        if (user != null) {
            Role role = roleService.getNormalUserRole();
            user.addRole(role);
            user.setEnabled(true);
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String losen = "{bcrypt}" + bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(losen);

            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private User toEntity(UserDTO user) {
        if (user != null) {
            return modelMapper.map(user, User.class);
        } else {
            return null;
        }
    }

    private UserDTO toDto(User user) {
        if (user != null) {
            return modelMapper.map(user, UserDTO.class);
        } else {
            return null;
        }
    }
}
