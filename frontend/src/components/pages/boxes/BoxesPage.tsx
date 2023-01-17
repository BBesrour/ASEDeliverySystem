import * as React from "react";
import { useEffect, useState } from "react";
import Button from "@mui/material/Button";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Grid from "@mui/material/Grid";
import Typography from "@mui/material/Typography";
import PageLayout from "../PageLayout";
import Box from "../../../model/Box";
import { deleteBox, getBoxes } from "../../../api/delivery/box";
import CreateBoxDialog from "./CreateBoxDialog";

export default function BoxesPage() {
  const [boxes, setBoxes] = useState<Box[]>([]);
  const [showCreateDialog, setShowCreateDialog] = useState(false);

  useEffect(() => {
    getBoxes().then((boxes) => {
      setBoxes(boxes);
    });
  }, []);

  function camelize(str: string) {
    return str
      .replace(/(?:^\w|[A-Z]|\b\w)/g, function (word, index) {
        return index === 0 ? word.toLowerCase() : word.toUpperCase();
      })
      .replace(/\s+/g, "");
  }

  const actionButtons = (
    <>
      <Button variant="contained" onClick={() => setShowCreateDialog(true)}>
        Create new box
      </Button>
    </>
  );

  const onClickDelete = (id: string | null) => {
    if (id) {
      deleteBox(id)
        .then((res) => alert("Delete Successfull"))
        .catch((err) => console.log(err));
    }
  };

  const content = (
    <Grid container spacing={4}>
      {boxes.map((box) => (
        <Grid item key={box.id} xs={12} sm={6} md={4}>
          <Card
            sx={{ height: "100%", display: "flex", flexDirection: "column" }}
          >
            <CardContent sx={{ flexGrow: 1 }}>
              <Typography gutterBottom variant="h5" component="h2">
                {box.name}
              </Typography>
              <Typography>Name: </Typography>
              <Typography>Address: {box.address}</Typography>
              <Typography>Assigned Deliverer: {box.assigned_to}</Typography>
              <Typography>
                Assigned Customers: {box.assigned_customers}
              </Typography>
            </CardContent>
            <CardActions>
              <Button size="small">Edit</Button>
              <Button onClick={() => onClickDelete(box.id)} size="small">
                Delete
              </Button>
            </CardActions>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
  return (
    <>
      <PageLayout
        title={"Boxes"}
        description={""}
        actionButtons={actionButtons}
        content={content}
      />
      <CreateBoxDialog
        open={showCreateDialog}
        handleClose={() => setShowCreateDialog(false)}
        onBoxCreated={(box) => setBoxes([box, ...boxes])}
      />
    </>
  );
}
