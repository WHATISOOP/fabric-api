package com.demo.service;


import com.demo.bean.model.Organization;
import com.demo.common.ConstantsData;
import com.demo.common.RespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrganizationService extends BaseService {

    @Resource
    private FabricService fabricService;

    public RespDTO<String> addOrganization(Organization organization) throws Exception{
        fabricService.invoke("addOrganization", new String[] { gson.toJson(organization) });
        return RespDTO.success();
    }

    public RespDTO<Organization> queryByOrganunitCode(String organunitCode) {
        String jsonStr = fabricService.query("queryByOrganunitCode", new String[] { organunitCode });
        log.info("===queryByOrganunitCode===" + jsonStr);
        if (StringUtils.isEmpty(jsonStr) || "null".equals(jsonStr)) {
            return RespDTO.fail(ConstantsData.NO_DATA);
        }
        Organization organization = simpleGson.fromJson(jsonStr, Organization.class);
        return RespDTO.success(organization);
    }
}
