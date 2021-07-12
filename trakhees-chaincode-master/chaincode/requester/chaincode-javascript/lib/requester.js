'use strict';

const Base = require('./base');
const Schema = require('./requester.json');
const updateSchema = require('./update.json');

class Requester extends Base {
    async initLedger(ctx) {
        // default implementation is do nothing
    }

    async create(ctx, id, data) {
         const asset = JSON.parse(data);
         asset.id = id;
         let currentTime = new Date();
         asset.createdAt = currentTime.toString();
         const buffer = Buffer.from(JSON.stringify(asset));
         await ctx.stub.putState(asset.id, buffer);
         return asset; 
    }

    async update(ctx) {
        // To be implemented
    }

    async findAll(ctx) {
        const allResults = [];
        // empty string for startKey and endKey does an open-ended query of all assets in the chaincode namespace.
        for await (const { key, value } of ctx.stub.getStateByRange("", "")) {
            const strValue = Buffer.from(value).toString('utf8');
            let record;
            try {
                record = JSON.parse(strValue);
            } catch (err) {
                console.log(err);
                record = strValue;
            }
            allResults.push({ Key: key, Record: record });
        }
        console.info(allResults);
        return JSON.stringify(allResults);
    }

    async findAllWithPagination(ctx) {
        const allResults = [];
        const args = super.parseArgs(ctx);
        const { pageSize, bookmark } = args;
        for await (const { key, value } of ctx.stub.getStateByRangeWithPagination("", "", pageSize, bookmark)) {
            const strValue = Buffer.from(value).toString('utf8');
            let record;
            try {
                record = JSON.parse(strValue);
            } catch (err) {
                console.log(err);
                record = strValue;
            }
            allResults.push({ Key: key, Record: record });
        }
        console.info(allResults);
        return JSON.stringify(allResults);
    }

    async findOne(ctx) {
        const { key } = super.parseArgs(ctx);
        if (!key) throw new Error('key is required');
        const assetJSON = await ctx.stub.getState(key);
        if (!assetJSON || assetJSON.length === 0) throw new Error(`The asset ${key} does not exist`);
        return assetJSON.toString();
    }

    async assetExists(ctx, args) {
        try {
            JSON.parse(args);
        } catch (error) {
            throw new Error('transaction argument must be a stringified object');
        }
        const { key } = JSON.parse(args);
        if (!key) throw new Error('key is required');
        const assetJSON = await ctx.stub.getState(key);
        if (!assetJSON || assetJSON.length === 0) return false;
        return true;
    }

    async query(ctx) {
        const { query } = super.parseArgs(ctx);
        if (!query) throw new Error('query is required');
        const allResults = await super.getQueryResult(ctx, query);
        return JSON.stringify(allResults);
    }

    async delete(ctx) {
        const { key } = super.parseArgs(ctx);
        if (!key) throw new Error('key is required');
        const assetJSON = await ctx.stub.getState(key);
        if (!assetJSON || assetJSON.length === 0) throw new Error(`The asset ${key} does not exist`);
        await ctx.stub.deleteState(key);
        return assetJSON.toString();
    }

}

module.exports = Requester;
