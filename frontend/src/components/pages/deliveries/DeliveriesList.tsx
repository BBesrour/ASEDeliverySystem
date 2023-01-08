import Delivery from "../../../model/Delivery";
import React from "react";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import {Button, CardActions} from "@mui/material";
import {deleteDelivery} from "../../../api/delivery/deliveries";
import {getDeliveries} from "../../../api/delivery/deliveries";


export default function DeliveriesList({deliveries, propertiesToShow, onDeliveryDeleted}: {
    deliveries: Delivery[],
    propertiesToShow: string[],
    onDeliveryDeleted: (id : string | null) => void
}) {
    function getDeliveryProperty(delivery: Delivery, property: string) {
        // @ts-ignore
        return delivery[property];
    }

    async function handleDeleteDelivery(id: string | null) {
        await deleteDelivery(id);
        onDeliveryDeleted(id);
    }

    return <Grid container spacing={4}>
        {deliveries.map(delivery => (
            <Grid item key={delivery.id} xs={12} sm={6} md={4}>
                <Card
                    sx={{height: '100%', display: 'flex', flexDirection: 'column'}}
                >

                    <CardContent sx={{flexGrow: 1}}>
                        <Typography gutterBottom variant="h5" component="h2">
                            Delivery
                        </Typography>
                        {propertiesToShow.map(property => (
                            <Typography>
                                {property}: {getDeliveryProperty(delivery, property)}
                            </Typography>
                        ))}
                    </CardContent>
                    <CardActions>
                        <Button size="small">Edit</Button>
                        <Button size="small" onClick={() => handleDeleteDelivery(delivery.id)}>Delete</Button>
                    </CardActions>
                </Card>
            </Grid>
        ))}
    </Grid>;
}