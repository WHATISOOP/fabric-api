package com.demo.controller;

import com.demo.common.ConstantsData;
import com.demo.common.RespDTO;
import com.demo.service.ChaincodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/chaincode")
@Slf4j
public class ChaincodeController extends BaseController {
    @Resource
    private ChaincodeService chaincodeService;

    @GetMapping("/install")
    public RespDTO<Boolean> install() {
        try {
            chaincodeService.instantiateChaincode();
            return RespDTO.success();
        } catch (Exception e) {
            log.error("chaincode install error: ", e);
            return RespDTO.fail(ConstantsData.SERVER_INTERNAL_ERROR);
        }
    }

    @GetMapping("/upgrade")
    public RespDTO<Boolean> upgrade() {
        try {
            chaincodeService.upgradeChaincode();
            return RespDTO.success();
        } catch (Exception e) {
            log.error("chaincode upgrade error: ", e);
            return RespDTO.fail(ConstantsData.SERVER_INTERNAL_ERROR);
        }
    }
}