import Dialog from "@mui/material/Dialog";
import React, {useEffect} from "react";

import createBoxQR from "../../../qr/createBoxQR";
import DialogTitle from "@mui/material/DialogTitle";
import DialogContent from "@mui/material/DialogContent";
import Button from "@mui/material/Button";


export default function QRCodeDialog({
                                         open = false,
                                         handleClose,
                                         deliveryId
                                     }: { open?: boolean, handleClose: () => void, deliveryId: string }) {
    const [qrCode, setQrCode] = React.useState<string | null>(null);
    const [qrUrl, setQrUrl] = React.useState<string | null>(null);
    useEffect(() => {
        if (open) {
            const url = `${document.location.origin}/deliveries/change_status?id=${encodeURIComponent(deliveryId)}`;
            setQrUrl(url);
            createBoxQR(url).then((qrCode) => {
                setQrCode(qrCode);
            });
        }
    }, [open, deliveryId]);

    function printQRCode() {
        // https://stackoverflow.com/a/58142392/4306257
        // @ts-ignore
        const win: Window = window.open('about:blank', "_new");
        win.document.open();
        win.document.write([
            '<html>',
            '   <head>',
            '   </head>',
            '   <body onload="window.print()" onafterprint="window.close()">',
            '       <img src="' + qrCode + '"/><br />',
            '       <a href="' + qrUrl + '">' + qrUrl + '</a>',
            '   </body>',
            '</html>'
        ].join(''));
        win.document.close();
    }

    return (
        <Dialog open={open}>
            <DialogTitle>QR Code for Delivery {deliveryId}</DialogTitle>
            <DialogContent>
                <img src={qrCode ?? ""} alt="QR Code"/><br />
                <a href={qrUrl ?? ""} target="_blank" rel="noreferrer">{qrUrl ?? ""}</a><br />
                <Button onClick={printQRCode}>Print</Button>
                <Button onClick={handleClose}>Close</Button>
            </DialogContent>
        </Dialog>
    );
}