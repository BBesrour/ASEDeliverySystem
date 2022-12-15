import * as React from 'react';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import PageLayout from "./PageLayout";
import {useEffect, useState} from "react";
import Delivery from "../../api/model/Delivery";
import {getDeliveries} from "../../api/delivery/deliveries";
import Box from "../../api/model/Box";
import {getBoxes} from "../../api/delivery/box";

const cards = [1, 2, 3, 4, 5, 6, 7, 8, 9];

export default function BoxesPage() {
    const [boxes, setBoxes] = useState<Box[]>([]);
    useEffect(() => {
        getBoxes().then((boxes) => {
            setBoxes(boxes);
        });
    });
    const actionButtons = <>
        <Button variant="contained">Create new box</Button>
        <Button variant="outlined">temp</Button>
    </>;
    const content = <Grid container spacing={4}>
        {boxes.map((box) => (
            <Grid item key={box.id} xs={12} sm={6} md={4}>
                <Card
                    sx={{height: '100%', display: 'flex', flexDirection: 'column'}}
                >

                    <CardContent sx={{flexGrow: 1}}>
                        <Typography gutterBottom variant="h5" component="h2">
                            Box
                        </Typography>
                        <Typography>
                            Name: {box.name}
                        </Typography>
                        <Typography>
                            Address: {box.address}
                        </Typography>
                        <Typography>
                            Status: {box.status}
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
        "Pick-up boxes",
        "Text text text text text text",
        actionButtons,
        content
    );
}