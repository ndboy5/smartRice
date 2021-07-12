const router = require('express-promise-router')();

const { validate } = require('../middleware/validator');

const { getLuckyNumberSchema } = require('../requestSchema/default/getLuckyNumber');

const {
  getLuckyNumber,
} = require('../controllers/Default');

router.get('/', validate(getLuckyNumberSchema), getLuckyNumber);

exports.defaultRouter = router;
