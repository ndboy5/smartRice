/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const requester = require('./lib/requester');

module.exports.Requester = requester;
module.exports.contracts = [requester];
