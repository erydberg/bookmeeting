package se.rydberg.bookmeeting.department;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import se.rydberg.bookmeeting.meeting.NotFoundInDatabaseException;

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

    public Department getDepartmentWithAttendees(UUID uuid) {
        return departmentRepository.getDepartmentWithAttendees(uuid);
    }

    public Department getDepartmentFullOld(UUID uuid) {
        Department departmentWithAttendees = departmentRepository.getDepartmentWithAttendees(uuid);
        Department departmentWithMeetings = departmentRepository.getDepartmentWithMeetings(uuid);
        departmentWithAttendees.addMeetings(departmentWithMeetings.getMeetings());
        return departmentWithAttendees;
    }

    public Department getDepartmentFull(UUID uuid){
        return departmentRepository.getFullDepartment(uuid);
    }

    public Department getDepartmentWithMeetings(UUID uuid) {
        return departmentRepository.getDepartmentWithMeetings(uuid);
    }

    public void delete(Department department){
        departmentRepository.delete(department);
    }

    public void deleteDTO(DepartmentDTO dto){
        delete(toEntity(dto));
    }

    public void deleteById(UUID id){
        departmentRepository.deleteById(id);
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

    public List<Department> getAll(){
        return departmentRepository.findAll(Sort.by(Sort.Direction.ASC,"name"));
    }

    public List<DepartmentDTO> getAllDTOs() {
        return departmentRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(department -> toDto(department)).toList();
    }
}
