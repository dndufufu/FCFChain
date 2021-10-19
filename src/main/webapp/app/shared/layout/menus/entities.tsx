import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/transaction-output">
      <Translate contentKey="global.menu.entities.transactionOutput" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/transaction-input">
      <Translate contentKey="global.menu.entities.transactionInput" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/block">
      <Translate contentKey="global.menu.entities.block" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/transaction">
      <Translate contentKey="global.menu.entities.transaction" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/wallet">
      <Translate contentKey="global.menu.entities.wallet" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
