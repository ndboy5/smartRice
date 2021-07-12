import React from 'react';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import { makeStyles } from '@material-ui/styles';
import { Hidden, IconButton } from '@material-ui/core';
import MenuIcon from '@material-ui/icons/Menu';
import InputIcon from '@material-ui/icons/Input';
import { SearchInput, Profile } from 'components';

const useStyles = makeStyles(theme => ({
  root: {},
  row: {
    height: '5px',
    display: 'flex',
    alignItems: 'center',
    marginLeft: theme.spacing(10)
  },
  spacer: {
    flexGrow: 1
  },
  searchInput: {
    marginRight: theme.spacing(1)
  },
  profile: {
    marginLeft: theme.spacing(8),
    padding: '10px'
  },
  signOutButton: {
    marginLeft: theme.spacing(1)
  }
}));

const Topbar = props => {
  const { className, onSidebarOpen, ...rest } = props;

  const classes = useStyles();

  return (
    <div
      {...rest}
      className={clsx(classes.root, className)}
    >
      <div className={classes.row}>
        <SearchInput
          className={classes.searchInput}
          placeholder="Search user"
        />
         <Profile 
          className={classes.profile} />
        
        <Hidden mdDown>
          <IconButton
            className={classes.signOutButton}
            color="primary">
            <InputIcon />
          </IconButton>
        </Hidden>
  
        <Hidden lgUp>
          <IconButton
            color="primary"
            onClick={onSidebarOpen}
          >
            <MenuIcon />
          </IconButton>
        </Hidden>
      </div>
    </div>
  );
};

Topbar.propTypes = {
  className: PropTypes.string
};

export default Topbar;
