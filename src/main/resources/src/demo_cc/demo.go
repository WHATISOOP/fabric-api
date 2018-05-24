/*
Copyright IBM Corp 2016 All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package main

import (
	"encoding/json"
	"fmt"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type myChaincode struct {
}


type Organization struct {
	LoginName         	string `json:"loginName"`
	OrganunitName 		string `json:"organunitName"`
	OrganunitCode       string `json:"organunitCode"`
	Province			string `json:"province"`
	City				string `json:"city"`
}



// ============================================================================================================================
// Main
// ============================================================================================================================
func main() {
	err := shim.Start(new(myChaincode))
	if err != nil {
		fmt.Printf("Error starting myChaincode chaincode: %s", err)
	}
}

// Init resets all the things
func (t *myChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

// Invoke is our entry point to invoke a chaincode function
func (t *myChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	switch function {

	case "addOrganization":
		return t.addOrganization(stub, args)
	case "queryByOrganunitCode":
		return t.queryByOrganunitCode(stub, args)

	default:
		return shim.Error("Unsupported operation")
	}
}

func (t *myChaincode) addOrganization(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	fmt.Println("===addOrganization===")
	if len(args) < 1 {
		return shim.Error("addOrganization operation must have 1 arg")
	}
	// get the args
	bOrganization := []byte(args[0])
	organization := &Organization{}
	err := json.Unmarshal(bOrganization, &organization)
	if err != nil {
		fmt.Println(err)
		return shim.Error("Unmarshal failed")
	}

	//save the json info
	err = stub.PutState(organization.OrganunitCode, bOrganization)
	if err != nil {
		return shim.Error("putting state err: " + err.Error())
	}
	return shim.Success(nil)
}

func (t *myChaincode) queryByOrganunitCode(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) < 1 {
		return shim.Error("queryByOrganunitCode operation must have 1 ars")
	}
	code := args[0]
	fmt.Println("===queryByOrganunitCode===", code)
 	val, err := stub.GetState(code)
	if err != nil {
		return shim.Error("queryByOrganunitCode operation failed while getting the state : " + err.Error())
	}

	return shim.Success(val)

}