import * as React from 'react';
import {useEffect, useState} from 'react';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import PageLayout from "../PageLayout";
import {getDeliveries} from "../../../api/delivery/deliveries";
import Delivery from "../../../model/Delivery";
import CreateDeliveryDialog from "./CreateDeliveryDialog";

export default function DeliveriesPage() {
    const [deliveries, setDeliveries] = useState<Delivery[]>([]);
    const [showCreateDialog, setShowCreateDialog] = useState(false);
    useEffect(() => {
        getDeliveries().then((deliveries) => {
            setDeliveries(deliveries);
        });
    }, []);
    const actionButtons = <>
        <Button variant="contained" onClick={() => setShowCreateDialog(true)}>Create delivery</Button>
    </>;
    const content = <Grid container spacing={4}>
        {deliveries.map(delivery => (
            <Grid item key={delivery.id} xs={12} sm={6} md={4}>
                <Card
                    sx={{height: '100%', display: 'flex', flexDirection: 'column'}}
                >

                    <CardContent sx={{flexGrow: 1}}>
                        <Typography gutterBottom variant="h5" component="h2">
                            Delivery
                        </Typography>
                        <Typography>
                            ID: {delivery.id}
                        </Typography>
                    </CardContent>
                </Card>
            </Grid>
        ))}
    </Grid>;
    return <>
        <PageLayout title="Deliveries" description={null} actionButtons={actionButtons} content={content}/>
        <CreateDeliveryDialog
            open={showCreateDialog}
            handleClose={() => setShowCreateDialog(false)}
            onDeliveryCreated={(delivery) => setDeliveries([delivery, ...deliveries])}
        />
    </>;
}