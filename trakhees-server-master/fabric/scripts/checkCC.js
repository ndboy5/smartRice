const { Wallets, Gateway } = require('fabric-network');
const fs = require('fs');
const path = require('path');
const debug = require('debug')('app:fabric:checkCC');
const { fabric, contracts } = require('../../config');

const applicationUserId = fabric.user;
const walletPath = path.join(__dirname, '..', 'secure', 'wallet');

(async () => {
  try {
    // load the network configuration
    const ccpPath = path.resolve(__dirname, '..', 'secure', 'connection-org1.json');
    const fileExists = fs.existsSync(ccpPath);
    if (!fileExists) throw new Error(`no such file or directory: ${ccpPath}`);
    const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

    // Create a new file system based wallet for managing identities.
    const wallet = await Wallets.newFileSystemWallet(walletPath);

    // Check to see if we've already enrolled the user.
    const userIdentity = await wallet.get(applicationUserId);
    if (!userIdentity) {
      debug(`An identity for the user ${applicationUserId} does not exists in the wallet`);
      process.exit(1);
    }

    // Create a new gateway for connecting to our peer node.
    const gateway = new Gateway();
    await gateway.connect(ccp, {
      wallet,
      identity: applicationUserId,
      discovery: { enabled: true, asLocalhost: true },
    });

    const network = await gateway.getNetwork(fabric.channel);
    Object.keys(contracts).map((cc) => {
      if (!network.getContract(cc)) throw new Error(`chaincode:${cc} not found`);
      return null;
    });
    debug('Chaincodes are up and running.');
    process.exit(0);
  } catch (error) {
    debug(`Failed to register user : ${error}`);
    process.exit(1);
  }
})();
