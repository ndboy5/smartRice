'use strict';

const Base = require('./base');
const Schema = require('./product.json');
const updateSchema = require('./update.json');

class Product extends Base {
    async initLedger(ctx) {
        // default implementation is do nothing
    }

    async create(ctx) {
        const args = super.parseArgs(ctx);
        const Key = super.generateKey(ctx, 10);
        const Record = super.validateSchema(Schema, args);

        // check if manufactory exist
        const { manufactory } = Record;
        const { payload } = await ctx.stub.invokeChaincode('user', ['getAssetIfExists',
            JSON.stringify({ key: manufactory }),
        ]);
        const found = JSON.parse(payload.toString('utf8'));
        if (found.length === 0 || found === false) {
            throw new Error(`user with id: ${manufactory} doesn't exist`);
        }
        console.log(found);
        if (found.userType !== 'manufactory') {
            throw new Error(`user with id: ${manufactory} is not of type "manufactory"`);
        }

        Record.createdAt = +`${ctx.stub.getTxTimestamp().seconds.toString()}000`;
        await ctx.stub.putState(Key, Buffer.from(JSON.stringify(Record), 'utf8'));
        return JSON.stringify({ Key, Record });
    }

    async update(ctx) {
        const { productId: Key, ...args } = super.validateSchema(updateSchema, super.parseArgs(ctx));

        // check if product exist
        const keyAsset = await ctx.stub.getState(Key);
        if (!keyAsset || keyAsset.length === 0) {
            throw new Error('no product found with this id');
        }
        const Record = JSON.parse(keyAsset.toString());

        Object.assign(Record, args);
        await ctx.stub.putState(Key, Buffer.from(JSON.stringify(Record), 'utf8'));
        return JSON.stringify({ Key, Record });
    }

    async findAll(ctx) {
        const iterator = await ctx.stub.getStateByRange("", "");
        const allResults = [];
        while (true) {
            const res = await iterator.next();

            if (res.value && res.value.value.toString()) {
                console.log(res.value.value.toString('utf8'));

                const Key = res.value.key;
                let Record;
                try {
                    Record = JSON.parse(res.value.value.toString('utf8'));
                } catch (err) {
                    console.log(err);
                    Record = res.value.value.toString('utf8');
                }
                allResults.push({ Key, Record });
            }
            if (res.done) {
                console.log('end of data');
                await iterator.close();
                console.info(allResults);
                return allResults;
            }
        }
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

    async getAssetIfExists(ctx, args) {
        try {
            JSON.parse(args);
        } catch (error) {
            throw new Error('transaction argument must be a stringified object');
        }
        const { key } = JSON.parse(args);
        if (!key) throw new Error('key is required');
        const assetJSON = await ctx.stub.getState(key);
        if (!assetJSON || assetJSON.length === 0) return false;
        return assetJSON.toString();
    }

    async query(ctx, args) {
        try {
            JSON.parse(args);
        } catch (error) {
            throw new Error('transaction argument must be a stringified object');
        }
        const { query } = JSON.parse(args);
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

module.exports = Product;
