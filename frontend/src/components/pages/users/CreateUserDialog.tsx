import React, {useState} from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import User, {UserRole} from "../../../model/User";
import {createUser} from "../../../api/delivery/user";
import UserRoleSelection from "./UserRoleSelection";

export default function CreateUserDialog({open, handleClose, onUserCreated}: {
    open: boolean;
    handleClose: () => void;
    onUserCreated: (user: User) => void;
}) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState<UserRole>("ROLE_CUSTOMER");

    async function handleCreateUser() {
        if (!email) {
            alert("Email is required");
            return;
        }
        if (!password) {
            alert("Password is required");
            return;
        }
        const newUser = new User(null, email, password, role);
        try {
            const createdUser = await createUser(newUser);
            onUserCreated(createdUser);
        } catch (e) {
            console.error(e);
            alert(e);
            return;
        }
        handleClose();
    }

    return open ? (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Create New User</DialogTitle>
            <DialogContent>
                <DialogContentText>Enter new user details here:</DialogContentText>
                <TextField
                    margin="dense"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    label="Email"
                    type="email"
                    fullWidth
                    variant="standard"
                />
                <TextField
                    margin="dense"
                    value={password}
                    onChange={(event) => setPassword(event.target.value)}
                    label="Password"
                    type="password"
                    fullWidth
                    variant="standard"
                />
                <br />
                <br />
                <UserRoleSelection role={role} setRole={setRole} />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleCreateUser}>Create</Button>
            </DialogActions>
        </Dialog>
    ) : (
        <></>
    );
}
