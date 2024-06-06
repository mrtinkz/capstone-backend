import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/shopper">
        Shopper
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle">
        Vehicle
      </MenuItem>
      <MenuItem icon="asterisk" to="/dealer">
        Dealer
      </MenuItem>
      <MenuItem icon="asterisk" to="/scheduled-test-drive">
        Scheduled Test Drive
      </MenuItem>
      <MenuItem icon="asterisk" to="/scheduled-pickup">
        Scheduled Pickup
      </MenuItem>
      <MenuItem icon="asterisk" to="/post-purchase-activity">
        Post Purchase Activity
      </MenuItem>
      <MenuItem icon="asterisk" to="/document-blob">
        Document Blob
      </MenuItem>
      <MenuItem icon="asterisk" to="/order">
        Order
      </MenuItem>
      <MenuItem icon="asterisk" to="/financing">
        Financing
      </MenuItem>
      <MenuItem icon="asterisk" to="/pricing-summary">
        Pricing Summary
      </MenuItem>
      <MenuItem icon="asterisk" to="/vehicle-options">
        Vehicle Options
      </MenuItem>
      <MenuItem icon="asterisk" to="/shopping-cart">
        Shopping Cart
      </MenuItem>
      <MenuItem icon="asterisk" to="/payment-detail">
        Payment Detail
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
