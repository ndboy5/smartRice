exports.getLuckyNumberSchema = {
  type: 'object',
  required: [
    'query',
  ],
  properties: {
    query: {
      required: ['username'],
      properties: {
        username: {
          type: 'string',
          minLength: 2,
          maxLength: 30,
          errorMessage: 'username should be a string between 2 and 30 characters',
        },
      },
      additionalProperties: false,
      errorMessage: {
        required: {
          username: 'username is required',
        },
      },
    },
    params: {},
    body: {},
  },
  errorMessage: {
    required: {
      query: 'query object is required',
    },
  },
};
