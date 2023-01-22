import * as React from "react";
import {useEffect, useState} from "react";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import PageLayout from "../PageLayout";
import CreateUserDialog from "./CreateUserDialog";
import {deleteUser, getAllUsers} from "../../../api/delivery/user";
import User from "../../../model/User";
import UpdateUserDialog from "./UpdateUserDialog";

export default function UsersPage() {
  const [users, setUsers] = useState<User[]>([]);
  const [showCreateDialog, setShowCreateDialog] = useState(false);
  const [userToEdit, setUserToEdit] = useState<User | null>(null);

  useEffect(() => {
    getAllUsers().then((users) => {
      setUsers(users);
    });
  }, []);

  const actionButtons = (
    <>
      <Button variant="contained" onClick={() => setShowCreateDialog(true)}>
        Create new user
      </Button>
    </>
  );

  const onClickDelete = (id: string | null) => {
    if (id) {
      deleteUser(id)
        .then(() => alert("Delete Successful"))
        .catch((err) => console.log(err));
    }
  };

  const content = (
    <Grid container spacing={4}>
      {users.map((user) => (
        <Grid item key={user.id} xs={12} sm={6} md={4}>
          <Card
            sx={{ height: "100%", display: "flex", flexDirection: "column" }}
          >
            <CardContent sx={{ flexGrow: 1 }}>
              <Typography gutterBottom variant="h5" component="h2">
                {user.name}
              </Typography>
              <Typography>ID: {user.id}</Typography>
              <Typography>Email: {user.email}</Typography>
            </CardContent>
            <CardActions>
              <Button onClick={() => setUserToEdit(user)} size="small">
                Edit
              </Button>
              <Button onClick={() => onClickDelete(user.id)} size="small">
                Delete
              </Button>
            </CardActions>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
  // @ts-ignore
  return (
    <>
      <PageLayout
        title={"Users"}
        description={""}
        actionButtons={actionButtons}
        content={content}
      />
      <CreateUserDialog
        open={showCreateDialog}
        handleClose={() => setShowCreateDialog(false)}
        onUserCreated={(user) => setUsers([user, ...users])}
      />
      <UpdateUserDialog
          open={!!userToEdit}
          user={userToEdit || new User(null, "", "")}
          handleClose={() => setUserToEdit(null)}
          onUserUpdated={(user) => setUsers(users.map((u) => u.id === user.id ? user : u))}
      />
    </>
  );
}
