import React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Delivery from "../../../model/Delivery";
import {updateDelivery} from "../../../api/delivery/deliveries";
import BoxSelection from "../helpers/BoxSelection";
import Box from "../../../model/Box";
import DelivererSelection from "../helpers/DelivererSelection";
import User from "../../../model/User";

export default function UpdateDeliveryDialog({
                                                 open,
                                                 delivery,
                                                 handleClose,
                                                 onDeliveryUpdated,
                                             }: {
    open: boolean;
    delivery: Delivery,
    handleClose: () => void;
    onDeliveryUpdated: (delivery: Delivery) => void;
}) {
    const [targetCustomerID, setTargetCustomerID] = React.useState('');
    const [targetBoxID, setTargetBoxID] = React.useState('');
    const [delivererID, setDelivererID] = React.useState('');

    async function handleUpdateDelivery() {
        const newDelivery = new Delivery(delivery.id, targetCustomerID, targetBoxID, delivererID, delivery.status, delivery.isActive);
        await updateDelivery(newDelivery);
        onDeliveryUpdated(newDelivery);
        handleClose();
    }

    return open ? (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Update User</DialogTitle>
            <DialogContentText>
                Enter new delivery details here:
            </DialogContentText>
            <DelivererSelection label="Customer" onSelect={(user: User | null) => setTargetCustomerID(user?.id || "")}/>
            <BoxSelection label="Target Box" onSelect={(box: Box | null) => setTargetBoxID(box?.id || "")}/>
            <DelivererSelection label="Deliverer" onSelect={(user: User | null) => setDelivererID(user?.id || "")}/>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleUpdateDelivery}>Update</Button>
            </DialogActions>
        </Dialog>
    ) : (
        <></>
    );
}

