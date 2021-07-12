/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const user = require('./lib/user');

module.exports.User = user;
module.exports.contracts = [user];
