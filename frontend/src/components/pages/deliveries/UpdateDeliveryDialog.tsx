import React, {useState} from "react";
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import Delivery from "../../../model/Delivery";
import DeliveryFields from "./DeliveryFields";
import {updateDelivery} from "../../../api/delivery/deliveries";

export default function UpdateDeliveryDialog({open, handleClose, onDeliveryUpdated, delivery}: {
    open: boolean,
    handleClose: () => void,
    onDeliveryUpdated: (delivery: Delivery) => void,
    delivery: Delivery
}) {
    const [changedDelivery, setChangedDelivery] = useState(delivery);

    async function updateDeliveryAndClose() {
        await updateDelivery(changedDelivery);
        onDeliveryUpdated(changedDelivery);
        handleClose();
    }

    return (
        open ?
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Update Delivery</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Enter new delivery details here:
                    </DialogContentText>
                    <DeliveryFields delivery={delivery} onDeliveryUpdate={setChangedDelivery}/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={updateDeliveryAndClose}>Update</Button>
                </DialogActions>
            </Dialog>
            : <></>
    );
}


