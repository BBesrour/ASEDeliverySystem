import React, { useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Delivery from "../../../model/Delivery";
import { updateDelivery } from "../../../api/delivery/deliveries";
import DeliveryFields from "./DeliveryFields";
import { DialogContent } from "@mui/material";

export default function UpdateDeliveryDialog({
  open,
  handleClose,
  onDeliveryUpdated,
  delivery,
}: {
  open: boolean;
  handleClose: () => void;
  onDeliveryUpdated: (delivery: Delivery) => void;
  delivery: Delivery;
}) {
  const [changedDelivery, setChangedDelivery] = useState(delivery);

  const updateDeliveryAndClose = () => {
    updateDelivery(changedDelivery)
      .then(() => {
        onDeliveryUpdated(changedDelivery);
        handleClose();
      })
      .catch((err) => {
        if (err.response.status === 409) {
          alert("Box does not exist! or assigned to another Customer");
        }
      });
  };

  return open ? (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>Update Delivery</DialogTitle>
      <DialogContent>
        <DialogContentText>Enter new delivery details here:</DialogContentText>
        <DeliveryFields
          delivery={delivery}
          onDeliveryUpdate={setChangedDelivery}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleClose}>Cancel</Button>
        <Button onClick={updateDeliveryAndClose}>Update</Button>
      </DialogActions>
    </Dialog>
  ) : (
    <></>
  );
}
