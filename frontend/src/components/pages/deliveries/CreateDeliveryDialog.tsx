import React from "react";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {createDelivery} from "../../../api/delivery/deliveries";
import Delivery from "../../../model/Delivery";
import BoxSelection from "../helpers/BoxSelection";
import Box from "../../../model/Box";
import User from "../../../model/User";
import DelivererSelection from "../helpers/DelivererSelection";

export default function CreateDeliveryDialog({open, handleClose, onDeliveryCreated}: {
    open: boolean,
    handleClose: () => void,
    onDeliveryCreated: (delivery: Delivery) => void
}) {
    const [targetCustomerID, setTargetCustomerID] = React.useState('');
    const [targetBoxID, setTargetBoxID] = React.useState('');
    const [delivererID, setDelivererID] = React.useState('');

    async function handleCreateDelivery() {
        const newDelivery = new Delivery(null, targetCustomerID, targetBoxID, delivererID, "ORDERED", true);
        onDeliveryCreated(await createDelivery(newDelivery));
        handleClose();
    }

    return (
        open ?
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Create New Delivery</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Enter new delivery details here:
                    </DialogContentText>
                    <DelivererSelection label="Deliverer" onSelect={(user: User | null) => setTargetCustomerID(user?.id || "")}/>
                    <BoxSelection label="Target Box" onSelect={(box: Box | null) => setTargetBoxID(box?.id || "")}/>
                    <DelivererSelection label="Deliverer" onSelect={(user: User | null) => setDelivererID(user?.id || "")}/>
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleCreateDelivery}>Create</Button>
                </DialogActions>
            </Dialog>
            : <></>
    );
}