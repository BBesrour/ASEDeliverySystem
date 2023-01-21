import React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import User from "../../../model/User";
import {createUser} from "../../../api/delivery/user";

export default function CreateUserDialog({
  open,
  handleClose,
  onUserCreated,
}: {
  open: boolean;
  handleClose: () => void;
  onUserCreated: (user: User) => void;
}) {
  const [name, setName] = React.useState("");
  const [email, setEmail] = React.useState("");

  async function handleCreateUser() {
    const newUser = new User(null, email, name);
    onUserCreated(await createUser(newUser));
    handleClose();
  }

  return open ? (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>Create New User</DialogTitle>
      <DialogContent>
        <DialogContentText>Enter new user details here:</DialogContentText>
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
        <Button onClick={handleCreateUser}>Create</Button>
      </DialogActions>
    </Dialog>
  ) : (
    <></>
  );
}
