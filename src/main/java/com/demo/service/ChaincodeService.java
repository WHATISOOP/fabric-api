package com.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
public class ChaincodeService extends BaseService {
    @Resource
    private FabricService fabricService;

    public boolean instantiateChaincode() {
        try {
            fabricService.installChaincode();
        } catch (InvalidArgumentException | ProposalException e) {
            log.error("installChaincode error:",e);
        }
        try {
            fabricService.instantiateChaincode();
        } catch (IOException | ProposalException | InvalidArgumentException | ExecutionException | InterruptedException | ChaincodeEndorsementPolicyParseException | TimeoutException e) {
            log.error("instantiateChaincode error:",e);
        }
        return true;
    }

    public boolean upgradeChaincode() {
        try {
            fabricService.installChaincode();
        } catch (InvalidArgumentException | ProposalException e) {
            log.error("installChaincode error:",e);
        }
        try {
            fabricService.upgradeChaincode();
        } catch (IOException | ProposalException | InvalidArgumentException | ExecutionException | InterruptedException | ChaincodeEndorsementPolicyParseException | TimeoutException e) {
            log.error("upgradeChaincode error:",e);
        }
        return true;
    }

}