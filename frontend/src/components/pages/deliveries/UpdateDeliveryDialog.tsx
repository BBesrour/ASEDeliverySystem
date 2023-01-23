import React from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Delivery from "../../../model/Delivery";
import {updateDelivery} from "../../../api/delivery/deliveries";
import DeliveryFields from "./DeliveryFields";

export default function UpdateDeliveryDialog({open, currentDelivery, handleClose, onDeliveryUpdated}: {
    open: boolean;
    currentDelivery: Delivery,
    handleClose: () => void;
    onDeliveryUpdated: (delivery: Delivery) => void;
}) {
    const [delivery, setDelivery] = React.useState(new Delivery(
        currentDelivery.id,
        currentDelivery.targetCustomerID,
        currentDelivery.targetBoxID,
        currentDelivery.delivererID,
        currentDelivery.status,
        currentDelivery.isActive,
    ));
    async function handleUpdateDelivery() {
        await updateDelivery(delivery);
        onDeliveryUpdated(delivery);
        handleClose();
    }

    return open ? (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Update User</DialogTitle>
            <DialogContentText>
                Enter new delivery details here:
            </DialogContentText>
            <DeliveryFields delivery={delivery} onDeliveryUpdate={setDelivery} />
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleUpdateDelivery}>Update</Button>
            </DialogActions>
        </Dialog>
    ) : (
        <></>
    );
}

