package co.id.jalin.qrmapper.service.dashboard;

import co.id.jalin.qrmapper.cache.ApplicationParameterManager;
import co.id.jalin.qrmapper.cache.CredentialDataManager;
import co.id.jalin.qrmapper.cache.RcMappingManager;
import co.id.jalin.qrmapper.dto.dashboard.DataCacheDto;
import co.id.jalin.qrmapper.util.SortObjectComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.RESP_CODE_SUCCESS;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.RESP_MESSAGE_SUCCESS;

@Log4j2
@Service
@RequiredArgsConstructor
public class DataCacheManagerService {

    private final ApplicationParameterManager applicationParameterManager;
    private final CredentialDataManager credentialDataManager;
    private final RcMappingManager rcMappingManager;


    private Page<DataCacheDto> convertListToPage(Pageable pageable, List<DataCacheDto> list){
        if (pageable.getSort().isSorted()) {
            Comparator<DataCacheDto> comparator = new SortObjectComparator<>(pageable.getSort(),DataCacheDto::getObject);
            list.sort(comparator);
        }
        var start = (int) pageable.getOffset();
        var end = Math.min((start + pageable.getPageSize()), list.size());
        var contents = list.subList(start,end);
        return new PageImpl<>(contents,pageable,list.size());
    }

    public Page<DataCacheDto> getApplicationParameterCache(Pageable pageable){
        log.debug("Get IMap data cache of getApplicationParameterMap");
        List<DataCacheDto> list = new ArrayList<>();
        applicationParameterManager.getApplicationParameterMap().forEach((key, applicationParameter) ->
                list.add(DataCacheDto.builder().key(key).object(applicationParameter).build())
        );
        return convertListToPage(pageable,list);
    }

    public Page<DataCacheDto> getApplicationParameterValueCache(Pageable pageable){
        log.debug("Get IMap data cache of getApplicationParameterValueMap");
        List<DataCacheDto> list = new ArrayList<>();
        applicationParameterManager.getApplicationParameterValueMap().forEach((key, value) ->
                list.add(DataCacheDto.builder().key(key).object(value).build())
        );
        return convertListToPage(pageable,list);
    }

    public DataCacheDto reloadApplicationParameterCache(){
        log.debug("Reload IMap data cache of applicationParameterManager");
        applicationParameterManager.reload();
        return DataCacheDto.builder()
                .responseCode(RESP_CODE_SUCCESS)
                .responseMessage(RESP_MESSAGE_SUCCESS)
                .build();
    }

    public Page<DataCacheDto> getCredentialDataByUserPassCache(Pageable pageable){
        log.debug("Get IMap data cache of getCredentialDataIMapByUserPass");
        List<DataCacheDto> list = new ArrayList<>();
        credentialDataManager.getCredentialDataIMapByUserPass().forEach((key, credentialData) ->
                list.add(DataCacheDto.builder().key(key).object(credentialData).build())
        );
        return convertListToPage(pageable,list);
    }

    public Page<DataCacheDto> getCredentialDataByCredIdCache(Pageable pageable){
        log.debug("Get IMap data cache of getCredentialDataIMapByCredId");
        List<DataCacheDto> list = new ArrayList<>();
        credentialDataManager.getCredentialDataIMapByCredId().forEach((key, credentialData) ->
                list.add(DataCacheDto.builder().key(key).object(credentialData).build())
        );
        return convertListToPage(pageable,list);
    }

    public DataCacheDto reloadCredentialDataCache(){
        log.debug("Reload IMap data cache of credentialDataManager");
        credentialDataManager.reload();
        return DataCacheDto.builder()
                .responseCode(RESP_CODE_SUCCESS)
                .responseMessage(RESP_MESSAGE_SUCCESS)
                .build();
    }


    public Page<DataCacheDto> getRcMappingJalinToDanaCache(Pageable pageable){
        log.debug("Get IMap data cache of getRcMappingIMapJalinToDana");
        List<DataCacheDto> list = new ArrayList<>();
        rcMappingManager.getRcMappingIMapJalinToDana().forEach((key, rcMapping) ->
                list.add(DataCacheDto.builder().key(key).object(rcMapping).build())
        );
        return convertListToPage(pageable,list);
    }

    public Page<DataCacheDto> getRcMappingDanaToJalinCache(Pageable pageable){
        log.debug("Get IMap data cache of getRcMappingIMapDanaToJalin");
        List<DataCacheDto> list = new ArrayList<>();
        rcMappingManager.getRcMappingIMapDanaToJalin().forEach((key, rcMapping) ->
                list.add(DataCacheDto.builder().key(key).object(rcMapping).build())
        );
        return convertListToPage(pageable,list);
    }

    public DataCacheDto reloadRcMappingCache(){
        log.debug("Reload IMap data cache of rcMappingManager");
        rcMappingManager.reload();
        return DataCacheDto.builder()
                .responseCode(RESP_CODE_SUCCESS)
                .responseMessage(RESP_MESSAGE_SUCCESS)
                .build();
    }
}
