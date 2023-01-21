import React, {useState} from "react";
// @ts-ignore
import QrReader from "react-qr-scanner";
import Dialog from "@mui/material/Dialog";
import Button from "@mui/material/Button";
import DeliveryStatusPage from "./DeliveryStatusPage";

export default function DeliveryQRCodeScanner({open, handleClose}: { open: boolean, handleClose: () => void }) {
    const [result, setResult] = useState("");
    const [deliveryIDOpen, setDeliveryIDOpen] = useState<string | null>(null);

    function onResult(result: { text: string; }) {
        if (result) {
            const url = result.text ?? "";
            setResult(url);
            const match = url.match(/id=([^&]+)/);
            if (match) {
                setDeliveryIDOpen(match[1]);
            }
        }
    }

    return <>
        <Dialog open={open} onClose={handleClose}>
            <QrReader
                delay={300}
                onScan={onResult}
                onError={console.error}
                style={{width: '100%'}}
            />
            <p>
                <span>Last result:</span>
                <span>{result}</span>
            </p>
            <Button onClick={handleClose}>Close</Button>
        </Dialog>
        <DeliveryStatusPage id={deliveryIDOpen} handleClose={() => setDeliveryIDOpen(null)} />
    </>;
}
