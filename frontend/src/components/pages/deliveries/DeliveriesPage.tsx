import * as React from 'react';
import {getRole} from "../../../storage/user";
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
    const roles = getRole();
    return <>
        {roles === ROLE_CUSTOMER ? <CustomerDeliveriesPage /> : null}
        {roles === ROLE_DELIVERER ? <DelivererDeliveriesPage /> : null}
        {roles === ROLE_DISPATCHER ? <DispatcherDeliveriesPage /> : null}
        <Outlet />
        <Button onClick={() => setQrCodeScannerOpen(true)}>Scan QR code</Button>
        <DeliveryQRCodeScanner open={qrCodeScannerOpen} handleClose={() => setQrCodeScannerOpen(false)} />
    </>;
}