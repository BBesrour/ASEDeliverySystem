import React from "react";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {updateBox} from "../../../api/delivery/box";
import Box from "../../../model/Box";

export default function UpdateBoxDialog({open, handleClose, onBoxUpdated, box}: {
    open: boolean,
    handleClose: () => void,
    onBoxUpdated: (box: Box) => void,
    box: Box
}) {
    const [name, setName] = React.useState(box.name);
    const [address, setAddress] = React.useState(box.address);

    async function handleUpdateBox() {
        box.name = name;
        box.address = address;
        let updatedBox = null;
        try {
            updatedBox = await updateBox(box);
        } catch (error) {
            // @ts-ignore
            alert("Error updating box: " + (await error.response.json()).error);
            return;
        }
        onBoxUpdated(updatedBox);
        handleClose();
    }

    return (
        open ?
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Update Box</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Enter new box details here:
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        defaultValue={name}
                        onChange={(event) => setName(event.target.value)}
                        label="Name"
                        type="text"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        defaultValue={address}
                        onChange={(event) => setAddress(event.target.value)}
                        label="Address"
                        type="address"
                        fullWidth
                        variant="standard"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleUpdateBox}>Update</Button>
                </DialogActions>
            </Dialog>
            : <></>
    );
}


