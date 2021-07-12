const dotenv = require('dotenv');

const envFound = dotenv.config();
if (!envFound) {
  throw new Error(' Couldn\'t find .env file!');
}

module.exports = {
  port: parseInt(process.env.PORT, 10),
  api: {
    prefix: '/',
  },
  fabric: {
    admin: process.env.FABRIC_ADMIN,
    adminPassword: process.env.FABRIC_ADMIN_PASSWORD,
    user: process.env.FABRIC_USER,
    channel: 'mychannel',
  },
  serverUrl: process.env.SERVER_URL,
  contracts: {
    approver: 'approver',
    submitter: 'submitter',
    license: 'license',
  },
};
