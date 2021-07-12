import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/styles';
import { Avatar, Typography } from '@material-ui/core';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    minHeight: 'fit-content',
    padding: '5px'
  },
  avatar: {
    width: 50,
    height: 50
  },
  name: {
    marginTop: theme.spacing(1),
    padding: '15px'
  }
}));

const Profile = props => {
  const { className, ...rest } = props;

  const classes = useStyles();

  const user = {
    name: 'Shen Zhi',
    avatar: '/images/avatars/avatar_11.png',
  };

  return (
    <div
      {...rest}
      className={clsx(classes.root, className)}
    >
       <Typography
        className={classes.name}
        variant="h6"
      >
        {user.name}
      </Typography>
      <Avatar
        alt="Person"
        className={classes.avatar}
        component={RouterLink}
        src={user.avatar}
        to="/settings"
      />
    </div>
  );
};

Profile.propTypes = {
  className: PropTypes.string
};

export default Profile;
