import React from 'react';
// import { Link as RouterLink } from 'react-router-dom';
import clsx from 'clsx';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/styles';
import { Typography } from '@material-ui/core';

const useStyles = makeStyles(theme => ({
  root: {
    display: 'flex',
    flexDirection: 'row',
    alignItems: 'center',
    minHeight: 'fit-content'
  },
  avatar: {
    width: 70,
    height: 70,
  },
  typography: {
    display: 'flex',
    flexDirection: 'column',
    padding: '1px',
    marginLeft: '5px',
    minHeight: 'fit-content'
  },
  name: {
    marginTop: theme.spacing(1)
  }
}));

const Logo = props => {
  const { className, ...rest } = props;

  const classes = useStyles();

  const logo = {
    img: '/images/logos/supplychain.png',
    name: 'Supply Chain',
    bio: 'block chain '
  };

  return (
    <div
      {...rest}
      className={clsx(classes.root, className)}
    >
      <img
        size="large"
        alt="Logo"
        className={classes.avatar}
        // component={RouterLink}
        src={logo.img}
        to="/settings"
      />
       <div className={clsx(classes.typography, className)}>
        <Typography
          className={classes.name}
          variant="h5"
        >
          {logo.name}
        </Typography>
        <Typography variant="body2">{logo.bio}</Typography>
      </div>    
    </div>
  );
};

Logo.propTypes = {
  className: PropTypes.string
};

export default Logo;
