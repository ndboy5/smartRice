
'use strict';

const { Contract } = require('fabric-contract-api');

class AssetTransfer extends Contract {

    async InitLedger(ctx) {
        const assets = [
            {
            ID: 'RCE1',
            Source_ID:'-',
            Size: 5,
            Owner: 'Tochukwu Okengwu',
            Rice_Type: "Ofada",
            Last_owner: "",
            Creation_date: 78998977,
            Last_update_date:78998977,
            Hist: '', 
            Batch_name: 'Batch A',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 9999898,
            },
            {
                ID: 'RCE2',
                Size: 5,
                Owner: 'Garba Sule',
            Last_owner: "Tochukwu Okengwu",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 78998977,
            Last_update_date:34253543,
            Hist: '', 
            Batch_name: 'Batch B',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 9999898,
            },
            {
                ID: 'RCE3',
                Size: 10,
                Owner: 'Garba Sule',
            Last_owner: "Jerry Gana",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 78998977,
            Last_update_date:37256773,
            Hist: '', 
            Batch_name: 'Batch B',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 9999898,
            },
            {
                ID: 'RCE4',
                Size: 10,
                Owner: 'Max',
            Last_owner: "Garba Babayaro",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 78998977,
            Last_update_date:324655656,
            Hist: '', 
            Batch_name: 'Batch C',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 9999898,
            },
            {
                ID: 'RCE5',
                Size: 15,
                Owner: 'Adriana',
            Last_owner: "Tochukwu Okengwu",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 68998977,
            Last_update_date:34253485,
            Hist: '', 
            Batch_name: 'Batch D',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 9999898,
            },
            {
                ID: 'RCE6',
                Size: 15,
                Owner: 'Tunde',
            Last_owner: "Ariochi Okengwu",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 38995977,
            Last_update_date:38995977,
            Hist: '', 
            Batch_name: 'Batch A',
            State: 'processed',
            Status:'Expired', //Expired, fresh ,Good etc
            Farm_location: 9999898,
            },
        ];

        for (const asset of assets) {
            asset.docType = 'asset';
            await ctx.stub.putState(asset.ID, Buffer.from(JSON.stringify(asset)));
            console.info(`Asset ${asset.ID} initialized`);
        }
    }

    // CreateAsset issues a new asset to the world state with given details.
    async CreateAsset(ctx, id, source_id, size, owner, rice_type, lastOwner, creation_date, transaction_date, 
                        hist, state, status,batch_name, farm_location) {
        const asset = {
            ID: id,
            Source_ID: source_id,
            Size: size, //The size of rice in kilogrames
            Owner: owner,
            Rice_Type: rice_type,
            Last_owner: lastOwner,
            Creation_date: creation_date,
            Last_update_date:transaction_date,
            Hist: hist, 
            Batch_name: batch_name,
            State: state, //Harvested, Processing, Consumed etc
            Status:status, //Expired, fresh ,Good etc
            Farm_location: farm_location,
        };
        ctx.stub.putState(id, Buffer.from(JSON.stringify(asset)));
        return JSON.stringify(asset);
    }

    // Create new Account 
    async CreateAccount(ctx, id,name, midname, surname, role,pass_phrase, location, creation_date) {
        const asset = {
            ID: id, Name:name, Midname:midname, Surname: surname, Role: role, 
            Pass_phrase: pass_phrase, Location:location, Creation_date: creation_date };
        ctx.stub.putState(id, Buffer.from(JSON.stringify(asset)));
        return JSON.stringify(asset);
    }

    // ReadAsset returns the asset stored in the world state with given id.
    async ReadAsset(ctx, id) {
        const assetJSON = await ctx.stub.getState(id); // get the asset from chaincode state
        if (!assetJSON || assetJSON.length === 0) {
            throw new Error(`The asset ${id} does not exist`);
        }
        return assetJSON.toString();
    }

    // UpdateAsset updates an existing rice asset in the world state with provided parameters.
    async UpdateAsset(ctx, id, size, owner, lastOwner, transaction_date, hist , state) {
        const exists = await this.AssetExists(ctx, id);
        if (!exists) {
            throw new Error(`The asset ${id} does not exist`);
        }
        // overwriting original asset with new asset
        const updatedAsset = {
            ID: id,
            Size: size,
            Owner: owner,
            Last_owner: lastOwner,
            Last_update_date:transaction_date,
            Hist: hist, 
            State: state,

        };
        return ctx.stub.putState(id, Buffer.from(JSON.stringify(updatedAsset)));
    }

    // DeleteAsset deletes an given asset from the world state.
    async DeleteAsset(ctx, id) {
        const exists = await this.AssetExists(ctx, id);
        if (!exists) {
            throw new Error(`The asset ${id} does not exist`);
        }
        return ctx.stub.deleteState(id);
    }

    // AssetExists returns true when asset with given ID exists in world state.
    async AssetExists(ctx, id) {
        const assetJSON = await ctx.stub.getState(id);
        return assetJSON && assetJSON.length > 0;
    }

    // TransferAsset updates the owner field of asset with given id in the world state.
    async TransferAsset(ctx, id, newOwner) {
        const assetString = await this.ReadAsset(ctx, id);
        const asset = JSON.parse(assetString);
        asset.Owner = newOwner;
        return ctx.stub.putState(id, Buffer.from(JSON.stringify(asset)));
    }

    // GetAllAssets returns all assets found in the world state of the hyper ledger.
    //Indexes on the ID are used to identify the type of Asset it belongs to
    async GetAllAssets(ctx) {
        const allResults = [];
        // range query with empty string for startKey and endKey does an open-ended query of all assets in the chaincode namespace.
        const iterator = await ctx.stub.getStateByRange('', '');
        let result = await iterator.next();
        while (!result.done) {
            const strValue = Buffer.from(result.value.value.toString()).toString('utf8');
            let record;
            try {
                record = JSON.parse(strValue);
            } catch (err) {
                console.log(err);
                record = strValue;
            }
            allResults.push({ Key: result.value.key, Record: record });
            result = await iterator.next();
        }
        return JSON.stringify(allResults);
    }


}

module.exports = AssetTransfer;
