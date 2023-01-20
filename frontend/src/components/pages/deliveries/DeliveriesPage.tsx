import * as React from 'react';
import {getRole} from "../../../storage/user";
import {ROLE_CUSTOMER, ROLE_DELIVERER, ROLE_DISPATCHER} from "../../../model/roles";
import DispatcherDeliveriesPage from "./DispatcherDeliveriesPage";
import CustomerDeliveriesPage from "./CustomerDeliveriesPage";
import DelivererDeliveriesPage from "./DelivererDeliveriesPage";

export default function DeliveriesPage() {
    const roles = getRole();
    return <>
        {roles === ROLE_CUSTOMER ? <CustomerDeliveriesPage/> : null}
        {roles === ROLE_DELIVERER ? <DelivererDeliveriesPage/> : null}
        {roles === ROLE_DISPATCHER ? <DispatcherDeliveriesPage/> : null}
    </>;
}