import React, {useState} from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import User from "../../../model/User";
import {updateUser} from "../../../api/delivery/user";

export default function UpdateUserDialog({open, user, handleClose, onUserUpdated}: {
    open: boolean;
    user: User,
    handleClose: () => void;
    onUserUpdated: (user: User) => void;
}) {
    const [name, setName] = useState(user.name);
    const [email, setEmail] = useState(user.email);
    const [password, setPassword] = useState<string | null>(null);
    const [role, setRole] = useState(user.role);

    async function handleUpdateUser() {
        const newUser = new User(user.id, email, null, name, role);
        await updateUser(newUser);
        onUserUpdated(newUser);
        handleClose();
    }

    return open ? (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Update User</DialogTitle>
            <DialogContent>
                <DialogContentText>Change user details here:</DialogContentText>
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
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    label="Email"
                    type="email"
                    fullWidth
                    variant="standard"
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleUpdateUser}>Update</Button>
            </DialogActions>
        </Dialog>
    ) : (
        <></>
    );
}
