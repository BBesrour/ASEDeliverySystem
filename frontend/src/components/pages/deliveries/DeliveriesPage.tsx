import * as React from 'react';
import {getRoles} from "../../../storage/user";
import {ROLE_CUSTOMER, ROLE_DELIVERER, ROLE_DISPATCHER} from "../../../model/roles";
import DispatcherDeliveriesPage from "./DispatcherDeliveriesPage";
import CustomerDeliveriesPage from "./CustomerDeliveriesPage";
import DelivererDeliveriesPage from "./DelivererDeliveriesPage";

export default function DeliveriesPage() {
    const roles = getRoles();
    return <>
        {roles.includes(ROLE_CUSTOMER) ? <CustomerDeliveriesPage /> : null}
        {roles.includes(ROLE_DELIVERER) ? <DelivererDeliveriesPage /> : null}
        {roles.includes(ROLE_DISPATCHER) ? <DispatcherDeliveriesPage /> : null}
    </>;
}