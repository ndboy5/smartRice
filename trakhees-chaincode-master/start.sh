#!/bin/bash
set -e

# start network and create channel with certificate authorities
cd ./test-network
./network.sh up createChannel -ca -s couchdb

# start publisher javascript chaincode
./network.sh deployCC -ccn publisher -ccl javascript

# start requester javascript chaincode
./network.sh deployCC -ccn requester -ccl javascript

cp organizations/peerOrganizations/org1.example.com/connection-org1.json ../
cp organizations/peerOrganizations/org2.example.com/connection-org2.json ../

echo "done"