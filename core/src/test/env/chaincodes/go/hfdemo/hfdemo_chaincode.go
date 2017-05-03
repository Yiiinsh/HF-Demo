package hfdemo

import (
	"bytes"
	"encoding/json"
	"fmt"
	"strconv"
	"strings"
	"time"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

type TJUStudentInfoChainCode struct {
}

type Student struct {
	Id string `json:"id"`
	Name string `json:"name"`
	University string `json:"university"`
	Degree string `json:"degree"`
	Start time.Date `json:"start"`
	End time.Date `json:"end"`
	EducationQualifications []string `json:"educationQualifications"`
	InternInfos []interninfo `json:"interninfos"`
}

type Interninfo struct {
	StudentId string `json:"studentid"`
	Name string `json:"name"`
	WorkingId string `json:"workingid"`
	Company string `json:"company"`
	Department string `json:"department"`
	Start time.Date `json:"start"`
	End time.Date `json:"end"`
}

func main() {
	err := shim.Start(new(TJUStudentInfoChainCode))
	if err != nil {
		fmt.Printf("Error starting TJUStudentInfoChainCode: %s", err)
	}
}

// Init initializes chaincode
func (t *TJUStudentInfoChainCode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	return shim.Success(nil)
}

// Invoke - entry point for invocations
func (t *TJUStudentInfoChainCode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	fmt.Println("invoke is running:" + function)

	// function handler
	// TODO: batch operations
	if function == "insert"  {
		return t.insert(stub, args)
	} else if function == "insertBatch" {
		return shim.Error("Batch operations not supported now.")
	} else if function == "remove" {
		return t.remove(stub, args)
	} else if function == "removeBatch" {
		return shim.Error("Batch operations not supported now.")
	} else if function == "update" {
		return shim.update(stub, args)
	} else if function == "updateBatch" {
		return shim.Error("Batch operations not supported now.")
	}


	fmt.Println("invoke did not find func:" + function) //error
	return shim.Error("Received unknown function invocation")
}

func (t *TJUStudentInfoChainCode) insert(stub shim.ChaincodeStubInterface, args[] string) pb.Response {
	// insert by id
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments.Excepting 2 : id & content")
	}
	id := args[0]
	info := args[1]

	// check if already exists
	tryFetch, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Fail to get by " + id + " " + err.Error())
	} else if tryFetch != nil {
		fmt.Println("Id " + id + " already exists.Content:" + tryFetch)
		return shim.Error("Id " + id + " already exists.")
	}

	fmt.Println("insert id:" + id + " content:" + info)
	err = stub.PutState(id, info)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Println("- insert success")
	return shim.Success(nil)
}

func (t *TJUStudentInfoChainCode) remove(stub shim.ChaincodeStubInterface, args[] string) pb.Response {
	// remove by id
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments.Excepting 1.")
	}
	id := args[0]

	fmt.Println("remove id:" + id)
	err = stub.DelState(id)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Println("- remove success")
	return shim.Success(nil)
}

func (t *TJUStudentInfoChainCode) update(stub shim.ChaincodeStubInterface, args[] string) pb.Response {
	// update by id
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments.Excepting 2 : id & content")
	}
	id := args[0]
	info := args[1]

	// check if already exists
	tryFetch, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Fail to get by " + id + " " + err.Error())
	} else if tryFetch == nil {
		fmt.Println("Id " + id + " not exists.Content:" + tryFetch)
		return shim.Error("Id " + id + " not exists.")
	}

	fmt.Println("update id:" + id + " content:" + info)
	err = stub.PutState(id, info)
	if err != nil {
		return shim.Error(err.Error())
	}
	fmt.Println("- update success")
	return shim.Success(nil)
}


// Query - query of the chaincode
func (t *TJUStudentInfoChainCode) Query(stub shim.ChaincodeStubInterface) pb.Response {
	function, args := stub.GetFunctionAndParameters()
	fmt.Println("query is running:" + function)

	// function handler
	if function == "query" {
		return t.query(stub, args)
	} else if function == "queryBatch" {
		return t.queryBatch(stub, args)
	}

	fmt.Println("query did not find func:" + function) // error
	return shim.Error("Received unknown function query")
}

func (t *TJUStudentInfoChainCode) query(stub shim.ChaincodeStubInterface, args[] string) pb.Response {
	// query by id
	if len(args) < 1 {
		return shim.Error("Incorrect number of arguments.Excepting 1")
	}
	studentId := args[0]

	fmt.Printf("- query on:\n%s\n", studentId)
	res, err := stub.GetState(studentId)
	if err != nil {
		return shim.Error(err.Error())
	}
	if res == nil {
		return shim.Error("Fail to get state for " + studentId)
	}
	fmt.Printf("query responses:%s\n", string(res))

	return shim.Success(res)
}

func (t *TJUStudentInfoChainCode) queryBatch(stub shim.ChaincodeStubInterface, args[] string) pb.Response {
	// query by student id
	if len(args) != 2 {
		return shim.Error("Incorrect number of arguments.Expected 2(start & end)")
	}
	startId := args[0]
	endId := args[1]
	if startId > endId {
		return shim.Error("Incorrect start & end for " + startId + "&" + endId)
	}

	fmt.Printf("- query batch on: \n%s-%s\n", startId, endId)
	res, err := stub.GetStateByRange(startId, endId)
	if err != nil {
		return shim.Error(err.Error())
	}
	if res == nil {
		return shim.Error("Fail to get state from " + startId + " to " + endId)
	}
	fmt.Printf("query responses:%s\n", string(res))

	return shim.Success(res)
}

