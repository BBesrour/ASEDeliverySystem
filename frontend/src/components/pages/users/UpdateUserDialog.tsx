import React, {useState} from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import Switch from "@mui/material/Switch";
import DialogTitle from "@mui/material/DialogTitle";
import FormGroup from "@mui/material/FormGroup";
import FormControlLabel from "@mui/material/FormControlLabel";
import User from "../../../model/User";
import {updateUser} from "../../../api/delivery/user";
import UserRoleSelection from "./UserRoleSelection";

export default function UpdateUserDialog({open, user, handleClose, onUserUpdated}: {
    open: boolean;
    user: User,
    handleClose: () => void;
    onUserUpdated: (user: User) => void;
}) {
    const [name, setName] = useState(user.name || "");
    const [email, setEmail] = useState(user.email || "");
    const [changePassword, setChangePassword] = useState(false);
    const [password, setPassword] = useState<string | null>(null);
    const [role, setRole] = useState(user.role || "ROLE_CUSTOMER");

    async function handleUpdateUser() {
        if (!email) {
            alert("Email is required");
            return;
        }
        if (changePassword && !password) {
            alert("Password is required");
            return;
        }
        if (!name) {
            alert("Name is required");
            return;
        }
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
                    margin="dense"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    label="Email"
                    type="email"
                    fullWidth
                    variant="standard"
                />
                <br />
                <br />
                <UserRoleSelection role={role} setRole={setRole} />
                <FormGroup>
                    <FormControlLabel control={
                        <Switch checked={changePassword} onChange={(event) => {
                            const pwdShouldBeChanged = event.target.checked;
                            setChangePassword(pwdShouldBeChanged);
                            if (!pwdShouldBeChanged) {
                                setPassword(null);
                            } else {
                                setPassword("");
                            }
                        }}/>
                    } label="Change password" />
                </FormGroup>
                {changePassword ? (
                    <TextField
                        margin="dense"
                        value={password ?? ""}
                        onChange={(event) => setPassword(event.target.value)}
                        label="Password"
                        type="password"
                        fullWidth
                        variant="standard"
                    />
                ) : (
                    <></>
                )}
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
