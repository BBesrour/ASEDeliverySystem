import React from "react";
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import {createBox} from "../../../api/delivery/box";
import Box from "../../../api/model/Box";

export default function CreateBoxDialog({open, handleClose, onBoxCreated}: {
    open: boolean,
    handleClose: () => void,
    onBoxCreated: (box: Box) => void
}) {
    const [name, setName] = React.useState('');
    const [address, setAddress] = React.useState('');
    const [status, setStatus] = React.useState('');
    const [numberOfItems, setNumberOfItems] = React.useState('');

    async function handleCreateBox() {
        const newBox = new Box(null, name, address, status, numberOfItems);
        onBoxCreated(await createBox(newBox));
        handleClose();
    }

    return (
        open ?
            <Dialog open={open} onClose={handleClose}>
                <DialogTitle>Create New Box</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        Enter new box details here:
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        value={name}
                        onChange={(event) => setName(event.target.value)}
                        label="Name"
                        type="text"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        value={address}
                        onChange={(event) => setAddress(event.target.value)}
                        label="Address"
                        type="address"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        value={status}
                        onChange={(event) => setStatus(event.target.value)}
                        label="Status"
                        type="text"
                        fullWidth
                        variant="standard"
                    />
                    <TextField
                        autoFocus
                        margin="dense"
                        value={numberOfItems}
                        onChange={(event) => setNumberOfItems(event.target.value)}
                        label="Number of items"
                        type="number"
                        fullWidth
                        variant="standard"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button onClick={handleCreateBox}>Create</Button>
                </DialogActions>
            </Dialog>
            : <></>
    );
}