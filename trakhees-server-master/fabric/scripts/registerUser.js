const { Wallets } = require('fabric-network');
const FabricCAServices = require('fabric-ca-client');
const fs = require('fs');
const path = require('path');
const debug = require('debug')('app:fabric:registerUser');
const { fabric } = require('../../config');

const caUserRole = 'client';
const applicationUserId = fabric.user;
const AdminUserId = fabric.admin;
const walletPath = path.join(__dirname, '..', 'secure', 'wallet');

(async () => {
  try {
    // load the network configuration
    const ccpPath = path.resolve(__dirname, '..', 'secure', 'connection-org1.json');
    const fileExists = fs.existsSync(ccpPath);
    if (!fileExists) throw new Error(`no such file or directory: ${ccpPath}`);
    const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

    // Create a new CA client for interacting with the CA.
    const caURL = ccp.certificateAuthorities['ca.org1.example.com'].url;
    const ca = new FabricCAServices(caURL);

    // Create a new file system based wallet for managing identities.
    const wallet = await Wallets.newFileSystemWallet(walletPath);

    // Check to see if we've already enrolled the user.
    const userIdentity = await wallet.get(applicationUserId);
    if (userIdentity) {
      debug(`An identity for the user ${applicationUserId} already exists in the wallet`);
      process.exit(0);
    }

    // Check to see if we've already enrolled the admin user.
    const adminIdentity = await wallet.get(AdminUserId);
    if (!adminIdentity) {
      debug('An identity for the admin user does not exist in the wallet');
      debug('Run the enrollAdmin.js application before retrying');
      process.exit(1);
    }

    // build a user object for authenticating with the CA
    const provider = wallet.getProviderRegistry().getProvider(adminIdentity.type);
    const adminUser = await provider.getUserContext(adminIdentity, AdminUserId);

    // Register the user, enroll the user, and import the new identity into the wallet.
    // if affiliation is specified by client, the affiliation value must be configured in CA
    const secret = await ca.register({
      affiliation: 'org1.department1',
      enrollmentID: applicationUserId,
      role: caUserRole,
    }, adminUser);
    const enrollment = await ca.enroll({
      enrollmentID: applicationUserId,
      enrollmentSecret: secret,
    });
    const x509Identity = {
      credentials: {
        certificate: enrollment.certificate,
        privateKey: enrollment.key.toBytes(),
      },
      mspId: 'Org1MSP',
      type: 'X.509',
    };
    await wallet.put(applicationUserId, x509Identity);
    debug(`Successfully registered and enrolled user ${applicationUserId} and imported it into the wallet`);
    process.exit(0);
  } catch (error) {
    debug(`Failed to register user : ${error}`);
    process.exit(1);
  }
})();
