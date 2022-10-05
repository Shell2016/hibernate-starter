package ru.michaelshell.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.QueryHints;
import ru.michaelshell.dao.UserRepository;
import ru.michaelshell.dto.UserCreateDto;
import ru.michaelshell.dto.UserReadDto;
import ru.michaelshell.entity.User;
import ru.michaelshell.mapper.Mapper;
import ru.michaelshell.mapper.UserCreateMapper;
import ru.michaelshell.mapper.UserReadMapper;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userCreateDto) {
        return userRepository.save(userCreateMapper.mapFrom(userCreateDto)).getId();
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        var graph = userRepository.getEntityManager().createEntityGraph(User.class);
        graph.addAttributeNodes("company");
        Map<String, Object> properties = Map.of(
                QueryHints.HINT_LOADGRAPH, graph
        );
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        var optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(user -> userRepository.delete(user.getId()));
        return optionalUser.isPresent();
    }
}
