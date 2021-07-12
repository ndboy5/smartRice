/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const publisher = require('./lib/publisher');

module.exports.Publisher = publisher;
module.exports.contracts = [publisher];
