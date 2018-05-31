# configtxlator
## install chaincode
curl localhost:8080/chaincode/install
## upgrade chaincode
curl localhost:8080/chaincode/upgrade
## test data
curl -X POST -H "Content-Type: application/json" -d '{"organunitName":"XX组织","organunitCode":"a001","province":"beijing","city":"haidian"}' http://localhost:8080/organization  
curl http://localhost:8080/organization?organunitCode=a001
## add an org 
###  新建org2的配置文件
1. mkdir org2 && cd org2  
2. add configtx.yaml and crypto-config.yaml
### 根据配置文件生成对应配置
1. cryptogen generate --config crypto-config.yaml --output=crypto-config
2. export FABRIC_CFG_PATH=$PWD && configtxgen -printOrg Org2MSP > ./org2.json
### 将排序节点相关证书拷贝到org2/crypto-config/里，让新的组织可以连接到排序节点
cp -r ../org1/crypto-config/ordererOrganizations/ ./crypto-config/
### update原有网络的配置文件
#### 获取system channel的配置文件
1. get config block (cd cli docker)  
CORE_PEER_LOCALMSPID=OrdererMSP  
CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/users/Admin@example.com/msp  
peer channel fetch config -o orderer.example.com:7050 -c testchainid  
2. configtxlator start (cd cli docker)  
3. decoding current genesis block (cd cli docker)  
curl -X POST --data-binary @testchainid_config.block http://127.0.0.1:7059/protolator/decode/common.Block > testchainid.json  
4. Extract current config (cd cli docker)  
apt update && apt install jq && jq .data.data[0].payload.data.config testchainid.json > testchainid_config.json  
5. generating new config (cd cli docker)   
jq -s '.[0] * {"channel_group":{"groups":{"Application":{"groups": {"Org2MSP":.[1]}}}}}' testchainid_config.json ./org2.json > testchainid_modified_config.json
6. Translate testchainid_config.json and testchainid_modified_config.json to proto (cd cli docker)  
curl -X POST --data-binary @testchainid_config.json http://127.0.0.1:7059/protolator/encode/common.Config > testchainid_config.pb  
curl -X POST --data-binary @testchainid_modified_config.json http://127.0.0.1:7059/protolator/encode/common.Config > testchainid_modified_config.pb  
7. Computing config update (cd cli docker)  
curl -X POST -F original=@testchainid_config.pb -F updated=@testchainid_modified_config.pb http://127.0.0.1:7059/configtxlator/compute/update-from-configs -F channel=testchainid > testchainid_config_update.pb
8. Decoding config update (cd cli docker)  
curl -X POST --data-binary @testchainid_config_update.pb http://127.0.0.1:7059/protolator/decode/common.ConfigUpdate > testchainid_config_update.json  
9. Generating config update envelope (cd cli docker)  
echo '{"payload":{"header":{"channel_header":{"channel_id":"testchainid", "type":2}},"data":{"config_update":'$(cat testchainid_config_update.json)'}}}' > testchainid_config_update_in_envelope.json
10. convert it into the proto (cd cli docker)  
curl -X POST --data-binary @testchainid_config_update_in_envelope.json http://127.0.0.1:7059/protolator/encode/common.Envelope > testchainid_config_update_in_envelope.pb
11. update (cd cli docker)    
CORE_PEER_LOCALMSPID=OrdererMSP  
CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/example.com/users/Admin@example.com/msp  
peer channel signconfigtx -f ./testchainid_config_update_in_envelope.pb  
peer channel update -f ./testchainid_config_update_in_envelope.pb -o orderer.example.com:7050 -c testchainid    


#### 获取app channel的配置文件
1. get config block (cd cli docker)  
CORE_PEER_LOCALMSPID=Org1MSP  
CORE_PEER_MSPCONFIGPATH=/opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/peerOrganizations/org1.example.com/users/Admin@org1.example.com/msp  
peer channel fetch config -o orderer.example.com:7050 -c demochannel  
2. configtxlator start  
3. decoding current genesis block  
curl -X POST --data-binary @demochannel_config.block http://127.0.0.1:7059/protolator/decode/common.Block > demochannel.json  
4. Extract current config   
apt install jq && jq .data.data[0].payload.data.config demochannel.json > demochannel_config.json  
5. generating new config   
jq -s '.[0] * {"channel_group":{"groups":{"Application":{"groups": {"Org2MSP":.[1]}}}}}' demochannel_config.json ./org2.json > demochannel_modified_config.json
6. Translate demochannel_config.json and demochannel_modified_config.json to proto  
curl -X POST --data-binary @demochannel_config.json http://127.0.0.1:7059/protolator/encode/common.Config > demochannel_config.pb  
curl -X POST --data-binary @demochannel_modified_config.json http://127.0.0.1:7059/protolator/encode/common.Config > demochannel_modified_config.pb  
7. Computing config update  
curl -X POST -F original=@demochannel_config.pb -F updated=@demochannel_modified_config.pb http://127.0.0.1:7059/configtxlator/compute/update-from-configs -F channel=demochannel > demochannel_config_update.pb
8. Decoding config update  
curl -X POST --data-binary @demochannel_config_update.pb http://127.0.0.1:7059/protolator/decode/common.ConfigUpdate > demochannel_config_update.json  
9. Generating config update envelope  
echo '{"payload":{"header":{"channel_header":{"channel_id":"demochannel", "type":2}},"data":{"config_update":'$(cat demochannel_config_update.json)'}}}' > demochannel_config_update_in_envelope.json
10. convert it into the proto  
curl -X POST --data-binary @demochannel_config_update_in_envelope.json http://127.0.0.1:7059/protolator/encode/common.Envelope > demochannel_config_update_in_envelope.pb
11. update (cd cli docker)    
peer channel signconfigtx -f ./demochannel_config_update_in_envelope.pb    
peer channel update -f ./demochannel_config_update_in_envelope.pb -o orderer.example.com:7050 -c demochannel    
12. org2 join demochannel   
1)启动org2的docker集群  docker-compose up  
2)
