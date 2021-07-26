'use strict'

const { Gateway, Wallets } = require('fabric-network');
const FabricCAServices = require('fabric-ca-client');
const path = require('path');
const { buildCAClient, registerAndEnrollUser, enrollAdmin } = require('../../test-application/javascript/CAUtil.js');
const { buildCCPOrg1, buildWallet } = require('../../test-application/javascript/AppUtil.js');
const channelName = 'mychannel';
const chaincodeName = 'basic';
const mspOrg1 = 'Org1MSP';
const walletPath = path.join(__dirname, 'wallet');
const org1UserId = 'appUser';

function prettyJSONString(inputString) {
	return JSON.stringify(JSON.parse(inputString), null, 2);
}

const express = require('express');
const { strict } = require('assert');
const { config } = require('process');
const app = express();

app.use(express.json());

const  PORT=5000;
const ccp = buildCCPOrg1();

//Gets all the rice assets in the current world state
app.get('/api/v1/rice', async (req, res)=>{
    try{
 		const wallet = await buildWallet(Wallets, walletPath);
        const gateway = new Gateway();
            
			await gateway.connect(ccp, {
				wallet,
				identity: org1UserId,
				discovery: { enabled: true, asLocalhost: true } // using asLocalhost as this gateway is using a fabric network deployed locally
			});

			// Build a network instance based on the channel where the smart contract is deployed
			const network = await gateway.getNetwork(channelName);

			// Get the contract from the network.
			const contract = network.getContract(chaincodeName);
    let result = await contract.evaluateTransaction('GetAllAssets');
	//filter assets to get only rice assets
	result = JSON.parse(result.toString()).filter(rice =>{
		rice.ID.substring(0,2)=="RCE";
	})
    res.status(200).json({success:true, data: result }); //Same cases may require that the JSON data be parsed twice
    }
    catch(error){
        console.log(`Get rice Contract Error: ${error}` )
    }
});

//Posts a new asset from client to the block chain
app.post('/api/v1/rice', async (req, res)=>{
    try{
 		const wallet = await buildWallet(Wallets, walletPath);

        const gateway = new Gateway();
            
			await gateway.connect(ccp, {
				wallet,
				identity: org1UserId,
				discovery: { enabled: true, asLocalhost: true } // using asLocalhost as this gateway is using a fabric network deployed locally
			});

			// Build a network instance based on the channel where the smart contract is deployed
			const network = await gateway.getNetwork(channelName);

    		const rice = req.body;
    		// const rice = JSON.parse(req.body);
			console.log(rice);
			// Get the contract from the network.
			const contract = network.getContract(chaincodeName);

			const result = await contract.submitTransaction('CreateAsset', ''+ rice.ID, ''+ rice.Source_ID, 
							''+ rice.Size, ''+ rice.Owner, ''+ rice.Rice_Type,''+ rice.Last_owner, ''+ rice.Creation_date, 
							''+ rice.Last_update_date, ''+ rice.Hist, ''+ rice.Batch_name,''+ rice.State,
							''+ rice.Status, ''+ rice.Farm_location); 
    res.status(200).json({success:true, msg: "submitted succesfully: " + JSON.parse(result.toString()) });
    }
    catch(error){
        console.log(`Get rice Contract Error: ${error}` )
    }
});
// Post new account to the blockchain
app.post('/api/v1/account', async (req, res)=>{
    try{
 		const wallet = await buildWallet(Wallets, walletPath);
        const gateway = new Gateway();
			await gateway.connect(ccp, {
				wallet,
				identity: org1UserId,
				discovery: { enabled: true, asLocalhost: true } // using asLocalhost as this gateway is using a fabric network deployed locally
			});

			// Build a network instance based on the channel where the smart contract is deployed
			const network = await gateway.getNetwork(channelName);
    		const account = req.body;
			// Get the contract from the network.
			const contract = network.getContract(chaincodeName);
			const result = await contract.submitTransaction('CreateAccount', ''+ account.ID, ''+ account.Name, 
							''+ account.Midname, ''+account.Surname, ''+ account.Role, ''+account.Pass_phrase,
							''+account.Location, ''+account.Creation_date); 
    res.status(200).json({success:true, msg: "submitted succesfully: " + result });
    }
    catch(error){
        console.log(`Get rice Contract Error: ${error}` )
    }
});

app.listen(PORT, console.log(`The app is listening on ${PORT}`))