# Trakhees Server
This project includes Trakhees NodeJS server.

## perquisites
- nodejs v12
- [A running HyperLedger Fabric network](https://gitlab.com/Fagr/trakhes/trakhees-chaincode)

## Installation
- before starting the server, you should've the [trakhees-chaincode](https://gitlab.com/Fagr/trakhes/trakhees-chaincode) project up and running.
- clone this repo
```shell
git clone https://gitlab.com/Fagr/trakhes/trakhees-chaincode.git
```
- install dependencies
```shell
npm i
```
- copy the connection profile JSON files to `/fabric/secure/` -- *make sure you are at the root of the server*
```
cp /<path to trakhees-chaincode>/connection-org* ./fabric/secure/
```
- don't forget to create `.env` file with the required variables
- enroll admin and register user to interact with the Blockchain
```
npm run register:fabric
```

## Check for deployed contracts
- to check that everything is working as expected, the chaincodes are up and accessible:
```
npm run checkCC:fabric
```

## Start the server
- starting the server in development mode
```
npm run dev
```
- starting the server in normal mode
```
npm start
```

## Documentation
- [Postman collection](https://documenter.getpostman.com/view)

## Teardown
- Don't forget to delete user's wallet and connection profiles when tearing down the network
```
cd fabric/secure && rm wallet/*.id && rm connection-org*
```