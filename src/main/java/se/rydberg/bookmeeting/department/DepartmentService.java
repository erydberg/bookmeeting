package se.rydberg.bookmeeting.department;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

import java.util.UUID;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;

    public DepartmentService(DepartmentRepository departmentRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public DepartmentDTO saveDTO(DepartmentDTO departmentDto) {
        Department entity = toEntity(departmentDto);
        Department saved = save(entity);
        return toDto(saved);
    }

    public Department findBy(UUID uuid) throws NotFoundInDatabaseException {
        return departmentRepository.findById(uuid)
                .orElseThrow(() -> new NotFoundInDatabaseException("Kunde inte hitta avdelningen i systemet."));
    }

    public DepartmentDTO findDTOBy(UUID id) throws NotFoundInDatabaseException {
        Department entity = findBy(id);
        return toDto(entity);
    }

    protected DepartmentDTO toDto(Department entity) {
        if (entity != null) {
            return modelMapper.map(entity, DepartmentDTO.class);
        } else {
            return null;
        }
    }

    protected Department toEntity(DepartmentDTO dto) {
        if (dto != null) {
            return modelMapper.map(dto, Department.class);
        } else {
            return null;
        }
    }


}
