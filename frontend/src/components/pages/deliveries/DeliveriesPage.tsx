import * as React from 'react';
import {getRoles} from "../../../storage/user";
import {ROLE_CUSTOMER, ROLE_DELIVERER, ROLE_DISPATCHER} from "../../../model/roles";
import DispatcherDeliveriesPage from "./DispatcherDeliveriesPage";
import CustomerDeliveriesPage from "./CustomerDeliveriesPage";
import DelivererDeliveriesPage from "./DelivererDeliveriesPage";
import {Outlet} from "react-router-dom";
import {useState} from "react";
import Button from "@mui/material/Button";
import DeliveryQRCodeScanner from "./DeliveryQRCodeScanner";

export default function DeliveriesPage() {
    const [qrCodeScannerOpen, setQrCodeScannerOpen] = useState(false);
    const roles = getRoles();
    return <>
        {roles.includes(ROLE_CUSTOMER) ? <CustomerDeliveriesPage /> : null}
        {roles.includes(ROLE_DELIVERER) ? <DelivererDeliveriesPage /> : null}
        {roles.includes(ROLE_DISPATCHER) ? <DispatcherDeliveriesPage /> : null}
        <Outlet />
        <Button onClick={() => setQrCodeScannerOpen(true)}>Scan QR code</Button>
        <DeliveryQRCodeScanner open={qrCodeScannerOpen} handleClose={() => setQrCodeScannerOpen(false)} />
    </>;
}