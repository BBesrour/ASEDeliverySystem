import * as React from 'react';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import PageLayout from "./PageLayout";
import {useEffect, useState} from "react";
import {getAllUsers} from "../../api/delivery/user";
import User from "../../api/model/User";


export default function UsersPage() {
    const [users, setUsers] = useState<User[]>([]);
    useEffect(() => {
        getAllUsers().then((users) => {
            setUsers(users);
        });
    });
    const actionButtons = <>
        <Button variant="contained">Create new user</Button>
        <Button variant="outlined">temp</Button>
    </>;
    const content = <Grid container spacing={4}>
        {users.map(user => (
            <Grid item key={user.id} xs={12} sm={6} md={4}>
                <Card
                    sx={{height: '100%', display: 'flex', flexDirection: 'column'}}
                >

                    <CardContent sx={{flexGrow: 1}}>
                        <Typography gutterBottom variant="h5" component="h2">
                            {user.name}
                        </Typography>
                        <Typography>
                            {user.email}
                        </Typography>
                    </CardContent>
                    <CardActions>
                        <Button size="small">Edit</Button>
                        <Button size="small">Delete</Button>
                    </CardActions>
                </Card>
            </Grid>
        ))}
    </Grid>;
    return PageLayout(
        "Users",
        "Text text text text text text",
        actionButtons,
        content
    );
}