const { Gateway, Wallets } = require('fabric-network');
const _ = require('lodash');
const path = require('path');
const fs = require('fs');
const { fabric } = require('../config');

const applicationUserId = fabric.user;
const walletPath = path.join(__dirname, 'secure', 'wallet');

async function getChaincode(chaincode) {
  // load the network configuration
  const ccpPath = path.resolve(__dirname, 'secure', 'connection-org1.json');
  const fileExists = fs.existsSync(ccpPath);
  if (!fileExists) throw new Error(`no such file or directory: ${ccpPath}`);
  const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

  const wallet = await Wallets.newFileSystemWallet(walletPath);

  const userIdentity = await wallet.get(applicationUserId);
  if (!userIdentity) throw new Error(`no identity found for user:${applicationUserId}`);

  // Create a new gateway for connecting to our peer node.
  const gateway = new Gateway();
  await gateway.connect(ccp, {
    wallet,
    identity: applicationUserId,
    discovery: { enabled: true, asLocalhost: true },
  });

  const network = await gateway.getNetwork(fabric.channel);
  const contract = network.getContract(chaincode);
  if (!contract) throw new Error(`chaincode:${chaincode} not found`);
  return contract;
}

exports.submitTx = async ({ chaincode, txName, args }) => {
  const contract = await getChaincode(chaincode);
  try {
    const result = await contract.submitTransaction(txName, JSON.stringify(args));
    return { result: JSON.parse(result) };
  } catch (err) {
    const msg = _.get(err, ['responses', '0', 'response', 'message'], 'transaction failed');
    return { err: msg };
  }
};

exports.evaluateTx = async ({ chaincode, txName, args }) => {
  const contract = await getChaincode(chaincode);
  try {
    const result = await contract.evaluateTransaction(txName, JSON.stringify(args));
    return { result: JSON.parse(result) };
  } catch (err) {
    const msg = _.get(err, ['responses', '0', 'response', 'message'], 'transaction failed');
    return { err: msg };
  }
};

exports.query = async ({ chaincode, selector }) => {
  const contract = await getChaincode(chaincode);
  const result = await contract.evaluateTransaction('query', JSON.stringify({
    // eslint-disable-next-line object-shorthand
    query: JSON.stringify({ selector: selector }),
  }));
  return JSON.parse(result);
};
