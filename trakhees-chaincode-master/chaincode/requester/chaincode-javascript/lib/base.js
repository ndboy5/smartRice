'use strict';

const { Contract } = require('fabric-contract-api');
const Ajv = require('ajv');
const ajv = new Ajv({ useDefaults: true, allErrors: true, jsonPointers: true });
require('ajv-errors')(ajv, { singleError: true });

class BaseContract extends Contract {

    generateKey(ctx) {
        return ctx.stub.getTxTimestamp().seconds.toString(18);
    }

    parseArgs(ctx) {
        let args = ctx.stub.getStringArgs();
        if (args.length !== 2) throw new Error('transaction accepts two arguments');
        let argumentsObject;
        try {
            argumentsObject = JSON.parse(args[1]);
        } catch (error) {
            throw new Error('transaction argument must be a stringified object');
        }
        return argumentsObject;
    }

    transactionMetaData(ctx) {
        return {
            txId: ctx.stub.getTxID(),
            creator: ctx.stub.getCreator(),
            timestamp: ctx.stub.getTxTimestamp().seconds.toString(),
        }
    }

    validateSchema(schema, Record) {
        let valid = ajv.validate(schema, Record);
        if (!valid) {
            throw new Error(ajv.errorsText());
        }
        return Record;
    }

    async getQueryResult(ctx, query) {
        const allResults = [];
        for await (const { key, value } of ctx.stub.getQueryResult(query)) {
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
        return allResults;
    }

}

module.exports = BaseContract;
