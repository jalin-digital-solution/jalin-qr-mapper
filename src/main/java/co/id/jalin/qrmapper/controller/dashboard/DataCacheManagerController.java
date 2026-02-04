package co.id.jalin.qrmapper.controller.dashboard;

import co.id.jalin.qrmapper.dto.dashboard.DataCacheDto;
import co.id.jalin.qrmapper.service.dashboard.DataCacheManagerService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@AllArgsConstructor
public class DataCacheManagerController extends DashboardController {

    private DataCacheManagerService dataCacheManagerService;

    @GetMapping("${api.path.cache.application.parameter}")
    public Page<DataCacheDto> getApplicationParameterCache(Pageable pageable){
        log.debug("DataCacheManagerController::getApplicationParameterCache started");
        return dataCacheManagerService.getApplicationParameterCache(pageable);
    }

    @GetMapping("${api.path.cache.application.parameter-value}")
    public Page<DataCacheDto> getApplicationParameterValueCache(Pageable pageable){
        log.debug("DataCacheManagerController::getApplicationParameterValueCache started");
        return dataCacheManagerService.getApplicationParameterValueCache(pageable);
    }

    @PostMapping("${api.path.cache.application.parameter.reload}")
    public DataCacheDto reloadApplicationParameterCache(){
        log.debug("DataCacheManagerController::reloadApplicationParameterCache started");
        return dataCacheManagerService.reloadApplicationParameterCache();
    }

    @GetMapping("${api.path.cache.credential.data.user-pass}")
    public Page<DataCacheDto> getCredentialDataByUserPassCache(Pageable pageable){
        log.debug("DataCacheManagerController::getCredentialDataByUserPassCache started");
        return dataCacheManagerService.getCredentialDataByUserPassCache(pageable);
    }

    @GetMapping("${api.path.cache.credential.data.cred-identifier}")
    public Page<DataCacheDto> getCredentialDataByCredIdCache(Pageable pageable){
        log.debug("DataCacheManagerController::getCredentialDataByCredIdCache started");
        return dataCacheManagerService.getCredentialDataByCredIdCache(pageable);
    }

    @PostMapping("${api.path.cache.credential.data.reload}")
    public DataCacheDto reloadCredentialDataCache(){
        log.debug("DataCacheManagerController::reloadCredentialDataCache started");
        return dataCacheManagerService.reloadCredentialDataCache();
    }

    @GetMapping("${api.path.cache.rc.mapping.esb-dana}")
    public Page<DataCacheDto> getRcMappingJalinToDanaCache(Pageable pageable){
        log.debug("DataCacheManagerController::getRcMappingJalinToDanaCache started");
        return dataCacheManagerService.getRcMappingJalinToDanaCache(pageable);
    }

    @GetMapping("${api.path.cache.rc.mapping.dana-esb}")
    public Page<DataCacheDto> getRcMappingDanaToJalinCache(Pageable pageable){
        log.debug("DataCacheManagerController::getRcMappingDanaToJalinCache started");
        return dataCacheManagerService.getRcMappingDanaToJalinCache(pageable);
    }

    @PostMapping("${api.path.cache.rc.mapping.reload}")
    public DataCacheDto reloadRcMappingCache(){
        log.debug("DataCacheManagerController::reloadRcMappingCache started");
        return dataCacheManagerService.reloadRcMappingCache();
    }
}
