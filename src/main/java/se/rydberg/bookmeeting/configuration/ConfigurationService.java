package se.rydberg.bookmeeting.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;
    private final ModelMapper modelMapper;

    public ConfigurationService(ConfigurationRepository configurationRepository, ModelMapper modelMapper) {
        this.configurationRepository = configurationRepository;
        this.modelMapper = modelMapper;
    }

    public Configuration save(Configuration configuration){
        return configurationRepository.save(configuration);
    }

    public Configuration saveDTO(ConfigurationDTO config) {
        return save(toEntity(config));
    }

    public ConfigurationDTO loadConfiguration(){
        List<Configuration> configurations = configurationRepository.findAll();
        if(configurations.isEmpty()){
            return ConfigurationDTO.builder().email("").emailkey("").build();
        }else{
            return toDto(configurations.get(0));
        }
    }

    public Configuration toEntity(ConfigurationDTO dto){
        if(dto!=null){
            return modelMapper.map(dto, Configuration.class);
        }else{
            return null;
        }
    }

    public ConfigurationDTO toDto(Configuration entity){
        if(entity!=null){
            return modelMapper.map(entity, ConfigurationDTO.class);
        }else{
            return null;
        }
    }
}
