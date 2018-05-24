package com.demo.controller;

import com.google.gson.JsonSyntaxException;
import com.demo.bean.model.Organization;
import com.demo.common.ConstantsData;
import com.demo.common.RespDTO;
import com.demo.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/organization")
@Slf4j
public class OrganizationController extends BaseController {
    @Resource
    private OrganizationService organizationService;

    @PostMapping
    public RespDTO<String> addOrganization(@RequestBody String body) {
        try {
            log.info("===addOrganization==="+body);
            Organization organization = gson.fromJson(body,Organization.class);
            return organizationService.addOrganization(organization);
        } catch(JsonSyntaxException e){
            log.error("addOrganization error: ", e);
            return RespDTO.fail(ConstantsData.JSON_PARSE_ERROR);
        } catch (Exception e) {
            log.error("addOrganization error: ", e);
            return RespDTO.fail(ConstantsData.SERVER_INTERNAL_ERROR);
        }
    }

    @GetMapping
    public RespDTO<Organization> queryByOrganunitCode(@RequestParam String organunitCode){
        try {
            log.info("===queryByOrganunitCode===" + organunitCode);
            if(StringUtils.isEmpty(organunitCode)){
                return RespDTO.fail(ConstantsData.ARGS_IS_EMPTY);
            }
            return organizationService.queryByOrganunitCode(organunitCode);
        }
        catch (Exception e) {
            log.error("queryByOrganunitCode error: ",e);
            return RespDTO.fail(ConstantsData.SERVER_INTERNAL_ERROR);
        }
    }
}
