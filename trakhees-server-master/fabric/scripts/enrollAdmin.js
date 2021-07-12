const FabricCAServices = require('fabric-ca-client');
const { Wallets } = require('fabric-network');
const fs = require('fs');
const path = require('path');
const debug = require('debug')('app:fabric:enrollAdmin');
const { fabric } = require('../../config');

const adminUserId = fabric.admin;
const adminUserPassword = fabric.adminPassword;
const walletPath = path.join(__dirname, '..', 'secure', 'wallet');

(async () => {
  try {
    // load the network configuration
    const ccpPath = path.resolve(__dirname, '..', 'secure', 'connection-org1.json');
    const fileExists = fs.existsSync(ccpPath);
    if (!fileExists) throw new Error(`no such file or directory: ${ccpPath}`);
    const ccp = JSON.parse(fs.readFileSync(ccpPath, 'utf8'));

    // Create a new CA client for interacting with the CA.
    const caInfo = ccp.certificateAuthorities['ca.org1.example.com'];
    const tlsCACerts = caInfo.tlsCACerts.pem;
    const ca = new FabricCAServices(
      caInfo.url,
      { trustedRoots: tlsCACerts, verify: false },
      caInfo.caName,
    );

    // Create a new  wallet : Note that wallet can manage identities.
    const wallet = await Wallets.newFileSystemWallet(walletPath);

    // Check to see if we've already enrolled the admin user.
    const identity = await wallet.get(adminUserId);
    if (identity) {
      debug('An identity for the admin user already exists in the wallet');
      process.exit(0);
    }

    // Enroll the admin user, and import the new identity into the wallet.
    const enrollment = await ca.enroll({
      enrollmentID: adminUserId,
      enrollmentSecret: adminUserPassword,
    });
    const x509Identity = {
      credentials: {
        certificate: enrollment.certificate,
        privateKey: enrollment.key.toBytes(),
      },
      mspId: 'Org1MSP',
      type: 'X.509',
    };
    await wallet.put(adminUserId, x509Identity);
    debug('Successfully enrolled admin user and imported it into the wallet');
    process.exit(0);
  } catch (error) {
    debug(`Failed to enroll admin user : ${error}`);
    process.exit(1);
  }
})();
