import Delivery from "../../../model/Delivery";
import React, {useState} from "react";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import QRCodeDialog from "./QRCodeDialog";
import Button from "@mui/material/Button";
import DeliveryStatusPage from "./DeliveryStatusPage";
import {deleteDelivery} from "../../../api/delivery/deliveries";
import UpdateDeliveryDialog from "./UpdateDeliveryDialog";


export default function DeliveriesList({deliveries, propertiesToShow, onDeliveryDeleted, onDeliveryUpdated}: {
    deliveries: Delivery[],
    propertiesToShow: string[],
    onDeliveryDeleted: (id: string | null) => void
    onDeliveryUpdated: (delivery: Delivery) => void
}) {
    const [showQRDialogFor, setShowQRDialogFor] = useState<string | null>(null);
    const [showStatusDialogFor, setShowStatusDialogFor] = useState<string | null>(null);
    const [currentDelivery, setCurrentDelivery] = useState<Delivery | null>(null);


    function getDeliveryProperty(delivery: Delivery, property: string) {
        // @ts-ignore
        return delivery[property];
    }

    function handleDeleteDelivery(delivery: Delivery) {
        deleteDelivery(delivery.id);
        onDeliveryDeleted(delivery.id);
    }

    return <>
        <Grid container spacing={4}>
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
                                <Typography key={property}>
                                    {property}: {getDeliveryProperty(delivery, property)}
                                </Typography>
                            ))}
                            <Button size="small" onClick={() => setShowStatusDialogFor(delivery.id)}>Status</Button>
                            <Button size="small" onClick={() => setShowQRDialogFor(delivery.id)}>QR</Button>
                            <Button size="small" onClick={() => setCurrentDelivery(delivery)}>Edit</Button>
                            <Button size="small" onClick={() => handleDeleteDelivery(delivery)}>Delete</Button>
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
        <QRCodeDialog open={showQRDialogFor !== null} handleClose={() => setShowQRDialogFor(null)}
                      deliveryId={showQRDialogFor ?? ""}/>
        <DeliveryStatusPage id={showStatusDialogFor} handleClose={() => setShowStatusDialogFor(null)}/>
        <UpdateDeliveryDialog
            open={!!currentDelivery}
            delivery={currentDelivery || new Delivery(null, "", "", "", "", false)}
            handleClose={() => setCurrentDelivery(null)}
            onDeliveryUpdated={onDeliveryUpdated}
        />
    </>;
}
