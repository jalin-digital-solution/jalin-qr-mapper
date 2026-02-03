package co.id.jalin.qrmapper.service;

import co.id.jalin.qrmapper.dto.GeneralDto;
import co.id.jalin.qrmapper.util.constant.GeneralConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.RESP_MESSAGE_DO_NOT_HONOR;


@Log4j2
@Service
public class GeneralService {

    public GeneralDto echoMessage(GeneralDto generalDto) throws NoSuchFieldException {
        log.info("GeneralService::echoMessage started");
        generalDto.setResponseCode(GeneralConstant.RESP_CODE_SUCCESS);
        generalDto.setResponseMessage(GeneralConstant.RESP_MESSAGE_SUCCESS);
        if (Objects.equals(generalDto.getKey(),"error")) throw new NoSuchFieldException(RESP_MESSAGE_DO_NOT_HONOR + " error key field at " + LocalDateTime.now());
        return generalDto;
    }
}
