/**
 * The Asset Transfer Class 
 * Purpose: Uses the fabric-contract-api to execute Account and Rice Asset transactions
 * Author: Ifeanyichukwu Deborah Okengwu
 * Date: July 15th 2021
 */
'use strict';

const { Contract } = require('fabric-contract-api');

class AssetTransfer extends Contract {
//Sample rice data for code testing
    async InitLedger(ctx) {
        const assets = [
            {
            ID: 'RCE1',
            Source_ID:'-',
            Size: 5,
            Owner: 'FMR09000000000',
            Rice_Type: "Ofada",
            Last_owner: "",
            Creation_date: 78998977,
            Last_update_date:78998977,
            Hist: '', 
            Batch_name: 'Batch A',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 'Effurun Farms, Okwokoko, Delta State',
            Transaction_Status:'open',
            Unit_Price: '100'
            },
            {ID: 'FMR09000000000', Name:'Frank', Midname:'', Surname: 'Olize', Role: 'FMR', 
            Pass_phrase: 'SchoolGarri', Location:14323423, Creation_date: 23142441 },
            {
                ID: 'RCE2',
                Size: 5,
            Owner: 'FMR09000000000',
            Last_owner: "Tochukwu Okengwu",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 78998977,
            Last_update_date:34253543,
            Hist: '', 
            Batch_name: 'Batch B',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 'Isingwu Farms, Osa, Umuahia, Nigeria',
            Transaction_Status:'open',
            Unit_Price: '100'
            },
            {
                ID: 'RCE3',
                Size: 10,
            Owner: 'FMR09000000000',
            Last_owner: "Jerry Gana",
             Rice_Type: "Long Grain",
            Source_ID:'-',
            Creation_date: 78998977,
            Last_update_date:37256773,
            Hist: '', 
            Batch_name: 'Batch B',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 'Isingwu Farms, Osa, Umuahia, Nigeria',
            Transaction_Status:'open',
            Unit_Price: '150'
            },
            {
                ID: 'RCE4',
                Size: 10,
            Owner: 'FMR09000000000',
            Last_owner: "Garba Babayaro",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 78998977,
            Last_update_date:324655656,
            Hist: '', 
            Batch_name: 'Batch C',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 'Sule Farms, Babagana Village, Iwela, Abuja',
            Transaction_Status:'open',
            Unit_Price: ''
            },
            {
                ID: 'RCE5',
                Size: 15,
            Owner: 'FMR09000000000',
            Last_owner: "Tochukwu Okengwu",
             Rice_Type: "Brown Rice",
            Source_ID:'-',
            Creation_date: 68998977,
            Last_update_date:34253485,
            Hist: '', 
            Batch_name: 'Batch D',
            State: 'harvested',
            Status:'Fresh', //Expired, fresh ,Good etc
            Farm_location: 'Sule Farms, Babagana Village, Iwela, Abuja',
            Transaction_Status:'open',
            Unit_Price: '120'
            },
            {
                ID: 'RCE6',
                Size: 15,
            Owner: 'FMR09000000000',
            Last_owner: "Ariochi Okengwu",
             Rice_Type: "Ofada",
            Source_ID:'-',
            Creation_date: 38995977,
            Last_update_date:38995977,
            Hist: '', 
            Batch_name: 'Batch A',
            State: 'processed',
            Status:'Expired', //Expired, fresh ,Good etc
            Farm_location: 'Area 8,Garban Keffi, Mando Road, Kaduna',
            Transaction_Status:'closed',
            Unit_Price: '100'
            },
        ];

        for (const asset of assets) {
            asset.docType = 'asset';
            await ctx.stub.putState(asset.ID, Buffer.from(JSON.stringify(asset)));
            console.info(`Asset ${asset.ID} initialized`);
        }
    }

    // CreateAsset issues a new rice asset to the world state with given details of the harvested crop
    async CreateAsset(ctx, id, source_id, size, owner, rice_type, lastOwner, creation_date, transaction_date, 
                        hist, state, status,batch_name, farm_location, trans_status, unit_price) {
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
            State: state, //Harvested, Processing, Storage, Consumed etc
            Status:status, //Expired, fresh ,Good etc
            Farm_location: farm_location,
            Transaction_Status: trans_status, //The possible transaction statuses are "open", "pending", "closed"
            Unit_Price: unit_price
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

    /**
     * Asset Trading transactions - Consumption, Trading
     * UpdateAsset updates an existing rice asset in the world state with provided parameters.
     * */
    async UpdateAsset(ctx, id, source_id, size, owner, rice_type, lastOwner, creation_date, transaction_date, 
                        hist, state, status,batch_name, farm_location, trans_status, unit_price ) {
        const exists = await this.AssetExists(ctx, id);
        if (!exists) {
            throw new Error(`The asset ${id} does not exist`);
        }
        // overwriting original asset with new asset
        const updatedAsset = {
            ID: id,Source_ID: source_id, Size: size, Owner: owner,
            Rice_Type: rice_type, Last_owner: lastOwner,Creation_date: creation_date,
            Last_update_date:transaction_date,Hist: hist, Batch_name: batch_name,
            State: state, Status:status, Farm_location: farm_location, Transaction_Status: trans_status, 
            Unit_Price: unit_price
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
    async TransferAsset(ctx, id, newOwner, lastOwner, updateDate) {
        const assetString = await this.ReadAsset(ctx, id);
        const asset = JSON.parse(assetString);
        asset.Owner = newOwner;
        asset.Last_owner = lastOwner;
        asset.Last_update_date = updateDate;
        return ctx.stub.putState(id, Buffer.from(JSON.stringify(asset)));
    }

    // To change Rice asset transactions status for release, sale or buy
    async AssetTradeTransactionUpdate(ctx, id, trans_status, updateDate) {
        const assetString = await this.ReadAsset(ctx, id);
        const asset = JSON.parse(assetString);
        asset.Transaction_Status = trans_status;
        asset.Last_update_date = updateDate;
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

    //Get the history of changes to an asset
    async GetAssetHistory(ctx, id) {
        const allResults = [];
        // TODO: Test efficacy of this function
      //  const iterator = await ctx.stub.GetAssetHistory(id);
        const iterator = await ctx.stub.getHistoryForKey(id);
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
