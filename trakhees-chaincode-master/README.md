# Train-Blockchain HyperLedger Fabric

This project includes Train-Blockchain smart contract logic.
Running this project will start a test network with:

- one orderer
- one channel
- two organization with one peer per organization
- couchdb instance per peer
- CA-server for both peers and orderer
- separate chaincode instance per peer

With the following containers:

- orderer:
  - orderer: `orderer`
  - ca: `ca_orderer`
- organization `org1`:
  - peer: `peer0`
  - ca: `ca_org1`
  - couchdb: `couchdb0`
  - chaincode: `publisher`
  - chaincode: `requester`
- organization: `org2`
  - peer: `peer0`
  - ca: `ca_org2`
  - couchdb: `couchdb1`
  - chaincode: `organization`
  - chaincode: `publisher`
  - chaincode: `requester`

## perquisites

- curl
- docker
- docker compose
- nodejs v12

for more info please visit [Perquisites Documentation](https://hyperledger-fabric.readthedocs.io/en/release-2.2/prereqs.html)

## Installation

- clone this repo

```shell
git clone https://gitlab.com/Fagr/train-blockchain/train-chaincode
```

- change directory to the project root

```shell
cd train-chaincode/
```

- download platform-specific binaries and pull docker images

```shell
curl -sSL https://bit.ly/2ysbOFE | bash -s -- -s
```

## Start network and Deploy contracts

- make sure the `start.sh` script is executable

```shell
chmod +x start.sh
```

- start network and deploy contracts

**NOTE:** the start script starts around _14_ docker container so it may take some time starting.

```shell
./start.sh
```

- check the running docker containers

```shell
docker ps -a
```

## What to do next

if the project started successfully you should see two json files in the root directory:

- connection-org1.json
- connection-org2.json

the connection files can be used to interact with the network using `fabric-network` and `fabric-ca-client` npm modules.

for more info see: [Fabric NodeJS SDK](https://hyperledger.github.io/fabric-sdk-node/release-2.2/module-fabric-network.html)

## Teardown Network

to stop and remove all running containers

```shell
cd test-network
./network.sh down
```

if you started your network with a CA you may need to manually remove `msp` folders after tearing down the network.
you may notice the following errors after running the `./network down` script:

> rm: cannot remove 'organizations/fabric-ca/ordererOrg/msp/cacerts': Permission denied

run the following command to manually delete `msp` folders

```
cd test-network
sudo rm -rf organizations/fabric-ca/ordererOrg/
sudo rm -rf organizations/fabric-ca/org1
sudo rm -rf organizations/fabric-ca/org2
```

## Resources

- [Fabric Test Network](https://hyperledger-fabric.readthedocs.io/en/latest/test_network.html)
- [Chaincode Development](https://hyperledger-fabric.readthedocs.io/en/release-2.0/chaincode4ade.html)
- [Fabric NodeJS Contract API](https://www.npmjs.com/package/fabric-contract-api)
