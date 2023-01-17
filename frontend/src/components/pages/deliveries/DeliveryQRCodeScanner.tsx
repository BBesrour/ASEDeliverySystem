import React, {useState} from "react";
import {useZxing} from "react-zxing";
import Dialog from "@mui/material/Dialog";
import Button from "@mui/material/Button";
import DeliveryStatusPage from "./DeliveryStatusPage";

export default function DeliveryQRCodeScanner({open, handleClose}: { open: boolean, handleClose: () => void }) {
    const [result, setResult] = useState("");
    const [deliveryIDOpen, setDeliveryIDOpen] = useState<string | null>(null);

    const { ref } = useZxing({
        paused: !open,
        onResult(result) {
            const url = result.getText();
            setResult(url);
            const match = url.match(/id=([^&]+)/);
            if (match) {
                setDeliveryIDOpen(match[1]);
            }
        },
    });

    return <>
        <Dialog open={open} onClose={handleClose}>
            <video ref={ref} />
            <p>
                <span>Last result:</span>
                <span>{result}</span>
            </p>
            <Button onClick={handleClose}>Close</Button>
        </Dialog>
        <DeliveryStatusPage id={deliveryIDOpen} handleClose={() => setDeliveryIDOpen(null)} />
    </>;
}
