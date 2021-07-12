const {
  getLuckyNumber,
} = require('../services/default');

const { controller } = require('../middleware/controller');

module.exports = {
  getLuckyNumber: controller(getLuckyNumber),
};
