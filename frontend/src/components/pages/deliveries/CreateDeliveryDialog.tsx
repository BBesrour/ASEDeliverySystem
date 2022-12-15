import React from "react";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {createDelivery} from "../../../api/delivery/deliveries";
import Delivery from "../../../api/model/Delivery";

export default function CreateDeliveryDialog({open, handleClose, onDeliveryCreated}: {
    open: boolean,
    handleClose: () => void,
    onDeliveryCreated: (delivery: Delivery) => void
}) {
    const [targetCustomerID, setTargetCustomerID] = React.useState('');
    const [targetBoxID, setTargetBoxID] = React.useState('');
    const [delivererID, setDelivererID] = React.useState('');

    async function handleCreateDelivery() {
        const newDelivery = new Delivery(null, targetCustomerID, targetBoxID, delivererID);
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
                    <TextField
                        autoFocus
                        margin="dense"
                        value={targetCustomerID}
                        onChange={(event) => setTargetCustomerID(event.target.value)}
                        label="Name"
                        type="text"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        value={targetBoxID}
                        onChange={(event) => setTargetBoxID(event.target.value)}
                        label="Name"
                        type="text"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        value={delivererID}
                        onChange={(event) => setDelivererID(event.target.value)}
                        label="Name"
                        type="text"
                        fullWidth
                        variant="standard"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleCreateDelivery}>Create</Button>
                </DialogActions>
            </Dialog>
            : <></>
    );
}