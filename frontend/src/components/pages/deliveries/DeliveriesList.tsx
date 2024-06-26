import Delivery from "../../../model/Delivery";
import React, {useEffect, useState} from "react";
import Grid from "@mui/material/Grid";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import QRCodeDialog from "./QRCodeDialog";
import Button from "@mui/material/Button";
import DeliveryStatusPage from "./DeliveryStatusPage";
import {deleteDelivery} from "../../../api/delivery/deliveries";
import TextField from "@mui/material/TextField";
import UpdateDeliveryDialog from "./UpdateDeliveryDialog";
import {getBoxes} from "../../../api/delivery/box";
import Box from "../../../model/Box";
import {getRole} from "../../../storage/user";
import {ROLE_DELIVERER, ROLE_DISPATCHER} from "../../../model/roles";


export default function DeliveriesList({deliveries, propertiesToShow, onDeliveryUpdated, onDeliveryDeleted}: {
    deliveries: Delivery[],
    propertiesToShow: string[],
    onDeliveryUpdated: (delivery: Delivery) => void,
    onDeliveryDeleted: (id: string | null) => void
}) {
    const [showQRDialogFor, setShowQRDialogFor] = useState<string | null>(null);
    const [showStatusDialogFor, setShowStatusDialogFor] = useState<string | null>(null);
    const [trackingCodeInput, setTrackingCodeInput] = useState<string>("");
    const [shownDeliveries, setShownDeliveries] = useState<Delivery[]>(deliveries);
    const [showUpdateDialogFor, setShowUpdateDialogFor] = useState<string | null>(null);
    const roles = getRole();

    function getDeliveryProperty(delivery: Delivery, property: string) {
        // @ts-ignore
        return delivery[property];
    }

    async function handleDeleteDelivery(id: string | null) {
        await deleteDelivery(id);
        onDeliveryDeleted(id);
    }

    function getDeliveriesByTrackingCode(deliveries: Delivery[], trackingCode: string): Delivery[] {
        if (!trackingCode) {
            return deliveries;
        }
        return deliveries.filter((delivery) => delivery.id?.startsWith(trackingCode));
    }

    useEffect(() => {
        setShownDeliveries(getDeliveriesByTrackingCode(deliveries, trackingCodeInput));
    }, [deliveries, trackingCodeInput]);


    const [boxNames, setBoxNames] = useState<Map<string, string>>(new Map());
    const [boxAddresses, setBoxAddresses] = useState<Map<string, string>>(new Map());
    const [boxes, setBoxes] = useState<Box[]>([]);
    useEffect(() => {
        getBoxes().then((boxes) => {
            setBoxes(boxes);
        });
    }, []);
    useEffect(() => {
        const newBoxNames = new Map();
        const newBoxAddresses = new Map();
        shownDeliveries.forEach((delivery) => {
            newBoxNames.set(delivery.id ?? "", boxes.filter((box) => box.id === delivery.targetBoxID)[0]?.name || "no box");
            newBoxAddresses.set(delivery.id ?? "", boxes.filter((box) => box.id === delivery.targetBoxID)[0]?.address || "no box");
        });
        setBoxNames(newBoxNames);
        setBoxAddresses(newBoxAddresses);
    }, [shownDeliveries, deliveries, boxes]);

    return <>
        <TextField
            id="tracking-code"
            label="Tracking Code"
            variant="outlined"
            onChange={(event) => setTrackingCodeInput(event.target.value)}
        />
        <br/>
        <br/>
        <Grid container spacing={4}>
            {shownDeliveries.map(delivery => (
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
                            <Typography>
                                targetBoxName: {boxNames.get(delivery.id ?? "")}
                            </Typography>
                            <Typography>
                                targetBoxAddress: {boxAddresses.get(delivery.id ?? "")}
                            </Typography>

                            {propertiesToShow.map(property => (
                                <Typography key={property}>
                                    {property}: {"" + getDeliveryProperty(delivery, property)}
                                </Typography>
                            ))}

                            {roles === ROLE_DELIVERER ?
                                <Button size="small" onClick={() => setShowStatusDialogFor(delivery.id)}>Status</Button> :
                                null}
                            {roles === ROLE_DISPATCHER ?
                                <Button size="small" onClick={() => setShowQRDialogFor(delivery.id)}>QR</Button> :
                                null}
                            {roles === ROLE_DISPATCHER ?
                                <Button size="small" onClick={() => setShowUpdateDialogFor(delivery.id)}>Edit</Button> :
                                null}
                            {roles === ROLE_DISPATCHER ?
                                <Button size="small" onClick={() => handleDeleteDelivery(delivery.id)}>Delete</Button> :
                                null}
                        </CardContent>
                    </Card>
                </Grid>
            ))}
        </Grid>
        <QRCodeDialog open={showQRDialogFor !== null} handleClose={() => setShowQRDialogFor(null)}
                      deliveryId={showQRDialogFor ?? ""}/>
        <DeliveryStatusPage id={showStatusDialogFor} handleClose={() => setShowStatusDialogFor(null)}/>
        <UpdateDeliveryDialog
            open={!!showUpdateDialogFor}
            handleClose={() => setShowUpdateDialogFor(null)}
            onDeliveryUpdated={(delivery) => {
                const index = deliveries.findIndex(d => d.id === delivery.id);
                deliveries[index] = delivery;
                onDeliveryUpdated(delivery);
                setShowUpdateDialogFor(null)
            }}
            delivery={deliveries.find(d => d.id === showUpdateDialogFor) || new Delivery(null, "", "", "", "", false)}
        />
    </>;
}