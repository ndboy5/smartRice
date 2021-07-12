exports.getLuckyNumber = async ({ query }) => {
  const { username } = query;

  return {
    data: {
      luckyNumber: Math.floor(Math.random() * (99999 - 1) + 1),
    },
    message: `Hello ${username}`,
  };
};
