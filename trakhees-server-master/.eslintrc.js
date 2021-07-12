module.exports = {
  env: {
    browser: true,
    commonjs: true,
    es6: true,
  },
  extends: 'airbnb-base',
  rules: {
    'no-unused-vars': 1,
    'object-curly-newline': ['error', {
      ExportDeclaration: {
        multiline: true,
        minProperties: 4,
      },
    }],
  },
};
