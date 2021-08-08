
# Note that the npm install always returns an error as shown below:

> protobufjs@6.11.2 postinstall /root/fabric/fabric-samples/asset-transfer-basic/application-javascript/node_modules/protobufjs
> node scripts/postinstall

internal/modules/cjs/loader.js:584
    throw err;


# The only way to clear the error is to run the following command in that folder and the execute npm install once again
sudo apt install build-essential

# rm -R node_modules
# rm package-lock.json

npm install --unsafe-perm
npm install


See https://github.com/protobufjs/protobuf.js/issues/877

# sudo apt install build-essential

#---------------------------------------------------
# Trakees-server-master
#---------------------------------------------------
# follow the instructions in the trakees server master readMe file, you may ignore the error on running the command " npm run checkCC:fabric"

# ------------------------------------------------
#installation Instruction for the Supplychain client dashboard - trakees
#-------------------------------------------------
# navigate to the root folder 
cd /root/fabric/supplychain-dashboard-master
npm install --unsafe-perm
# From the supplychain root folder try to install dependencies using 'npm install' It will generate an error for eslint
npm install
# to address the eslint error, edit the package.json file to remove all the eslint packages
vi package.json
# delete the package-lock.json file and the node_modules folder
rm -R node_modules
rm package-lock.json
# install the dependencies again
npm install
# run the application 
npm run start


