#!/bin/bash
set -e

# start network and create channel with certificate authorities
cd ./test-network
./network.sh up createChannel -ca -s couchdb

# start user javascript chaincode
./network.sh deployCC -ccn user -ccl javascript

# start product javascript chaincode
./network.sh deployCC -ccn product -ccl javascript

# copy connection profile to project root
cp organizations/peerOrganizations/org1.example.com/connection-org1.json ../
cp organizations/peerOrganizations/org2.example.com/connection-org2.json ../

echo "done"